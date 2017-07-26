define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	
	var Page = require('core/page');
	var Utils = require('core/utils');
	
	// 机构添加用户
	var UserInfoRoleAdd = Page.extend(function() {
		// @override
		this.object = {
			isPrimary: 'T',
			obtainDate:Utils.formateDate(new Date(),'yyyy-MM-dd')
		};
		
		// @override
		this.load = function(panel) {
			this.refresh=false;
			// 获取父窗口的用户信息
			var userinfo = this.parent.data;
			
			var data = this.data = $.extend({}, this.object, {
				userCode: userinfo.userCode,
				userName: userinfo.userName
			});
			
			panel.form('disableValidation')
				.find('form')
				.form('load', data)
				.form('focus');
		};
		
		// @override
		this.submit = function(panel, data, closeCallback) {
			var form = panel.find('form');
			var _self=this;			
			form.form('enableValidation');
			var isValid = form.form('validate');
			
			if (isValid) {
				form.form('ajax', {
					url: Config.ContextPath+'system/userrole',
					data: data
				}).then(function(){
					_self.refresh=true;
					closeCallback();
				});
				
			}
			
			return false;
		};
		
		// @override
		this.onClose = function(table) {
			if(this.refresh)
			table.datagrid('reload');
		};
	});
	
	return UserInfoRoleAdd;
});