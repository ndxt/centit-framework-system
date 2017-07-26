define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');
	
	// 新增角色信息
	var RoleInfoAdd = Page.extend(function() {
		
		// @override
		this.object = {
			isValid: 'T'           
		}
		
		// @override
		this.load = function(panel) {
			form = panel.find('form');
			
			form.form('disableValidation')
				.form('load', this.object)
				.form('addValidation', {
					roleCode: {
						required: true,
					    validType: {
					    	remote: [Config.ContextPath+'system/roleinfo/notexists/{{roleCode}}', 'roleCode']
					    }
					}
				})
				.form('focus');
		}		
		// @override
		this.submit = function(panel, data, closeCallback) {
			var form = panel.find('form');
			
			var value = form.form('value').isGlobal;
			
			var isValid = form.form('enableValidation').form('validate');
			
			if (isValid) {
				form.form('ajax', {
					url: value=='F'?  Config.ContextPath + 'system/roleinfo/public':
							Config.ContextPath + 'system/roleinfo/global',
					method: 'post'
				}).then(function(){
					return require('loaders/cache/loader.system').loadAll()
				}).then(closeCallback);
			}
			
			return false;
		};
		
		// @override
		this.onClose = function(table, data) {
			table.datagrid('reload');
		};
	});
	
	return RoleInfoAdd;
});