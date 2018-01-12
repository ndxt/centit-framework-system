define(function (require) {
  var UnitInfo = require('modules/sys/unitinfo/ctrl/unitinfo');
  var Cache = require('core/cache');

  return UnitInfo.extend(function () {
    this.queryUnitUrl = 'system/unitinfo/subunits?struct=true';

    var unitCode = Cache.get('loginuser').currentUnit;
    this.queryUnitUserUrl = 'system/unitinfo/currentusers/T';
  });
});
