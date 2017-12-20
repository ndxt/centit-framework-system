define(function (require) {
  var Config = require('config');
  var Page = require('core/page');
  var Core = require('core/core');

  var UserDefaultSettingRemove = require('./userdefaultsetting.remove');

  return Page.extend(function () {
    var _self = this;

    this.injecte([
      new UserDefaultSettingRemove('userdefaultsetting_remove')
    ]);

    // @override
    this.load = function (panel, data) {
      this.data = data;

      var table = this.table = panel.find('table');
      table.cdatagrid({
        controller: _self,
        url: Config.ContextPath + 'system/usersetting/listdefault/',

        onEndEdit: function(index, row, changes){
          Core.ajax(Config.ContextPath + 'system/usersetting/updatedefault', {
            data: row,
            method: 'post'
          });
        },

        rowStyler: function (index, row) {
          if (row.paramValue === 'null') {
            return 'background-color:#6293BB;color:#fff;';
          }
        }
      });
    };

    this.submit = function (panel, data, closeCallback) {
      var form = panel.find('form');
      var table = this.table;

      if (form.form('validate') && table.cdatagrid('endEdit')) {
        var formData = form.form('value');
        var optDefs = table.datagrid('getData').rows;

        $.extend(data, formData);
        data.optDefs = optDefs;
        data._method = 'PUT';

        Core.ajax(Config.ContextPath + 'system/usersetting/' + data.paramCode, {
          data: data,
          method: 'post'
        }).then(closeCallback);
      }
    };
  });
});

