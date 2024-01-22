define(function(require) {
	var Core = require('core/core');
	var Page = require('core/page');
	var Config = require('config');
	
	// 删除数据字典
	var DatabaseInfoRemove = Page.extend(function() {
		
		// @override
		this.submit = function(table, data) {
			Core.ajax(Config.ContextPath+'system/sys/database/'+data.databaseCode, {
            	type: 'json',
                method: 'post',
                data: {
                    _method: 'delete'
                }
			}).then(function(){
				table.datagrid('reload');
			});
		};
	});
	
	return DatabaseInfoRemove;
});