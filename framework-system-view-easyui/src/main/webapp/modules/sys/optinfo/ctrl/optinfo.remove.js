define(function (require) {
  var Config = require('config');
  var Core = require('core/core');
  var Page = require('core/page');

  return Page.extend(function () {

    // @override
    this.submit = function (table, data) {
      Core.ajax(Config.ContextPath + 'system/optinfo/' + data.id, {
        type: 'json',
        method: 'post',
        data: {
          _method: 'delete'
        }
      }).then(function () {
        table.treegrid('remove', data.id);
      });
    };

    this.renderButton = function (target, data) {
      if (data.state === 'closed')
        return false;
      else {
        return data.children && data.children.length === 0;
      }
    };
  });
});
