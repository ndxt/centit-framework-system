define(function (require) {
  var Config = require('config');
  var Core = require('core/core');
  var Page = require('core/page');
  var RoleUser = require('../ctrl/roleinfo.user');
  var $ = require('jquery');

  var RoleInfoAside = Page.extend(function () {
    var tabSelectedIndex = 0;

    var controllers = [
      new RoleUser('role_user')
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
