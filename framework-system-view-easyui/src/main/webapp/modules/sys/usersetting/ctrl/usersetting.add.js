define(function (require) {
  var Config = require('config');
  var $ = require('jquery');

  var Page = require('core/page');

  // 添加个人设置
  return Page.extend(function () {
    var _self = this;

   /* this.initUnitCombotree = function (input) {
      input
        .attr('target', 'unit')
        .combotree({
          target: 'unit'
        });
    };

    // @override
    this.object = {
      isPrimary: 'T'
    };*/

    // @override
    this.load = function (panel) {

      /*this.$findUp('initUnitCombotree')($('input[name=unitCode]', panel));

      // 获取父窗口的用户信息
      var userInfo = this.parent.data;

      var data = this.data = $.extend({}, this.object, {
        userCode: userInfo.userCode,
        userName: userInfo.userName
      });

      panel.find('form').form('disableValidation')
        .form('load', data)*/

    };

    // @override
    this.submit = function (panel, data, closeCallback) {
      var form = panel.find('form');

      form.form('enableValidation');
      var isValid = form.form('validate');

      if (isValid) {
        form.form('ajax', {
          url: Config.ContextPath + 'system/usersetting',
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
