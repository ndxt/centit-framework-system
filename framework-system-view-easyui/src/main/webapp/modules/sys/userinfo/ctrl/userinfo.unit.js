define(function (require) {
  var Config = require('config');
  var Page = require('core/page');

  var UserInfoUnitAdd = require('../ctrl/userinfo.unit.add');
  var UserInfoUnitEdit = require('../ctrl/userinfo.unit.edit');
  var UserInfoUnitRemove = require('../ctrl/userinfo.unit.remove');

  return Page.extend(function () {
    var _self = this;

    this.injecte([
      new UserInfoUnitAdd('userinfo_unit_add'),
      new UserInfoUnitEdit('userinfo_unit_edit'),
      new UserInfoUnitRemove('userinfo_unit_remove')
    ]);

    // @override
    this.load = function (panel, data) {
      this.data = data;

      var table = this.table = panel.find('table');
      table.cdatagrid({
        controller: _self,
        url: Config.ContextPath + 'system/userunit/userunits/' + data.userCode
      })
    };
  });
});
