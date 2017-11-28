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

    // @override
    this.load = function (panel) {
      this.$autoHeight('north', $('.unit-info-main', panel));

      var table = this.table = panel.find('table');
      var currentUnit = null;

      table.ctreegrid({
        controller: this,

        rowStyler: function (row) {
          if (row && row.isValid == 'F') {
            return {
              'class': 'ban'
            };
          }
        },

        onClickRow: function (row) {
          if (row.unitCode === currentUnit) return;

          currentUnit = row.unitCode;
          var panel = $('#unitinfo_panel', panel).layout('panel', 'east');

          panel.data('panel').options.onLoad = function () {
            UnitInfoAsideImpl.init(panel, row);
          };
          panel.panel('setTitle', row.unitName + ' 机构用户');
          panel.panel('refresh', Config.ViewContextPath + 'modules/sys/unitinfo/unitinfo-aside.html');
        }
      });
    };

  });

  return UnitInfo;
});
