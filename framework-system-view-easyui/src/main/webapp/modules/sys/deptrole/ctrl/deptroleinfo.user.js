define(function(require) {
	var Config = require('config');
//	var Core = require('core/core');
	var Page = require('core/page');

	var DeptRoleinfoUserAdd = require('../ctrl/deptroleinfo.user.add');
//	var DeptRoleinfoUserEdit = require('../ctrl/deptroleinfo.user.edit');
	var DeptRoleinfoUserRemove = require('../ctrl/deptroleinfo.user.remove');

	var DeptRoleInfoUser = Page.extend(function() {
		var _self = this;

		this.injecte([
		  new DeptRoleinfoUserAdd('deptroleinfo_user_add'),
//		  new DeptRoleinfoUserEdit('deptroleinfo_user_edit'),
		  new DeptRoleinfoUserRemove('deptroleinfo_user_remove')
		]);

		// @override
		this.load = function(panel, data) {
			this.data = data;
			var form = panel.find('form').form('load', data);
			var table = panel.find('table');
			table.cdatagrid({
				controller: _self,
				url: Config.ContextPath+'system/userrole/roleusers/'+data.roleCode+'?s_userCode_isValid=T',
        columns: {
          changeDesc: {
            formatter: function (value, row, index) {
              return '<a title="' + value + '">' + value + '</a>';
            }
          }
        }
			})

    };
	});

	return DeptRoleInfoUser;
});
