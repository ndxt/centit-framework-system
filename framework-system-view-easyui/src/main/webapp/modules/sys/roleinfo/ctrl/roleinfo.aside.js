define(function (require) {
  var Config = require('config');
  var Core = require('core/core');
  var Page = require('core/page');
  var RoleUser = require('../ctrl/roleinfo.user');
  var $ = require('jquery');

  var RoleInfoAside = Page.extend(function () {
    var vm = this;

    var controllers = [
      new RoleUser('role_user')
    ];

    this.injecte(controllers);

    this.load = function (panel, data) {
      this.roleInfo = data;

      $('.role-info-aside', panel).tabs({
        onLoad: function(tab) {
          var index = $(this).tabs('getTabIndex', tab);
          controllers[index].init(tab, data);
        }
      })
    };

  });

  return RoleInfoAside;
});
