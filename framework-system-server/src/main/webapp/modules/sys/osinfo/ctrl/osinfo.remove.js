define(function(require) {
	var Core = require('core/core');
	var Page = require('core/page');
	var Config = require('config');
	
	// 删除数据字典
	var OsInfoRemove = Page.extend(function() {
		
		// @override
		this.submit = function(table, data) {
			Core.ajax(Config.ContextPath+'system/sys/os/'+data.osId, {
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
	
	return OsInfoRemove;
});