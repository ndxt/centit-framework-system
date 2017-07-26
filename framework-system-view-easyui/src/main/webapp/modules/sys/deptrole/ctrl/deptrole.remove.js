define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');
	
	// 删除数据字典
	var RoleInfoRemove = Page.extend(function() {
		
		// @override
		this.submit = function(table, data) {
			Core.ajax(Config.ContextPath+'system/roleinfo/'+data.roleCode, {
            	type: 'json',
                method: 'delete',
			}).then(function(){
				return require('loaders/cache/loader.system').loadAll()
			}).then(function() {
            	table.datagrid('reload');
            });
		};
	});
	
	return RoleInfoRemove;
});