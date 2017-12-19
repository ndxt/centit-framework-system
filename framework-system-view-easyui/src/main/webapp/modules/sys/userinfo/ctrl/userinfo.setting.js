define(function (require) {
  var Config = require('config');
  var Page = require('core/page');
  var Core = require('core/core');

  // var UserInfoSettingAdd = require('./userinfo.setting.add');
  // var UserInfoSettingEdit = require('./userinfo.setting.edit');
  var UserInfoSettingRemove = require('./userinfo.setting.remove');

  return Page.extend(function () {
    var _self = this;

    this.injecte([
      // new UserInfoSettingAdd('userinfo_setting_add'),
      // new UserInfoSettingEdit('userinfo_setting_edit'),
      new UserInfoSettingRemove('userinfo_setting_remove')
    ]);

    // @override
    this.load = function (panel, data) {
      this.data = data;

      var table = this.table = panel.find('table');
      table.cdatagrid({
        controller: _self,
        url: Config.ContextPath + 'system/usersetting/list/' + data.userCode,

        onEndEdit: function(index, row, changes){
          Core.ajax(Config.ContextPath + 'system/usersetting/', {
            data: row,
            method: 'post'
          }).then(function(){
            table.cdatagrid('reload');
          });
        },

        rowStyler: function (index, row) {
          if (row.defaultValue === true) {
            return {'class': 'fix'};
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

