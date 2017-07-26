define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');
	
	// 删除数据字典
	var DictionaryRemove = Page.extend(function() {
		
		this.url = 'bussi/zidian/';
		
		// @override
		this.submit = function(table, data) {
			Core.ajax(Config.ContextPath+this.url+data.catalogCode, {
            	type: 'json',
                method: 'post',
                data: {
                    _method: 'delete'
                }
			}).then(function() {
                table.datagrid('reload');
            });
		}
	});
	
	return DictionaryRemove;
});