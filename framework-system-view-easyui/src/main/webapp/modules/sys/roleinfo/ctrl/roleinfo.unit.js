define(function (require) {
  var Config = require('config');
  var Page = require('core/page');
  var Mustache = require('plugins/mustache.min');

  var RoleinfoUnitAdd = require('../ctrl/roleinfo.unit.add');
  var RoleinfoUnitRemove = require('../ctrl/roleinfo.unit.remove');

  var RoleInfoUser = Page.extend(function () {
    var _self = this;

    this.injecte([
      new RoleinfoUnitAdd('roleinfo_unit_add'),
      new RoleinfoUnitRemove('roleinfo_unit_remove')
    ]);

    // @override
    this.load = function (panel, data) {
      this.data = data;
      var table = panel.find('table');
      table.cdatagrid({
        controller: _self,
        url: Mustache.render(this.$findUp('queryRoleUnitUrl'), data)
      })
    };
  });

  return RoleInfoUser;
});
