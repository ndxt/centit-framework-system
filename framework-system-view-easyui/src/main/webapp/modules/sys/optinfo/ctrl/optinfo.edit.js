define(function (require) {
  var Config = require('config');
  var Core = require('core/core');
  var OptInfoAdd = require('../ctrl/optinfo.add');

  var OptInfoEdit = OptInfoAdd.extend(function () {
    var _self = this;

    // @override
    this.load = function (panel, data) {
      var form = panel.find('form');

      Core.ajax(Config.ContextPath + 'system/optinfo/' + data.id, {
        type: 'json',
        method: 'get'
      }).then(function (data) {
        _self.data = data;

        form.form('disableValidation')
          .form('load', data)
          .form('readonly', 'optId')
          .form('focus');
      });
    };

    // @override
    this.submit = function (panel, data, closeCallback) {
      var form = panel.find('form');

      // 开启校验
      form.form('enableValidation');
      var isValid = form.form('validate');

      if (isValid) {
        this.newObject = form.form('value');

        form.form('ajax', {
          url: Config.ContextPath + 'system/optinfo/' + data.optId,
          method: 'put',
          data: data
        }).then(closeCallback);
      }

      return false;
    };

  });

  return OptInfoEdit;
});
