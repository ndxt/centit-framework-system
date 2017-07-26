define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');
	
	var LogInfoView = require('../ctrl/loginfo.view');
	var LogInfoRemove = require('../ctrl/loginfo.remove');

	var LogInfo = Page.extend(function() {
		
		this.injecte([
		    new LogInfoView('loginfo_view'),
		    new LogInfoRemove('loginfo_remove')
		]);
		
		// @override
		this.load = function(panel) {
			panel.find('table').cdatagrid({
				controller: this
			});
		};
	});
	
	return LogInfo;
});