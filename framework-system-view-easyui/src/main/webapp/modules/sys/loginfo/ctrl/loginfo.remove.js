define(function(require) {
	require('plugins/extend');

	var Page = require('core/page');
	var Core = require('core/core');
	var Config = require('config');
	
	var LogInfoRemove = Page.extend(function() {
		
		// TODO 日志删除
		// @override
		this.submit = function(table, data) {
			var logIds = data.map(function(val){
				return val.logId;
			})
			Core.ajax(Config.ContextPath+'system/optlog/deleteMany', {
           	type: 'json',
               method: 'post',
               data: {
                   _method: 'delete',
				   logIds:logIds
               }
			}).then(function() {
           		table.datagrid('reload');
           });
		}
	});
	
	return LogInfoRemove;
});