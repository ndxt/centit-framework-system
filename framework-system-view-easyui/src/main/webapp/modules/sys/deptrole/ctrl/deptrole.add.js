define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');
	var Cache = require('core/cache');
	// 新增角色信息
	var DeptRoleAdd = Page.extend(function() {

		// @override
		this.object = {
			isValid: 'T'
		}

		// @override
		this.load = function(panel) {
			form = panel.find('form');
			var loginuser=Cache.get('loginuser');
			var primaryUnit=loginuser.primaryUnit;
			form.form('disableValidation')
				.form('load', this.object)
				.form('addValidation', {
					roleCode: {
						required: true,
					    validType: {
					    	remote: [Config.ContextPath+'system/roleinfo/notexists/'+primaryUnit+'-'+'{{roleCode}}',
								'roleCode']
					    }
					},
					roleName: {
						required:true,
						validType:{
							remote:[Config.ContextPath+'system/roleinfo/nameexists/{{roleName}}/'+primaryUnit, 'roleName']
						}
					}
				})
				.form('focus');
		}

		// @override
		this.submit = function(panel, data, closeCallback) {
			var form = panel.find('form');
			//var loginuser=Cache.get('loginuser');
			//var primaryUnit=loginuser.primaryUnit;
			var isValid = form.form('enableValidation').form('validate');

			if (isValid) {
				form.form('ajax', {
					url: Config.ContextPath + 'system/roleinfo', ///dept/'+primaryUnit
					method: 'post'
				}).then(closeCallback);
			}

			return false;
		};

		// @override
		this.onClose = function(table, data) {
			table.datagrid('reload');
		};
	});

	return DeptRoleAdd;
});
