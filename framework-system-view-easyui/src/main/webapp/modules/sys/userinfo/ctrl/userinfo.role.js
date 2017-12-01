define(function (require) {
  var Config = require('config');
  var $ = require('jquery');
  var Page = require('core/page');
  var Mustache = require('plugins/mustache.min');

  var UserInfoRoleAdd = require('../ctrl/userinfo.role.add');
  var UserInfoRoleEdit = require('../ctrl/userinfo.role.edit');
  var UserInfoRoleRemove = require('../ctrl/userinfo.role.remove');
  var UserInfoRoleBan = require('../ctrl/userinfo.role.ban');

  return Page.extend(function () {
    var _self = this;

    this.injecte([
      new UserInfoRoleAdd('userinfo_role_add'),
      new UserInfoRoleEdit('userinfo_role_edit'),
      new UserInfoRoleRemove('userinfo_role_remove'),
      new UserInfoRoleBan('userinfo_role_ban')
    ]);

    /**
     * 用户角色 （直接获取的角色，可编辑） /userrole/userroles/{usercode}
     * @type {string}
     */
    this.userInfoRoles = Config.ContextPath + 'system/userrole/userroles/{{userCode}}';

    /**
     * 间接获取的角色（不可编辑）/userrole/userrolesinherited/{usercode}
     * @type {string}
     */
    this.userInfoRolesFromUnit = Config.ContextPath + 'system/userrole/userrolesinherited/{{userCode}}';

    // @override
    this.load = function (panel, data) {
      this.data = data;

      // 继承自岗位
      $('table.role-unit').cdatagrid({
        controller: _self,
        url: Mustache.render(this.$findUp('userInfoRolesFromUnit'), data),
        onLoadSuccess: function() {
          _self.$autoHeight();
        },
        onLoadError: function() {
          _self.$autoHeight();
        }
      });

      // 自有角色
      $('table.role').cdatagrid({
        controller: _self,
        url: Mustache.render(this.$findUp('userInfoRoles'), data)
      })
    };
  });
});
