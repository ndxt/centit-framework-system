define(function (require) {
  var Config = require('config');
  var Core = require('core/core');
  var Page = require('core/page');

  // 删除数据字典
  var UserInfoDelete = Page.extend(function () {
    var vm = this;

    // @override
    this.submit = function (table, data) {
      var userCodes = [];
      for (var i = 0; i < data.length; i++) {
        userCodes.push(data[i].userCode);
      }

      Core.ajax(Config.ContextPath + 'system/userinfo/' + userCodes, {
        type: 'json',
        method: 'post',
        data: {
          _method: 'delete'
        }
      }).then(function () {
        return require('loaders/cache/loader.system').loadAll()
      }).then(function () {
        vm.parent.clearPanel();
        table.datagrid('reload');
      });
    };
  });

  return UserInfoDelete;
});
