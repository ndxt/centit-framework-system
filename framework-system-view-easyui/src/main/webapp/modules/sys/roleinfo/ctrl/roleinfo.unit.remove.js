define(function (require) {
  var Config = require('config');
  var Core = require('core/core');

  var Page = require('core/page');
  var Mustache = require('plugins/mustache.min');

  // 删除用户角色
  var UserInfoRoleRemove = Page.extend(function () {

    this.renderButton = function () {
      return 'F' !== this.parent.data.roleType;
    };

    // @override
    this.submit = function (table, data) {
      var ids = data.map(function(row) {
        return row.unitCode;
      });
      var _self = this;

      Core.ajax(Config.ContextPath + 'system/unitrole/' + this.parent.data.roleCode + '/' + ids.join(','), {
        method: 'DELETE'
      }).then(function () {
        return require('loaders/cache/loader.system').loadAll()
      }).then(function () {

        var roleInfoUrl = Mustache.render(_self.parent.parent.roleInfoUsersInheritedUrl, _self.parent.parent.data);
        return Core.ajax(roleInfoUrl, {
          method: 'get'
        })
      }).then(function (data) {
        _self.parent.parent.panel.find('#roleinfoUserTable').datagrid('loadData', data);

        table.datagrid('reload');
      });
    };
  });

  return UserInfoRoleRemove;
});
