define(function (require) {
  var Page = require('core/page');
  var RoleUser = require('./roleinfo.user');
  var RoleUserInherited = require('./roleinfo.user.inherited');
  var RoleUnit = require('./roleinfo.unit');
  var $ = require('jquery');

  var RoleInfoAside = Page.extend(function () {
    var tabSelectedIndex = 0;

    var controllers = [
      new RoleUser('role_user'),
      new RoleUserInherited('RoleUserInherited'),
      new RoleUnit('role_unit'),
    ];

    this.injecte(controllers);

    this.load = function (panel, data) {

      $('.role-info-aside', panel).tabs({
        selected: tabSelectedIndex,
        onLoad: function(tab) {
          tabSelectedIndex = $(this).tabs('getTabIndex', tab);
          controllers[tabSelectedIndex].init(tab, data);
        },
        onSelect: function(title, index) {
          tabSelectedIndex = index;
        }
      })
    };

  });

  return RoleInfoAside;
});
