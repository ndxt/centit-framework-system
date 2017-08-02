setI18NText("");
define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');
	
	var RoleInfoAdd = require('../ctrl/roleinfo.add');
	var RoleInfoEdit = require('../ctrl/roleinfo.edit');
	var RoleInfoRemove = require('../ctrl/roleinfo.remove');
	var RoleInfoOperate = require('../ctrl/roleinfo.operate');
	var RoleUser = require('../ctrl/roleinfo.user');
    
	RoleInfoEdit = new RoleInfoEdit('roleinfo_edit'); 
	RoleInfoOperate = new RoleInfoOperate('roleinfo_operate');
	RoleUser = new RoleUser('roleinfo_user');
	// 角色信息列表
	var RoleInfo = Page.extend(function() {
		//var RoleUserPanel = $('#roleinfo_sub_layout').layout('panel', 'east');
		
		
		this.injecte([
	        new RoleInfoAdd('roleinfo_add'), 
	        new RoleInfoRemove('roleinfo_remove'), 
	        RoleInfoEdit, 
	        RoleInfoOperate,
	        RoleUser
	    ]);
		
		// @override
		this.load = function(panel) {
			
			var selectedIndex;
			
			panel.find('table').cdatagrid({
				// 必须要加此项!!
				controller: this,
					
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
					var RoleUserPanel = $('#roleinfo_layout').layout('panel', 'east');
					
					RoleUserPanel.data('panel').options.onLoad = function() {
						RoleUser.init(RoleUserPanel, row);
					};
					RoleUserPanel.panel('setTitle', row.roleName + ' 角色用户');
					RoleUserPanel.panel('refresh', Config.ViewContextPath + 'modules/sys/roleinfo/roleinfo-user.html');

				}
			});
		};
	});
	
	return RoleInfo;
});