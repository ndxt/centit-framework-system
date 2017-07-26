define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');
	
	var UserInfoRoleAdd = require('../ctrl/deptuserinfo.role.add');
	var UserInfoRoleEdit = require('../ctrl/deptuserinfo.role.edit');
	var UserInfoRoleRemove = require('../ctrl/deptuserinfo.role.remove');
	var UserInfoRoleBan = require('../ctrl/deptuserinfo.role.ban');
	
	var UserInfoRole = Page.extend(function() {
		var _self = this;
		
		this.injecte([
		  new UserInfoRoleAdd('deptuserinfo_role_add'),   
		  new UserInfoRoleEdit('deptuserinfo_role_edit'),
		  new UserInfoRoleRemove('deptuserinfo_role_remove'),
		  new UserInfoRoleBan('deptuserinfo_role_ban')
		]);
		
		// @override
		this.load = function(panel, data) {
			this.data = data;
			
			var form = panel.find('form').form('load', data);
			var table =this.table= panel.find('table');
			table.cdatagrid({
				controller: _self,
				url: Config.ContextPath+'system/userrole/userroles/'+data.userCode
			})
		};
	});
	
	return UserInfoRole;
});