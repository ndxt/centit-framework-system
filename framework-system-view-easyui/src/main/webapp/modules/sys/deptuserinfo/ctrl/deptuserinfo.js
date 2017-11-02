define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');
	var Cache = require('core/cache');
	var DeptUserInfoAdd = require('../ctrl/deptuserinfo.add');
	var DeptUserInfoEdit = require('../ctrl/deptuserinfo.edit');
	var UserInfoDelete = require('modules/sys/userinfo/ctrl/userinfo.delete');
  var DeptUserInfoPowerView = require('../ctrl/deptuserinfo.power.view');

	var DeptUserInfoAll = require('../ctrl/deptuserinfo.all');
	DeptUserInfoAll = new DeptUserInfoAll('deptuserinfo_all');
	var UserInfoUnit = require('modules/sys/deptuserinfo/ctrl/deptuserinfo.unit');
	var UserInfoRole = require('modules/sys/deptuserinfo/ctrl/deptuserinfo.role');

	var UserInfoResetPassword = require('modules/sys/userinfo/ctrl/userinfo.resetpassword');

	// 角色信息列表
	var UserInfo = Page.extend(function() {
		this.injecte([
	        new DeptUserInfoAdd('deptuserinfo_add'),
	        new DeptUserInfoEdit('deptuserinfo_edit'),
	        new UserInfoDelete('deptuserinfo_delete'),
          new DeptUserInfoPowerView('deptuserinfo_power_view'),
	        DeptUserInfoAll,
	        new UserInfoUnit('deptuserinfo_unit'),
	        new UserInfoRole('deptuserinfo_role'),
	        new UserInfoResetPassword('deptuserinfo_resetpassword')
	    ]);

		// @override
		this.load = function(panel) {
			var loginuser=Cache.get('loginuser');
			var selectIndex=-1;
			this.object={loginuser:loginuser};
			this.table=panel.find('table');
			panel.find('table').cdatagrid({
				// 必须要加此项!!
				controller: this,
				url: Config.ContextPath+"system/unitinfo/"+loginuser.primaryUnit+"/users",
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

					}else{

						selectIndex = index;
						var layout = $('#deptuserinfo_panel').layout('panel', 'east');
						layout.data('panel').options.onLoad = function() {
							DeptUserInfoAll.init(layout, row);
						};
//						layout.panel('setTitle', row.userName + ' 角色权限');
						layout.panel('refresh', Config.ViewContextPath + 'modules/sys/deptuserinfo/deptuserinfo-all.html');
		            }

				},
				onUncheck: function(index, row) {
					if (index == selectIndex) {
						var layout = $('#deptuserinfo_panel').layout('panel', 'east');
						layout.panel('setTitle','机构与权限');
						layout.panel('clear');
						selectIndex = -1;
					}
				}
			});
		};
	});

	return UserInfo;
});
