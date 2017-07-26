define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');
	
	var RoleinfoUserAdd = require('../ctrl/roleinfo.user.add');
	var RoleinfoUserEdit = require('../ctrl/roleinfo.user.edit');
	var RoleinfoUserRemove = require('../ctrl/roleinfo.user.remove');
	
	var RoleInfoUser = Page.extend(function() {
		var _self = this;
		
		this.injecte([
		  new RoleinfoUserAdd('roleinfo_user_add'),   
		  new RoleinfoUserEdit('roleinfo_user_edit'),
		  new RoleinfoUserRemove('roleinfo_user_remove')
		]);
		
		// @override
		this.load = function(panel, data) {
			this.data = data;
			var form = panel.find('form').form('load', data);
			var table = panel.find('table');
			table.cdatagrid({
				controller: _self,
				url: Config.ContextPath+'system/userrole/roleusers/'+data.roleCode+'?s_userCode_isValid=T'
			})
		};
	});
	
	return RoleInfoUser;
});