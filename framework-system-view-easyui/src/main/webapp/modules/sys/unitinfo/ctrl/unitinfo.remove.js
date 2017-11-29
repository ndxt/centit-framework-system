define(function (require) {
  var Config = require('config');
  var Core = require('core/core');
  var Page = require('core/page');

  var UnitInfoRemove = Page.extend(function () {
    var vm = this;

    this.submit = function (table, data) {
      Core.ajax(Config.ContextPath + 'system/unitinfo/' + data.unitCode, {
        type: 'json',
        method: 'post',
        data: {
          _method: 'delete'
        }
      }).then(function () {
        return require('loaders/cache/loader.system').loadAll()
      }).then(function () {
        table.treegrid('remove', data.unitCode);
        vm.parent.clearUnit();
      });
    };

    this.renderButton = function (target, data) {
      if (data.state === 'closed')
        return false;
      else {
        return data.children.length === 0;
      }
    };

  });

  return UnitInfoRemove;
});
