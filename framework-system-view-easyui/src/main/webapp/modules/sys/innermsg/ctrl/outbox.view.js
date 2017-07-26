define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');
	
	// 添加数据字典
	var InnerMsgView = Page.extend(function() {
		
		// @override
		this.load = function(panel, data) {
			
			panel.find('form').form('load', data);
		};
		// @override
		this.onClose = function(table) {
			table.datagrid('reload');
		};
	});
	
	return InnerMsgView;
});