define(function(require) {
	var Config = require('config');
	var Core = require('core/core');

	var Page = require('core/page');

	// 机构添加用户
	var UserInfoUnitAdd = Page.extend(function() {
		var _self=this;
		// @override
		this.object = {
			isPrimary: 'T'
		};

		// @override
		this.load = function(panel) {
			var _self=this;
			// 获取父窗口的用户信息
			//var userinfo = this.parent.data.userInfo;
      var userinfo = this.parent.data;

			var data = this.data = $.extend({}, this.object, {
				userCode: userinfo.userCode,
				userName: userinfo.userName
			});

			panel.find('form').form('disableValidation')
				.form('load', data)

		};

		// @override
		this.submit = function(panel, data, closeCallback) {
			var form = panel.find('form');

			form.form('enableValidation');
			var isValid = form.form('validate');

			if (isValid) {
				form.form('ajax', {
					url: Config.ContextPath+'system/userunit',
					data: data
				}).then(function(){
					return require('loaders/cache/loader.system').loadAll()
				}).then(function(data){
					var table_userinfo=_self.parent.parent.panel.find("#userInfoTable");
					//var row=table_userinfo.datagrid("getSelected");
					//table_userinfo.datagrid("updateRow",{index:0,row:data});
					table_userinfo.datagrid("reload");
					closeCallback();
				});
			}

			return false;
		};

		// @override
		this.onClose = function(table) {
			table.datagrid('reload');
		};
	});

	return UserInfoUnitAdd;
});
