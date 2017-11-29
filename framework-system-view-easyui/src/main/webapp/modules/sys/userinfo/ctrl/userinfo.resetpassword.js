define(function (require) {
  var Config = require('config');
  var Core = require('core/core');
  var Page = require('core/page');
  var $ = require('jquery');

  // 编辑角色信息
  var UserInfoResetPassword = Page.extend(function () {

    // @override
    this.submit = function (table, data) {
      var userCodes = data.map(
        function (d) {
          return d.userCode;
        });
      Core.ajax(Config.ContextPath + 'system/userinfo/reset', {
        method: 'post',
        data: {
          _method: 'PUT',
          userCodes: userCodes
        }
      }).then(function () {
        $.messager.alert('提示', '重置密码成功！');
      });
    };

  });

  return UserInfoResetPassword;
});
