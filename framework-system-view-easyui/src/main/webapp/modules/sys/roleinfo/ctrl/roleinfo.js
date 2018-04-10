define(function (require) {
  var Config = require('config');
  var Core = require('core/core');
  var Page = require('core/page');

  var RoleInfoAdd = require('../ctrl/roleinfo.add');
  var RoleInfoEdit = require('../ctrl/roleinfo.edit');
  var RoleInfoRemove = require('../ctrl/roleinfo.remove');
  var RoleInfoOperate = require('../ctrl/roleinfo.operate');

  var RoleAside = require('./roleinfo.aside');

  RoleInfoEdit = new RoleInfoEdit('roleinfo_edit');
  RoleInfoOperate = new RoleInfoOperate('roleinfo_operate');
  RoleAside = new RoleAside('roleinfo_aside');


  // 角色信息列表
  var RoleInfo = Page.extend(function () {

    this.injecte([
      new RoleInfoAdd('roleinfo_add'),
      new RoleInfoRemove('roleinfo_remove'),
      RoleInfoEdit,
      RoleInfoOperate,
      RoleAside
    ]);

    this.queryRoleUrl = 'system/roleinfo/all';

    this.allPowerUrl = 'system/optinfo/poweropts';

    //角色机构
    this.queryRoleUnitUrl = Config.ContextPath + "system/unitrole/roleunits/{{roleCode}}";

    // @override
    this.load = function (panel) {
      var selectIndex = -1;
      var vm = this;
      var RoleUserPanel = $('#roleinfo_layout', panel).layout('panel', 'east');

      this.$autoHeight('north', $('.role-info-main', panel));

      this.table = panel.find('table').cdatagrid({
        controller: this,

        url: this.$findUp('queryRoleUrl'),

        queryParams: {
          s_isValid: 'T'
        },

        rowStyler: function (index, row) {
          if (row.isValid === 'F') {
            return {'class': 'ban'};
          }
          if (row.roleType === 'F') {
            return {'class': 'fix'}
          }
        },

        columns: {
          roleType: {
            formatter: this.roleTypeFormatter
          }
        },

        onSelect: function (index, row) {
          if (index !== selectIndex || selectIndex === 0) {
            selectIndex = index;
            vm.selectRole(RoleUserPanel, row, RoleAside);
          }
        },

        onLoadSuccess: function () {
          var index = $(this).datagrid('getSelectedRowIndex');

          var rows = $(this).datagrid('getRows');
          if (rows.length) {
            $(this).datagrid('selectRow', index === -1 ? 0 : index)
          } else {
            vm.clearRole(RoleUserPanel)
          }
        }
      })
    };

    this.selectRole = function (panel, row, controller) {
      panel.data('panel').options.onLoad = function () {
        controller.init(panel, row);
      };
      panel.panel('refresh', Config.ViewContextPath + 'modules/sys/roleinfo/roleinfo-aside.html');
    };

    this.clearRole = function (panel) {
      panel.data('panel').options.onLoad = $.noop;
      panel.panel('refresh', Config.ViewContextPath + 'modules/sys/commons/no-data.html');
    };

    this.roleTypeFormatter = function (value) {
      var types = {
        G: '一般角色',
        P: '公共角色',
        F: '内置角色',
        D: '部门角色'
      };

      return types[value] ? types[value] : value;
    };
  });

  return RoleInfo;
});
