define(function (require) {
  var Config = require('config');
  var Core = require('core/core');

  var Page = require('core/page');

  // 编辑机构用户
  var UnitInfoUserEdit = Page.extend(function () {
    var _self = this;

    // @override
    this.load = function (panel, data) {

      // 保存原始信息
      this.oldData = data;
      panel.find('input[name=userCode]').combobox({
        url: this.$findUp('queryUnitUserUrl'),
        textField: 'userName',
        valueField: 'userCode'
      });

      var form = panel.find('form');
      Core.ajax(Config.ContextPath + 'system/userunit/' + data.userUnitId, {
        data: {
          userStation: data.userStation,
          userRank: data.userRank
        },
        method: 'get'
      }).then(function (data) {
        _self.data = data;

        form.form('disableValidation').form('load', data)
          .form('readonly', 'userCode')
          .form('focus');
        panel.find('input[name=userCode]').combobox('setValue', data.userCode);

        if (data.isPrimary === 'T') {
          $("input[name='isPrimary']", panel).attr("disabled", true);
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
          url: Config.ContextPath + 'system/userunit/' + data.userUnitId,
          method: 'put',
          data: data
        }).then(function () {
          closeCallback(true)
        });
      }

      return false;
    };

    // @override
    this.onClose = function (table, submitted) {
      if (submitted) {
        table.datagrid('reload');
      }
    };
  });

  return UnitInfoUserEdit;
});
