define(function (require) {
  var Config = require('config');
  var Core = require('core/core');
  var Page = require('core/page');


  // 删除数据字典
  var RoleInfoRemove = Page.extend(function () {

    this.renderButton = function (btn, data) {
      return canRemoveRole(data) && 'F' !== data.roleType;
    }

    // @override
    this.submit = function (table, data) {

      if (!canRemoveRole(data)) {
        $.messager.alert('系统内置角色不能删除。');
        return;
      }

      Core.ajax(Config.ContextPath + 'system/roleinfo/' + data.roleCode, {
        type: 'json',
        method: 'delete',
      }).then(function () {
        return require('loaders/cache/loader.system').loadAll()
      }).then(function () {
        table.datagrid('reload');
      });
    };

  });

  return RoleInfoRemove;

  function canRemoveRole(data) {
    return !~['G-SYSADMIN', 'G-anonymous', 'G-DEPLOY'].indexOf(data.roleCode);
  }
});
