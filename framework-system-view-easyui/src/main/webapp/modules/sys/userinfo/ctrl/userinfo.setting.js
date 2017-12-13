define(function (require) {
  var Config = require('config');
  var Page = require('core/page');

  var UserInfoSettingAdd = require('./userinfo.setting.add');
  var UserInfoSettingEdit = require('./userinfo.setting.edit');
  var UserInfoSettingRemove = require('./userinfo.setting.remove');

  return Page.extend(function () {
    var _self = this;

    this.injecte([
      new UserInfoSettingAdd('userinfo_setting_add'),
      new UserInfoSettingEdit('userinfo_setting_edit'),
      new UserInfoSettingRemove('userinfo_setting_remove')
    ]);

    // @override
    this.load = function (panel, data) {
      this.data = data;

      var table = this.table = panel.find('table');
      table.cdatagrid({
        controller: _self,
        url: Config.ContextPath + 'system/usersetting/list/' + data.userCode
      })
    };
  });
});

