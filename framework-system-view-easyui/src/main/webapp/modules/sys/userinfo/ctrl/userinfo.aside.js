define(function(require) {

	var Page = require('core/page');

	var UserInfoBaseInfo = require('./userinfo.baseinfo');
	var UserInfoRoleInfo = require('./userinfo.role');


	var UserAll = Page.extend(function() {
		var tabSelectedIndex = 0;
		var controllers = [
		  new UserInfoBaseInfo('UserInfoBaseInfo'),
      new UserInfoRoleInfo('UserInfoRoleInfo'),
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

	return UserAll;
});
