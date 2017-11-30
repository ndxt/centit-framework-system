define(function (require) {
  var Config = require('config');
  var Core = require('core/core');
  var Page = require('core/page');

  var UserInfoAdd = require('../ctrl/userinfo.add');
  var UserInfoEdit = require('../ctrl/userinfo.edit');
  var UserInfoDelete = require('../ctrl/userinfo.delete');

  var DeptUserInfoPowerView = require('../ctrl/userinfo.power.view');//新增查看权限按钮
  var UserInfoAside = require('../ctrl/userinfo.aside');
  UserInfoAside = new UserInfoAside('UserInfoAside');

  var UserInfoUnit = require('../ctrl/userinfo.unit');
  var UserInfoRole = require('../ctrl/userinfo.role');

  var UserInfoResetPassword = require('../ctrl/userinfo.resetpassword');

  // 角色信息列表
  var UserInfo = Page.extend(function () {

    this.selectIndex = -1;

    this.injecte([
      new UserInfoAdd('userinfo_add'),
      new UserInfoEdit('userinfo_edit'),
      new UserInfoDelete('userinfo_delete'),
      UserInfoAside,
      new UserInfoUnit('userinfo_unit'),
      new UserInfoRole('userinfo_role'),
      new UserInfoResetPassword('userinfo_resetpassword'),
      new DeptUserInfoPowerView('userinfo_power_view')
    ]);

    this.beforeSearch = function () {
      this.selectIndex = -1;
    };

    this.queryUserUrl = 'system/userinfo?_search=false&field=userCode&field=loginName&field=userOrder&field=userName&field=isValid&field=primaryUnit';

    // @override
    this.load = function (panel) {
      var vm = this;

      this.$autoHeight('north', $('#user-info-main', panel));
      this.AsidePanel = $('#userinfo_panel', panel).layout('panel', 'east');
      this.AsideController = UserInfoAside;

      panel.find('table').cdatagrid({
        url: this.queryUserUrl,

        controller: this,

        queryParams: {
          s_isValid: 'T'
        },

        rowStyler: function (index, row) {
          if (row.isValid === 'F') {
            return {'class': 'ban'};
          }
        },

        onSelect: function (index, row) {
          if (index !== vm.selectIndex) {
            vm.selectIndex = index;
            vm.selectUser(row);
          }
        },

        onLoadSuccess: function () {
          var table = $(this);
          var rows = table.datagrid('getRows');
          if (rows.length) {
            table.datagrid('selectRow', vm.selectIndex === -1 ? 0 : vm.selectIndex);
          } else {
            vm.clearPanel();
          }
        }
      });
    };

    this.selectUser = function (user) {
      var vm = this;

      if (!user || !user.userCode) {
        return this.clearPanel();
      }

      this.AsidePanel.data('panel').options.onLoad = function () {
        vm.AsideController.init($(this), user);
      };
      this.AsidePanel.panel('refresh', Config.ViewContextPath + 'modules/sys/userinfo/userinfo-aside.html');
    };

    this.clearPanel = function () {
      var vm = this;

      vm.selectIndex = -1;
      this.AsidePanel.data('panel').options.onLoad = $.noop;
      this.AsidePanel.panel('clear');
    };
  });

  return UserInfo;
});
