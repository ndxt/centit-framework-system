define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');
	var Cache = require('core/cache');
	var DeptRoleAdd = require('../ctrl/deptrole.add');
	var DeptRoleEdit = require('../ctrl/deptrole.edit');
	var DeptRoleRemove = require('../ctrl/deptrole.remove');
	var DeptRoleOperate = require('../ctrl/deptrole.operate');
	var DeptRoleUser = require('../ctrl/deptroleinfo.user');
	DeptRoleUser = new DeptRoleUser('deptroleinfo_user');
	
    
	// 角色信息列表
	var DeptRole = Page.extend(function() {
		this.injecte([
	        new DeptRoleAdd('deptrole_add'), 
	        new DeptRoleEdit('deptrole_edit'), 
	        new DeptRoleRemove('deptrole_remove'), 
	        new DeptRoleOperate('deptrole_operate'),
	        DeptRoleUser
	    ]);

		var selectedIndex=null;
		// @override
		this.load = function(panel) {
			var loginuser=Cache.get('loginuser');
			var primaryUnit=loginuser.primaryUnit;
			panel.find('table').cdatagrid({
				// 必须要加此项!!
				controller: this,
				url:Config.ContextPath+"system/roleinfo/unit/"+primaryUnit,
				queryParams: {
					s_isValid: 'T'
				},
				
				rowStyler: function(index, row) {
					if (row.isValid == 'F') {
						return {'class': 'ban'};
					}
				},
				onClickRow: function(index, row) {
					if (selectedIndex == index) return;
					selectedIndex = index;
					var DeptRoleUserPanel = $('#deptrole_layout').layout('panel', 'east');
					DeptRoleUserPanel.data('panel').options.onLoad = function() {
						DeptRoleUser.init(DeptRoleUserPanel, row);
					};
					DeptRoleUserPanel.panel('setTitle', row.roleName + ' 部门角色用户');
					DeptRoleUserPanel.panel('refresh', Config.ViewContextPath + 'modules/sys/deptrole/deptroleinfo-user.html');
				}
			});
		};
	});
	
	return DeptRole;
});