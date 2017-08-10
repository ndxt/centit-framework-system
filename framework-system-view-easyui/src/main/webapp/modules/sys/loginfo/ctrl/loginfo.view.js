define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');
	
	var LogInfoView = Page.extend(function() {
		
		// @override
		this.load = function(panel, data) {
			var form = panel.find('form');
			var table = panel.find('table');
			
			Core.ajax(Config.ContextPath + 'system/optlog/' + data.logId, {
				method: 'get'
			}).then(function(data) {
				form.form('load', data);
				
				var grid = parseLogContent(data.newValue, data.oldValue);
				table.datagrid({
						title: data.optContent,
						rowStyler: function(index,row){
							if (!row.equals){
								return 'background-color:#F5EE68;color:#FF3232;';
							}
						}
					})
					.datagrid('loadData', grid.rows);
			});
		};
		
		// 解析日志内容
		function parseLogContent(after, before) {
			var items = [];
			var afterMap = JSON.parse(after||"{}"), beforeMap = JSON.parse(before||"{}");
			var tempMap = JSON.parse(after||"{}");
			// var afterMap = JSON.stringify(afterMapJson);
			for(var key in beforeMap){
				tempMap[key] = beforeMap[key];
		    }
			
			for(var key in tempMap){
				var temp = {};
				temp.key=key;
				temp.before = JSON.stringify(beforeMap[key]);
				temp.after = JSON.stringify(afterMap[key]);
				temp.equals = (temp.before && temp.after && temp.before == temp.after);
			
				items.push(temp);
			}
			
			return {
				rows: items
			};
		};
	});
	
	return LogInfoView;
});