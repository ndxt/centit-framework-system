define(function (require) {
  var UnitInfo = require('modules/sys/unitinfo/ctrl/unitinfo');

  var UnitInfoAdd = require('modules/sys/unitinfo/ctrl/unitinfo.add');
  var UnitInfoAddTop = require('modules/sys/unitinfo/ctrl/unitinfo.add.top');
  var UnitInfoEdit = require('modules/sys/unitinfo/ctrl/unitinfo.edit');
  var UnitInfoRemove = require('modules/sys/unitinfo/ctrl/unitinfo.remove');
  var UnitInfoAside = require('modules/sys/unitinfo/ctrl/unitinfo.aside');
  var UnitOperate = require('modules/sys/unitinfo/ctrl/unitinfo.operate');

  return UnitInfo.extend(function () {

    var UnitInfoAsideImpl = new UnitInfoAside('unitinfo_aside');

    this.injecte([
      new UnitInfoAddTop('unitinfo_add_top'),
      new UnitInfoAdd('unitinfo_add'),
      new UnitInfoEdit('unitinfo_edit'),
      new UnitInfoRemove('unitinfo_remove'),
      new UnitOperate('unitinfo_operate'),
      UnitInfoAsideImpl
    ]);

    this.queryUrl = 'system/unitinfo/subunits?struct=true';
  });
});
