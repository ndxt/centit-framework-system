define(function (require) {
  var UnitInfo = require('modules/sys/unitinfo/ctrl/unitinfo');

  return UnitInfo.extend(function () {
    this.queryUnitUrl = 'system/unitinfo/subunits?struct=true';
  });
});
