define(function(require) {
	var Core = require('core/core');
	var Page = require('core/page');
	var Config = require('config');
	
	var DatabaseInfoAdd = require('./databaseinfo.add');
	var DatabaseInfoEdit = require('./databaseinfo.edit');
	var DatabaseInfoRemove = require('./databaseinfo.remove');
    
	// 角色信息列表
	var DatabaseInfo = Page.extend(function() {
		this.injecte([
	        new DatabaseInfoAdd('databaseinfo_add'), 
	        new DatabaseInfoEdit('databaseinfo_edit'), 
	        new DatabaseInfoRemove('databaseinfo_remove')
	    ]);
		
		// @override
		this.load = function(panel) {
			panel.find('table').cdatagrid({
				// 必须要加此项!!
				controller: this
				
			});
		};
	});
	
	return DatabaseInfo;
});