define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');

	var UnitInfoUserAdd = require('../ctrl/unitinfo.user.add');
	var UnitInfoUserEdit = require('../ctrl/unitinfo.user.edit');
	var UnitInfoUserRemove = require('../ctrl/unitinfo.user.remove');

	var UnitInfoUser = Page.extend(function() {
		this.injecte([
		  new UnitInfoUserAdd('unitinfo_user_add'),
		  new UnitInfoUserEdit('unitinfo_user_edit'),
		  new UnitInfoUserRemove('unitinfo_user_remove')
		]);

		this.load = function(panel, data) {
			this.data = data;
			this.$autoHeight();

			this.table = panel.find('table').cdatagrid({
				controller: this,
				url: Config.ContextPath+'system/userunit/unitusers/'+data.unitCode+'?s_userCode_isValid=T',
			})
		};

	});

	return UnitInfoUser;
});
