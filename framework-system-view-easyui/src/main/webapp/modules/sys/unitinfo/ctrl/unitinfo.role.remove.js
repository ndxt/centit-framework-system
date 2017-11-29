define(function (require) {
  var Config = require('config');
  var Core = require('core/core');

  var Page = require('core/page');

  // 删除机构用户
  var UnitInfoUserRemove = Page.extend(function () {

    // @override
    this.submit = function (table, data) {

      Core.ajax(Config.ContextPath + 'system/unitrole/' + data.roleCode + '/' + this.parent.data.unitCode, {
        method: 'post',
        data: {
          _method: 'DELETE'
        }
      }).then(function () {
        return require('loaders/cache/loader.system').loadAll()
      }).then(function () {
        table.datagrid('reload');
      });
    };

  });

  return UnitInfoUserRemove;
});
