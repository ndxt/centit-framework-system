define(function (require) {
  var Config = require('config');
  var Page = require('core/page');
  var Core = require('core/core');

  var SystemParameterAdd = require('./systemparameter.add');
  var SystemParameterRemove = require('./systemparameter.remove');

  return Page.extend(function () {
    var _self = this;

    this.injecte([
      new SystemParameterAdd('systmeparameter_add'),
      new SystemParameterRemove('systmeparameter_remove')
    ]);

    // @override
    this.load = function (panel, data) {
      this.data = data;

      var table = this.table = panel.find('table');
      Core.ajax(Config.ContextPath + 'system/dictionary/SystemParameter', {
        method: 'get'
      }).then(function (data) {
        table.cdatagrid({
          controller: _self,
          // url: Config.ContextPath + 'system/dictionary/SystemParameter',

          onEndEdit: function (index, row, changes) {
            Core.ajax(Config.ContextPath + 'system/dictionary/update/SystemParameter', {
              data: row,
              method: 'post'
            });
          },

          rowStyler: function (index, row) {
            if (row.paramValue === 'null') {
              return 'background-color:#6293BB;color:#fff;';
            }
          }
        }).datagrid('loadData', data.dataDictionaries);
      });
    };

    this.submit = function (panel, data, closeCallback) {
      var form = panel.find('form');

      // 开启校验
      var isValid = form.form('enableValidation').form('validate');

      if (isValid) {
        form.form('ajax', {
          url: Config.ContextPath + 'system/systemsetting/uploadico',
          method: 'post',
          data: data
        }).then(function () {
       /*   return require('loaders/cache/loader.system').loadAll()
        }).then(function () {
          vm.parent.clearPanel();
          closeCallback(true)*/
        });
      }

      return false;
    };
  });
});

