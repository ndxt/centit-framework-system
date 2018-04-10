define(function (require) {
  var Config = require('config');
  var Page = require('core/page');
  var Mustache = require('plugins/mustache.min');

  var UnitInfoRoleAdd = require('../ctrl/unitinfo.role.add');
  var UnitInfoRoleEdit = require('../ctrl/unitinfo.role.edit');
  var UnitInfoRoleRemove = require('../ctrl/unitinfo.role.remove');

  var UnitInfoUser = Page.extend(function () {
    var _self = this;

    this.injecte([
      new UnitInfoRoleAdd('unitinfo_role_add'),
      new UnitInfoRoleEdit('unitinfo_role_edit'),
      new UnitInfoRoleRemove('unitinfo_role_remove')
    ]);

    // @override
    this.load = function (panel, data) {
      panel.find('table').cdatagrid({
        controller: _self,
        url: Mustache.render(this.$findUp('queryCurrentUnitRoleUrl'), data)
      })
    };

  });

  return UnitInfoUser;
});
