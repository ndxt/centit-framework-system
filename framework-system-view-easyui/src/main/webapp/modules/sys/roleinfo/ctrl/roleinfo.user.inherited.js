define(function (require) {
  var Config = require('config');
  var Mustache = require('plugins/mustache.min');
  var Page = require('core/page');

  var RoleinfoUserAdd = require('../ctrl/roleinfo.user.add');
  var RoleinfoUserEdit = require('../ctrl/roleinfo.user.edit');
  var RoleinfoUserRemove = require('../ctrl/roleinfo.user.remove');

  var RoleInfoUser = Page.extend(function () {
    var _self = this;

    this.injecte([
      new RoleinfoUserAdd('roleinfo_user_add'),
      new RoleinfoUserEdit('roleinfo_user_edit'),
      new RoleinfoUserRemove('roleinfo_user_remove')
    ]);

    /**
     * 间接获取角色的用户（不可编辑）/userrole/roleusersinherited/{rolecode}
     * @type {string}
     */


    // @override
    this.load = function (panel, data) {
      this.$autoHeight();
      this.data = data;
      panel.find('form').form('load', data);
      var table = panel.find('table');
      table.cdatagrid({
        controller: _self,
        url: Mustache.render(this.$findUp('roleInfoUsersInheritedUrl'), data),
      })
    };
  });

  return RoleInfoUser;
});
