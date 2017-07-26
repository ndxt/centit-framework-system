define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');
	
	var DeptUserInfoRoleAdd = require('../ctrl/deptuserinfo.role.add');
	var DeptUserInfoRoleEdit = require('../ctrl/deptuserinfo.role.edit');
	var DeptUserInfoRoleRemove = require('../ctrl/deptuserinfo.role.remove');
	var DeptUserInfoRoleBan = require('../ctrl/deptuserinfo.role.ban');
	
	var DeptUserInfoUnitAdd = require('../ctrl/deptuserinfo.unit.add');
	var DeptUserInfoUnitEdit = require('../ctrl/deptuserinfo.unit.edit');
	var DeptUserInfoUnitRemove = require('../ctrl/deptuserinfo.unit.remove');
	
	var DeptUserInfoAll = Page.extend(function() {
		var _self = this;
	
		this.injecte([
		  new DeptUserInfoRoleAdd('deptuserinfo_role_add'),   
		  new DeptUserInfoRoleEdit('deptuserinfo_role_edit'),
		  new DeptUserInfoRoleRemove('deptuserinfo_role_remove'),
		  new DeptUserInfoRoleBan('deptuserinfo_role_ban'),
		  new DeptUserInfoUnitAdd('deptuserinfo_unit_add'),   
		  new DeptUserInfoUnitEdit('deptuserinfo_unit_edit'),
		  new DeptUserInfoUnitRemove('deptuserinfo_unit_remove')
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
				
				form.form('disableValidation')
					.form('load', data)
					.form('readonly', ['userName', 'loginName'])
					.form('focus');
			});
			//获取角色信息table中所需要的数据
			var tableRole = panel.find('table.deptrole');
			tableRole.cdatagrid({
				controller: _self,
				url: Config.ContextPath+'system/userrole/userroles/'+data.userCode+'?type=D'
			})
			//获取机构信息table中所需要的数据
			var tableUnit = panel.find('table.deptunit');
			tableUnit.cdatagrid({
				controller: _self,
				url: Config.ContextPath+'system/userunit/userunits/'+data.userCode
			})
		};

	});
	
	return DeptUserInfoAll;
});