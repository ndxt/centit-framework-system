define(function (require) {
  var Config = require('config');
  var Core = require('core/core');
  var Page = require('core/page');

  var UserInfoRoleAdd = require('../ctrl/userinfo.role.add');
  var UserInfoRoleEdit = require('../ctrl/userinfo.role.edit');
  var UserInfoRoleRemove = require('../ctrl/userinfo.role.remove');
  var UserInfoRoleBan = require('../ctrl/userinfo.role.ban');

  var UserInfoRole = Page.extend(function () {
    var _self = this;

    this.injecte([
      new UserInfoRoleAdd('userinfo_role_add'),
      new UserInfoRoleEdit('userinfo_role_edit'),
      new UserInfoRoleRemove('userinfo_role_remove'),
      new UserInfoRoleBan('userinfo_role_ban')
    ]);

    // @override
    this.load = function (panel, data) {
      this.data = data;

      var table = this.table = panel.find('table');
      table.cdatagrid({
        controller: _self,
        url: Config.ContextPath + 'system/userrole/userroles/' + data.userCode,
        onLoadSuccess: function() {
          _self.$autoHeight();
        }
      })
    };
  });

  return UserInfoRole;
});
