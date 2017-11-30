define(function (require) {
  var Config = require('config');
  var Page = require('core/page');

  var UserInfoAdd = require('../ctrl/userinfo.add');
  var UserInfoEdit = require('../ctrl/userinfo.edit');
  var UserInfoDelete = require('../ctrl/userinfo.delete');

  var DeptUserInfoPowerView = require('../ctrl/userinfo.power.view');
  var UserInfoAside = require('../ctrl/userinfo.aside');
  UserInfoAside = new UserInfoAside('UserInfoAside');

  var UserInfoUnit = require('../ctrl/userinfo.unit');
  var UserInfoRole = require('../ctrl/userinfo.role');

  var UserInfoResetPassword = require('../ctrl/userinfo.resetpassword');

  return Page.extend(function () {

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
      this.currentUserCode = null;
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
          if (vm.currentUserCode !== row.userCode) {
            vm.currentUserCode = row.userCode;
            vm.selectUser(row);
          }
        },

        onLoadSuccess: function () {
          var table = $(this);
          var rows = table.datagrid('getRows');

          if (rows.length) {
            // 重新刷新后，获取之前选中对象在新数据中的index
            var index = 0;
            for (var i = 0; i < rows.length; i++) {
              if (rows[i].userCode === vm.currentUserCode) {
                index = i;
                break;
              }
            }

            // 重置，确保数据能够被刷新
            vm.currentUserCode = null;
            table.datagrid('selectRow', index);
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
      this.currentUserCode = null;
      this.AsidePanel.data('panel').options.onLoad = $.noop;
      this.AsidePanel.panel('clear');
    };
  });
});
