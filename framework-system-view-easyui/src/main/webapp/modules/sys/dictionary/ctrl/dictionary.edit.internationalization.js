define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');
	
	var DictionaryI18N = Page.extend(function() {
		var _self = this;
		var row, callback, dataValue;
		
		
		
		// @override
		this.load = function(panel, data) {
			var form = panel.find('form');
			
			row = data.row;
			callback = data.callback;
			
			try {
				dataValue = JSON.parse(row.dataValue);
			}
			catch(e) {
				dataValue = {};
			}
			
			Core.ajax('http://localhost:8180/framework-sys-module/system/dictionary/XMGSD?', {
				method: 'get'
			}).then(function(data) {
				data = data.dataDictionaries.map(function(item) {
					return {
						name: item.label,
						field: item.dataCode,
						value: dataValue[item.dataCode],
						editor: 'text'
					}
				});
				
				panel.find('#pg').propertygrid({showHeader:false,data: {
					total: data.length,
					rows: data
				}});

			});
		};
		
		// @overload
		this.submit = function(panel, data, closeCallback) {
			
			var table = panel.find('#pg');
			var obj = {};
			table.propertygrid('getData').rows.forEach(function(item) {
				obj[item.field] = item.value
			})

			row.dataValue = JSON.stringify(obj);
			
			return true;
		};
		
		this.onClose = function() {
			callback(row);
		}
	});
	
	return DictionaryI18N;
});