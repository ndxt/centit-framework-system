define(function(require) {
	var Config = require('config');
	var Page = require('core/page');

	var UserInfoUnitAdd = require('../ctrl/userinfo.unit.add');
	var UserInfoUnitEdit = require('../ctrl/userinfo.unit.edit');
	var UserInfoUnitRemove = require('../ctrl/userinfo.unit.remove');
	var RoleInfoOperate = require('modules/sys/roleinfo/ctrl/roleinfo.operate');

	var UserAll = Page.extend(function() {
		var _self = this;

		this.injecte([
		  new UserInfoUnitAdd('userinfo_unit_add'),
		  new UserInfoUnitEdit('userinfo_unit_edit'),
		  new RoleInfoOperate('roleinfo_operate'),
		  new UserInfoUnitRemove('userinfo_unit_remove')
		]);

		// @override
		this.load = function(panel, data) {
			this.data = data;

			var tableUnit = panel.find('table.unit');
			tableUnit.cdatagrid({
				controller: _self,
				url: Config.ContextPath+'system/userunit/userunits/'+data.userCode
			});
		};

	});

	return UserAll;
});
