define(function (require) {
  var Config = require('config');
  var Page = require('core/page');
  var Core = require('core/core');
  var $ = require('jquery');

  var UserInfoSettingRemove = require('./userinfo.setting.remove');

  return Page.extend(function () {
    var _self = this;

    this.injecte([
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
          if(row.paramValue === '' || row.paramValue === null){
            $.messager.confirm("提示", "设置为空将删除记录！", function(result){
              if(result){
                if(row.defaultValue){
                  $.messager.alert('警告', '默认设置不能删除', 'warning',function(){
                    table.datagrid('reload');
                  });
                  return;
                }
                Core.ajax(Config.ContextPath+'system/usersetting/'+row.userCode+'/'+row.paramCode, {
                  method: 'delete'
                }).then(function(){
                  return require('loaders/cache/loader.system').loadAll()
                }).then(function() {
                  table.datagrid('reload');
                });
              }else{
                table.datagrid('reload');
              }
            });
          }else if(changes.paramValue) {

            Core.ajax(Config.ContextPath + 'system/usersetting/', {
              data: row,
              method: 'post'
            }).then(function () {
              table.cdatagrid('reload');
            });
          }
        },

        rowStyler: function (index, row) {
          if (row.defaultValue) {
            return 'background-color:#909090;color:#fff;';
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

