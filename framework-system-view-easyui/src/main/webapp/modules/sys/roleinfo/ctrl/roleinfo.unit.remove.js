define(function (require) {
  var Config = require('config');
  var Core = require('core/core');

  var Page = require('core/page');

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

      Core.ajax(Config.ContextPath + 'system/unitrole/' + this.parent.data.roleCode + '/' + ids.join(','), {
        method: 'DELETE'
      }).then(function () {
        return require('loaders/cache/loader.system').loadAll()
      }).then(function () {
        table.datagrid('reload');
      });
    };
  });

  return UserInfoRoleRemove;
});
