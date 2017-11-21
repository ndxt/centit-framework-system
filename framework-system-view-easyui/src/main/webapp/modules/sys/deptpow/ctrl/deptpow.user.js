define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');

	var UnitInfoUserAdd = require('modules/sys/deptpow/ctrl/deptpow.user.add');
	var UnitInfoUserEdit = require('modules/sys/deptpow/ctrl/deptpow.user.edit');
	var UnitInfoUserRemove = require('modules/sys/deptpow/ctrl/deptpow.user.remove');

	var DeptPowUser = Page.extend(function() {
		var _self = this;

		this.injecte([
		  new UnitInfoUserAdd('deptpow_user_add'),
		  new UnitInfoUserEdit('deptpow_user_edit'),
		  new UnitInfoUserRemove('deptpow_user_remove'),
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
