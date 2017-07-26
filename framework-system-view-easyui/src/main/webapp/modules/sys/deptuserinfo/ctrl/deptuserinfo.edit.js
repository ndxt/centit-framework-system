define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');
	
	// 编辑角色信息
	var DeptUserInfoEdit = Page.extend(function() {
		var _self = this;
	
		// @override
		this.load = function(panel, data) {
			var form = panel.find('form');
			
			Core.ajax(Config.ContextPath + 'system/userinfo/' + data.userCode, {
				method: 'get'
			}).then(function(data) {
				_self.data = data;
				
				form.form('disableValidation')
					.form('load', data)
					.form('readonly', ['userName', 'loginName'])
					.form('focus');
			});
		};
		
		// @override
		this.submit = function(panel, data, closeCallback) {
			var form = panel.find('form');
			
			// 开启校验
			var isValid = form.form('enableValidation').form('validate');
			
			if (isValid) {
				this.newObject = form.form('value');
				form.form('ajax', {
					url: Config.ContextPath + 'system/userinfo/' + data.userCode,
					method: 'put',
					data: data
				}).then(closeCallback);
			}
			
			return false;
		};
		
		// @override
		this.onClose = function(table) {
			var newObject = this.newObject;
			if (!newObject) return;
			var layout = $('#deptuserinfo_panel').layout('panel', 'east');
			layout.panel('refresh', Config.ViewContextPath + 'modules/sys/deptuserinfo/deptuserinfo-all.html');
			table.datagrid('updateRow',newObject);
			this.newObject=null;
		};
	});
	
	return DeptUserInfoEdit;
});