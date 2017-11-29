define(function (require) {
  var Config = require('config');
  var Core = require('core/core');
  var UserInfoAdd = require('./userinfo.add');

  // 编辑角色信息
  var UserInfoEdit = UserInfoAdd.extend(function () {
    var _self = this;

    // @override
    this.load = function (panel, data) {
      var form = panel.find('form');

      Core.ajax(Config.ContextPath + 'system/userinfo/' + data.userCode, {
        method: 'get'
      }).then(function (data) {
        _self.data = data;

        form
          .form('disableValidation')
          .form('load', data.userUnit)
          .form('load', data.userInfo);
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
        data._method = 'PUT';
        form.form('ajax', {
          url: Config.ContextPath + 'system/userinfo/' + data.userInfo.userCode,
          method: 'put',
          data: data
        }).then(function () {
          _self.parent.selectUser(data);
          closeCallback(true)
        })
      }

      return false;

    };

  });

  return UserInfoEdit;
});
