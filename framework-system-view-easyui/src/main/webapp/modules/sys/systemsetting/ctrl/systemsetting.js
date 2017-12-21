define(function(require) {

	var Page = require('core/page');

	var UserDefaultSetting = require('./userdefaultsetting');
	var SystemParameter = require('./systemparameter');
	var SystemIcon = require('./systemicon');


	var SystemSetting = Page.extend(function() {
		var tabSelectedIndex = 0;
		var controllers = [
		  new UserDefaultSetting('UserDefaultSetting'),
      new SystemParameter('SystemParameter'),
      new SystemIcon('SystemIcon')
    ];

		this.injecte(controllers);

		// @override
		this.load = function(panel, data) {
			this.data = data;

			$('.user-info-aside', panel).tabs({
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

	return SystemSetting;
});
