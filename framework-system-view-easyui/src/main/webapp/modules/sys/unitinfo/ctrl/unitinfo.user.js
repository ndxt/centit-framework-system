define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');

	var UnitInfoUserAdd = require('../ctrl/unitinfo.user.add');
	var UnitInfoUserEdit = require('../ctrl/unitinfo.user.edit');
	var UnitInfoUserRemove = require('../ctrl/unitinfo.user.remove');

	var UnitInfoUser = Page.extend(function() {
		var _self = this;

		this.injecte([
		  new UnitInfoUserAdd('unitinfo_user_add'),
		  new UnitInfoUserEdit('unitinfo_user_edit'),
		  new UnitInfoUserRemove('unitinfo_user_remove')
		]);

		// @override
		this.load = function(panel, data) {
			this.data = data;

			var form = panel.find('form').form('load', data);
			var table = panel.find('table');

			Core.ajax(Config.ContextPath+'system/unitinfo/'+data.unitCode, {
				type: 'json',
				method: 'get'
			}).then(function(data) {
				_self.data = data;
				form.form('disableValidation').form('load', data)
					.form('focus');

			});

			table.cdatagrid({
				controller: _self,
				url: Config.ContextPath+'system/userunit/unitusers/'+data.unitCode+'?s_userCode_isValid=T',
				onAfterEdit: function(index,row,changes) {
					// 有改动
					if (!$.isEmptyObject(changes)) {
						// 提交修改
						Core.ajax(Config.ContextPath+'system/userunit/'+row.userUnitId, {
							type: 'json',
							method: 'post',
							data: $.extend({_method: 'PUT'}, row, changes)
						});
					}

				}
			})
		};

	});

	return UnitInfoUser;
});
