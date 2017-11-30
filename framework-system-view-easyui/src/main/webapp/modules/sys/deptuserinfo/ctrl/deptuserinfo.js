define(function (require) {
  var Config = require('config');
  var Cache = require('core/cache');
  var UserInfo = require('modules/sys/userinfo/ctrl/userinfo');

  // 角色信息列表
  return UserInfo.extend(function () {
    var loginUser = Cache.get('loginuser');
    this.queryUserUrl = Config.ContextPath + "system/unitinfo/" + loginUser.primaryUnit + "/users";

    // 修改用户角色链接
    this.queryRoleUrl = 'system/roleinfo/listRoles/D';

    // 修改用户机构下拉框初始化
    this.initUnitCombotree = function (input) {
      input.combotree({
        url: 'system/unitinfo/subunits'
      });
    };
  });

});
