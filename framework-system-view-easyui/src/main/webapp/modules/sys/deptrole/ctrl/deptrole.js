define(function(require) {
  var Config = require('config');
  var RoleController = require('modules/sys/roleinfo/ctrl/roleinfo');
  var Cache = require('core/cache');

	return RoleController.extend(function() {

	  // 部门角色只查询当前部门下的角色
    var loginUser = Cache.get('loginuser');
    this.queryRoleUrl = Config.ContextPath + "system/roleinfo/currentunit";
    //部门所有权限
    this.allPowerUrl = Config.ContextPath + 'system/optinfo/unitpoweropts/'+loginUser.userInfo.primaryUnit;
    //部门机构
    this.queryUnitUrl = Config.ContextPath + 'system/unitinfo/'

    this.validateRoleNameWhenAdd = Config.ContextPath + 'system/roleinfo/isunitroleunique/' + loginUser.userInfo.primaryUnit + '/{{roleName}}';

    this.validateRoleNameWhenEdit = Config.ContextPath + 'system/roleinfo/isunitroleunique/' + loginUser.userInfo.primaryUnit + '/{{roleName}}/{{roleCode}}';

    // 新增部门角色的roleType必须为 D
    this.doDeptRoleAdd = function(ctrl) {
      ctrl.panel.find('.roleType').remove();
      ctrl.object.roleType = 'D';
    };

    // 修改用户机构下拉框初始化
    this.initUnitCombotree = function (input) {
      input.combotree({
        url: 'system/unitinfo/subunits'
      });
    };

	});

});
