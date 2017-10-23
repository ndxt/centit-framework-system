define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');
	
	var UserInfoAdd = require('../ctrl/userinfo.add');
	var UserInfoEdit = require('../ctrl/userinfo.edit');
	var UserInfoDelete = require('../ctrl/userinfo.delete');
	
	var UserInfoAll = require('../ctrl/userinfo.all');
	UserInfoAll = new UserInfoAll('userinfo_all');
	
	var UserInfoUnit = require('../ctrl/userinfo.unit');
	var UserInfoRole = require('../ctrl/userinfo.role');
	
	var UserInfoResetPassword = require('../ctrl/userinfo.resetpassword');
    
	// 角色信息列表
	var UserInfo = Page.extend(function() {
		this.injecte([
	        new UserInfoAdd('userinfo_add'), 
	        new UserInfoEdit('userinfo_edit'), 
	        new UserInfoDelete('userinfo_delete'),
	        UserInfoAll,
	        new UserInfoUnit('userinfo_unit'),
	        new UserInfoRole('userinfo_role'),
	        new UserInfoResetPassword('userinfo_resetpassword')
	    ]);
		
		// @override
		this.load = function(panel) {
			var selectIndex=-1;
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
				onCheck: function(index, row) {
					if (index == selectIndex) {
						return
					}else{
						
						selectIndex = index;
						var layout = $('#userinfo_panel').layout('panel', 'east');
						layout.data('panel').options.onLoad = function() {
							UserInfoAll.init(layout, row);
						};
						layout.panel('setTitle', row.userName + ' 角色权限');
						layout.panel('refresh', Config.ViewContextPath + 'modules/sys/userinfo/userinfo-all.html');
		            }
					
				},
				onUncheck: function(index, row) {
					if (index == selectIndex) {
						var layout = $('#userinfo_panel').layout('panel', 'east');
						layout.panel('setTitle',' 角色权限');
						layout.panel('clear');
						selectIndex = -1;
					}
				}
			});
		};
	});
	
	return UserInfo;
});