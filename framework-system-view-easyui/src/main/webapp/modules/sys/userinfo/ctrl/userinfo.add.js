define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');
	
	// 编辑角色信息
	var UserInfoAdd = Page.extend(function() {
		
		// @override
		this.object = {
			isValid: 'T'
		};
	
		// @override
		this.load = function(panel) {
			var form = panel.find('form');
			
			this.data = $.extend({}, this.object);

			form.form('disableValidation').form('load', this.data)
				.form('focus');
		};
		
		// @override
		this.submit = function(panel, data, closeCallback) {
			var form = panel.find('form');
			
			// 开启校验
			var isValid = form.form('enableValidation').form('validate');
			
			if (isValid) {
				this.newObject = form.form('value');
				form.form('ajax', {
					url: Config.ContextPath + 'system/userinfo',
					method: 'post',
					data: data
				}).then(function(){
					return require('loaders/cache/loader.system').loadAll()
				}).then(closeCallback);
			}
			
			return false;
		}
		
		// @override
		this.onClose = function(table) {
			var newObject = this.newObject;
			if (!newObject) return;
			table.datagrid('reload');
			this.newObject=null;
		};
	});
	
	return UserInfoAdd;
});