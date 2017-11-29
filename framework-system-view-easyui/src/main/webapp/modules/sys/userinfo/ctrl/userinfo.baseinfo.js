define(function (require) {
  var Config = require('config');
  var Core = require('core/core');
  var Page = require('core/page');


  var UserAll = Page.extend(function () {

    // @override
    this.load = function (panel, data) {
      this.data = data;

      var form = panel.find('form');

      Core.ajax(Config.ContextPath + 'system/userinfo/' + data.userCode, {
        method: 'get'
      }).then(function (data) {
        form.form('disableValidation').form('load', data.userInfo)
          .form('readonly', ['userName', 'loginName', 'regCellPhone', 'regEmail', 'userDesc'])
          .form('focus');
      });
    };

  });

  return UserAll;
});
