define(function (require) {
  var Config = require('config');
  var Cache = require('core/cache');
  var UserInfo = require('modules/sys/userinfo/ctrl/userinfo');

  // 角色信息列表
  return UserInfo.extend(function () {
    var loginUser = Cache.get('loginuser');
    this.queryUrl = Config.ContextPath + "system/unitinfo/" + loginUser.primaryUnit + "/users";

    // 修改用户角色链接
    this.$find('userinfo_role_add', function (controller) {
      controller.queryRoleUrl = 'system/roleinfo/listRoles/D'
    });

    // 修改用户机构下拉框初始化
    this.$find('userinfo_unit_add', function (controller) {
      controller.initUnitCombotree = function (input) {
        input.combotree({
          url: 'system/unitinfo/subunits'
        });
      };
    });
  });

});
