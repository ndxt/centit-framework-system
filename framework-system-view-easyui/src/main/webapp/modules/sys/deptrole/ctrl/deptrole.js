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
    this.queryUnitUrl = Config.ContextPath + 'system/unitinfo/';
    //角色机构
    this.queryRoleUnitUrl = Config.ContextPath + "system/unitrole/rolesubunits/{{roleCode}}";
    //角色用户
    this.roleInfoUsersUrl = Config.ContextPath + 'system/userrole/rolecurrentusers/{{roleCode}}';

    this.validateRoleNameWhenAdd = Config.ContextPath + 'system/roleinfo/isunitroleunique/' + loginUser.userInfo.primaryUnit + '/{{roleName}}';

    this.validateRoleNameWhenEdit = Config.ContextPath + 'system/roleinfo/isunitroleunique/' + loginUser.userInfo.primaryUnit + '/{{roleName}}/{{roleCode}}';

    // 新增部门角色的roleType必须为 D
    this.doDeptRoleAdd = function(ctrl) {
      ctrl.panel.find('.roleType').remove();
      ctrl.object.roleType = 'D';
    };

    // 修改角色机构下拉框初始化
    this.initUnitCombotree = function (input) {
      input.combotree({
        url: 'system/unitinfo/validsubunits'
      });
    };
    //修改角色用户下拉框初始化
    this.initUserCombobox = function (input) {
      input.combobox({
        url: 'system/unitinfo/currentusers/T',
        valueField: 'userCode',
        textField: 'userName',
        loadFilter: function(data) {
          return data.objList ? data.objList : data;
        }
      });
    };

	});

});
