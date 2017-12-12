define(function (require) {
  var Config = require('config');
  var Core = require('core/core');
  var Page = require('core/page')


  var UserSettingAdd = require('./userinfo.setting.add');

  // 编辑机构用户
  return UserSettingAdd.extend(function () {
    var _self = this;

    // @override
    this.load = function (panel, data) {

      var form = panel.find('form');
      Core.ajax(Config.ContextPath + 'system/usersetting/' + data.cid.paramCode, {
        method: 'get'
      }).then(function (data) {
        _self.data = data;

        form.form('disableValidation').form('load', data)
          .form('readonly', 'paramCode')
          .form('focus');
      });
    };

    // @override
    this.submit = function (panel, data, closeCallback) {
      var form = panel.find('form');

      form.form('enableValidation');
      var isValid = form.form('validate');

      if (isValid) {
        form.form('ajax', {
          url: Config.ContextPath + 'system/usersetting/' + data.cid.paramCode,
          method: 'put',
          data: data
        }).then(function () {
          return require('loaders/cache/loader.system').loadAll()
        }).then(function () {
          _self.parent.table.datagrid("reload");
          closeCallback();
        });
      }

      return false;
    };
  });

});
