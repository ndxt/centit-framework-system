define(function (require) {
  var Config = require('config');
  var Core = require('core/core');
  var Page = require('core/page');

  var UnitInfoAdd = Page.extend(function () {

    // @override
    this.object = {
      // 默认管理业务
      unitType: 'A',

      // 默认启用
      isValid: 'T'
    };

    // @override
    this.load = function (panel, data) {
      var form = panel.find('form');

      form
        .form('disableValidation')
        .form('load', $.extend({}, this.object, {
          parentUnit: data.unitCode,
          parentUnitName: data.parentUnitName
        }))
        .form('focus');

      if (!data.unitCode) {
        form.form('readonly', 'parentUnit');
      }
    };

    // @override
    this.submit = function (panel, data, closeCallback) {
      var form = panel.find('form');

      form.form('enableValidation');
      var isValid = form.form('validate');

      if (isValid) {
        form.form('ajax', {
          url: Config.ContextPath + 'system/unitinfo',
          method: 'post'
        }).then(closeCallback);
      }

      return false;
    };

    // @override
    this.onClose = function (table, data) {
      if (!data) {
        return;
      }

      this.parent.currentUnit = data.unitCode;

      // 子节点
      if ("0" !== data.parentUnit) {
        this.parent.table.treegrid('reload', data.parentUnit);
      }
      // 顶级节点
      else {
        this.parent.table.treegrid('reload');
      }

      require('loaders/cache/loader.system').loadAll();
    };
  });

  return UnitInfoAdd;
});
