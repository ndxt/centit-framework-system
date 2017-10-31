define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');

	var UserInfoRoleAdd = require('../ctrl/userinfo.role.add');
	var UserInfoRoleEdit = require('../ctrl/userinfo.role.edit');
	var UserInfoRoleRemove = require('../ctrl/userinfo.role.remove');
	var UserInfoRoleBan = require('../ctrl/userinfo.role.ban');

	var UserInfoUnitAdd = require('../ctrl/userinfo.unit.add');
	var UserInfoUnitEdit = require('../ctrl/userinfo.unit.edit');
	var UserInfoUnitRemove = require('../ctrl/userinfo.unit.remove');
	var RoleInfoOperate = require('modules/sys/roleinfo/ctrl/roleinfo.operate');

	var UserAll = Page.extend(function() {
		var _self = this;

		this.injecte([
		  new UserInfoRoleAdd('userinfo_role_add'),
		  new UserInfoRoleEdit('userinfo_role_edit'),
		  new UserInfoRoleRemove('userinfo_role_remove'),
		  new UserInfoRoleBan('userinfo_role_ban'),
		  new UserInfoUnitAdd('userinfo_unit_add'),
		  new UserInfoUnitEdit('userinfo_unit_edit'),
		  new RoleInfoOperate('roleinfo_operate'),
		  new UserInfoUnitRemove('userinfo_unit_remove')
		]);

		// @override
		this.load = function(panel, data) {
			this.data = data;

			//var form = panel.find('form').form('load', data);
			//获取表form中所要展示的数据
			var form = panel.find('form');

			Core.ajax(Config.ContextPath + 'system/userinfo/' + data.userCode, {
				method: 'get'
			}).then(function(data) {
				_self.data = data;

				form.form('disableValidation').form('load', data)
					.form('readonly', ['userName', 'loginName'])
					.form('focus');
			});
			//获取角色信息table中所需要的数据
			var tableRole = panel.find('table.role');
			tableRole.cdatagrid({
				controller: _self,
				url: Config.ContextPath+'system/userrole/userroles/'+data.userCode/*+'?type=S'*/
			});
			//获取角色信息table中所需要的数据
/*			var tableRole = panel.find('table.unitRole');
			tableRole.cdatagrid({
				controller: _self,
				url: Config.ContextPath+'system/userrole/userroles/'+data.userCode+'?type=D'
			});*/
			//获取机构信息table中所需要的数据
			var tableUnit = panel.find('table.unit');
			tableUnit.cdatagrid({
				controller: _self,
				url: Config.ContextPath+'system/userunit/userunits/'+data.userCode
			});
		};

	});

	return UserAll;
});
