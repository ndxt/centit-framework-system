define(function (require) {
  var Config = require('config');
  var Page = require('core/page');

  var UnitInfoAdd = require('../ctrl/unitinfo.add');
  var UnitInfoAddTop = require('../ctrl/unitinfo.add.top');
  var UnitInfoEdit = require('../ctrl/unitinfo.edit');
  var UnitInfoRemove = require('../ctrl/unitinfo.remove');
  var UnitInfoAside = require('./unitinfo.aside');
  var UnitOperate = require('../ctrl/unitinfo.operate');
  var UnitInfo = Page.extend(function () {

    var UnitInfoAsideImpl = new UnitInfoAside('unitinfo_aside');

    this.injecte([
      new UnitInfoAddTop('unitinfo_add_top'),
      new UnitInfoAdd('unitinfo_add'),
      new UnitInfoEdit('unitinfo_edit'),
      new UnitInfoRemove('unitinfo_remove'),
      new UnitOperate('unitinfo_operate'),
      UnitInfoAsideImpl
    ]);

    this.queryUnitUrl = 'system/unitinfo?struct=true';

    this.beforeSearch = function() {
      this.currentUnit = null;
    };

    // @override
    this.load = function (panel) {
      this.$autoHeight('north', $('.unit-info-main', panel));

      var UnitUserPanel = this.UnitUserPanel = $('#unitinfo_panel', panel).layout('panel', 'east');
      var table = this.table = panel.find('table');

      var vm = this;

      table.ctreegrid({
        controller: this,

        url: this.queryUnitUrl,

        rowStyler: function (row) {
          if (row && row.isValid === 'F') {
            return {
              'class': 'ban'
            };
          }
        },

        onSelect: function (row) {
          if (row.unitCode !== vm.currentUnit) {
            vm.currentUnit = row.unitCode;
            vm.selectUnit(UnitUserPanel, row, UnitInfoAsideImpl);
          }
        },

        onLoadSuccess: function () {
          if (vm.currentUnit) {
            var unitCode = vm.currentUnit;
            // 确保刷新时一定重新加载子页面
            vm.currentUnit = null;
            return $(this).treegrid('select', unitCode);
          }

          var root = $(this).treegrid('getRoot');
          if (root) {
            $(this).treegrid('select', root.unitCode);
          } else {
            vm.clearUnit()
          }
        }
      });
    };

    this.selectUnit = function (panel, row, controller) {
      panel.data('panel').options.onLoad = function () {
        controller.init(panel, row);
      };
      panel.panel('refresh', Config.ViewContextPath + 'modules/sys/unitinfo/unitinfo-aside.html');
    };

    this.clearUnit = function () {
      var panel = this.UnitUserPanel;
      panel.data('panel').options.onLoad = $.noop;
      panel.panel('refresh', Config.ViewContextPath + 'modules/sys/commons/no-data.html');
    };

  });

  return UnitInfo;
});
