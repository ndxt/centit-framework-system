define(function (require) {
  var Config = require('config');
  var Page = require('core/page');

  var UserInfoUnitAdd = require('./usersetting.add');
  var UserInfoUnitEdit = require('./usersetting.edit');
  var UserInfoUnitRemove = require('./usersetting.remove');

  return Page.extend(function () {
    var _self = this;

    this.injecte([
      new UserInfoUnitAdd('usersetting_add'),
      new UserInfoUnitEdit('usersetting_edit'),
      new UserInfoUnitRemove('usersetting_remove')
    ]);

    // @override
    this.load = function (panel, data) {
      this.data = data;

      var table = this.table = panel.find('table');
      table.cdatagrid({
        controller: _self,
        url: Config.ContextPath + 'system/usersetting/' + data.userCode
      })
    };
  });
});

