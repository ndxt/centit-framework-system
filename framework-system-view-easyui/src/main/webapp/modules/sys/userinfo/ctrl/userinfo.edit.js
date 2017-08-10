define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');
	
	// 编辑角色信息
	var UserInfoEdit = Page.extend(function() {
		var _self = this;
	
		// @override
		this.load = function(panel, data) {
			var form = panel.find('form');
			
			Core.ajax(Config.ContextPath + 'system/userinfo/' + data.userCode, {
				method: 'get'
			}).then(function(data) {
				_self.data = data;
				
				form
					.form('disableValidation')
					.form('load', data);
			});
		};
		
		// @override
		this.submit = function(panel, data, closeCallback) {
			var form = panel.find('form');
			
			// 开启校验
			form.form('enableValidation');
			var isValid = form.form('validate');
			
			if (isValid) {
				this.newObject = form.form('value');
                data._method = 'PUT';
				form.form('ajax', {
					url: Config.ContextPath + 'system/userinfo/' + data.userCode,
					method: 'put',
					data: data
				}).then(function(){
					return require('loaders/cache/loader.system').loadAll();
				}).then(closeCallback);
			}
			
			return false;

		};
		
		// @override
		this.onClose = function(table) {
			var newObject = this.newObject;
			if (!newObject) return;
			var layout = $('#userinfo_panel').layout('panel', 'east');
			layout.panel('refresh', Config.ViewContextPath + 'modules/sys/userinfo/userinfo-all.html');
			table.datagrid('updateRow',newObject);
			this.newObject=null;
		};
	});
	
	return UserInfoEdit;
});