define(function (require) {
  var Config = require('config');
  var Page = require('core/page');
  var Core = require('core/core');

  // var SystemParameterAdd = require('./systemparameter.add');
  // var SystemParameterAdd = require('./systemicon');
  // var SystemParameterRemove = require('./systemparameter.remove');

  return Page.extend(function () {
    var _self = this;

    this.injecte([
      // new SystemParameterAdd('systmeparameter_add'),
      // new SystemParameterAdd('systmeico_upload'),
      // new SystemParameterRemove('systmeparameter_remove')
    ]);

    // @override
    this.load = function (panel, data) {
      this.data = data;

      panel.find('#systemIcon').filebox({
        buttonText: '选择图标',
        buttonAlign: 'left',
        accept: 'icon/*'
      })

     /* var table = this.table = panel.find('table');
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
      });*/
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

