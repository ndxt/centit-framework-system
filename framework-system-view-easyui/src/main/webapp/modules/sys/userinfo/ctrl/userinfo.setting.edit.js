define(function (require) {
  var Config = require('config');
  var Core = require('core/core');
  var Page = require('core/page')


  var UserSettingAdd = require('./userinfo.setting.add');

  // 编辑机构用户
  return UserSettingAdd.extend(function () {
    var _self = this;

    // @override
    this.load = function (panel, data) {

      // 保存原始信息
      this.oldData = data;
      this.$findUp('initUnitCombotree')($('input[name=unitCode]', panel));

      var form = panel.find('form');
      Core.ajax(Config.ContextPath + 'system/usersetting/' + data.paraCode, {
        method: 'get'
      }).then(function (data) {
        _self.data = data;

        form.form('disableValidation').form('load', data)
          .form('readonly', 'unitCode')
          .form('focus');
        if (data.isPrimary === 'T') {
          $("input[name='isPrimary']").attr("disabled", true);
        }
      });
    };

    // @override
    this.submit = function (panel, data, closeCallback) {
      var form = panel.find('form');

      form.form('enableValidation');
      var isValid = form.form('validate');

      if (isValid) {
        // 原始职位和岗位
        data.oldUserStation = this.oldData.userStation;
        data.oldUserRank = this.oldData.userRank;
        form.form('ajax', {
          url: Config.ContextPath + 'system/usersetting/' + data.userUnitId,
          method: 'put',
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
