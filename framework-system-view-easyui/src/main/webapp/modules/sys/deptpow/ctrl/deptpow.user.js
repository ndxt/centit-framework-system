define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');
	
	var UnitInfoUserAdd = require('modules/sys/unitinfo/ctrl/unitinfo.user.add');
	var UnitInfoUserEdit = require('modules/sys/unitinfo/ctrl/unitinfo.user.edit');
	var UnitInfoUserRemove = require('modules/sys/unitinfo/ctrl/unitinfo.user.remove');
	
	var DeptPowUser = Page.extend(function() {
		var _self = this;
		
		this.injecte([
		  new UnitInfoUserAdd('unitinfo_user_add'),   
		  new UnitInfoUserEdit('unitinfo_user_edit'),
		  new UnitInfoUserRemove('unitinfo_user_remove'),
		]);
		
		// @override
		this.load = function(panel, data) {
			this.data = data;
			
			var form = panel.find('form').form('load', data);
			var table = panel.find('table');
			table.cdatagrid({
				controller: _self,
				url: Config.ContextPath+'system/userunit/unitusers/'+data.unitCode
			})
		};
	});
	
	return DeptPowUser;
});