define(function (require) {
  var UnitInfo = require('modules/sys/unitinfo/ctrl/unitinfo');
  var Cache = require('core/cache');
  var Config = require('config');

  return UnitInfo.extend(function () {
    this.queryUnitUrl = 'system/unitinfo/subunits?struct=true';

    var unitCode = Cache.get('loginuser').currentUnit;
    this.queryUnitUserUrl = 'system/unitinfo/currentusers/T';
    //机构角色 下拉框
    this.queryRoleUrl = Config.ContextPath + "system/unitinfo/validroles";
    //机构角色 表格
    this.queryCurrentUnitRoleUrl = Config.ContextPath + 'system/unitrole/currentunitroles/{{unitCode}}';
  });
});
