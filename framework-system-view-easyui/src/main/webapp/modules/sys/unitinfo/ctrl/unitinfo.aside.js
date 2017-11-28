define(function(require) {
  var $ = require('jquery');
  var Page = require('core/page');
  var UnitInfoUser = require('../ctrl/unitinfo.user');

  return Page.extend(function() {
    var controllers = [
      new UnitInfoUser('UnitInfoUser')
    ];

    var tabSelectedIndex = 0;

    this.injecte(controllers);

    this.load = function(panel, data) {
      $('.unit-info-tabs', panel).tabs({
        selected: tabSelectedIndex,

        onLoad: function(tab) {
          tabSelectedIndex = $(this).tabs('getTabIndex', tab);
          controllers[tabSelectedIndex].init(tab, data);
        },

        onSelect: function(title, index) {
          tabSelectedIndex = index;
        }
      });
    }
  });
});
