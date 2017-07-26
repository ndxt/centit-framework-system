define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	
	var Page = require('core/page');
	
	// 编辑用户角色
	var UserInfoRoleEdit = Page.extend(function() {
		var _self = this;
		
		// @override
		this.load = function(panel, data) {
			
			var form = panel.find('form');
			Core.ajax(Config.ContextPath+'system/userrole/'+data.roleCode+'/'+data.userCode, {
				method: 'get',
				data: {
					obtainDate: data.obtainDate
				}
			}).then(function(data) {
				_self.data = data;
				
				form.form('disableValidation').form('load', data)
					.form('readonly', ['roleCode'])
					.form('focus');
			});
		};
		
		// @override
		this.submit = function(panel, data, closeCallback) {
			var form = panel.find('form');
			
			form.form('enableValidation');
			var isValid = form.form('validate');
			
			if (isValid) {
			
				form.form('ajax', {
					url: Config.ContextPath+'system/userrole/'+data.roleCode+'/'+data.userCode,
					method: 'put',
					data: data
				}).then(function(){
					return require('loaders/cache/loader.system').loadAll()
				}).then(closeCallback);
			}
			
			return false;
		};
		
		// @override
		this.onClose = function(table) {
			table.datagrid('reload');
		};
	});
	
	return UserInfoRoleEdit;
});