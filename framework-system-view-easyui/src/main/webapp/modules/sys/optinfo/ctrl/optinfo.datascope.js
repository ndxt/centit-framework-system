define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');
	
	var OptInfoPowerAdd = require('../ctrl/optinfo.power.add');
    var OptInfoPowerRemove = require('../ctrl/optinfo.power.remove');
	
	var OptInfoPower = Page.extend(function() {
		var _self = this;
		
		this.injecte([
              new OptInfoPowerAdd('optinfo_power_add'),
              new OptInfoPowerRemove('optinfo_power_remove')
		]);
		
		// @override
		this.load = function(panel, data) {
			var form = panel.find('form');
			var table = this.table = panel.find('table');
			
			Core.ajax(Config.ContextPath + 'system/optinfo/' + data.id, {
				method: 'get'
			}).then(function(data) {
				_self.data = data;
				
				form.form('load', data);
				
				// 表格数据
				table.cdatagrid({
						controller: _self
					})
					.datagrid('loadData', data.dataScopes);
			});
		};
		
		// @override
		this.submit = function(panel, data, closeCallback) {
			var form = panel.find('form');
			var table = this.table;
			
			if (form.form('validate') && table.cdatagrid('endEdit')) {
				var formData = form.form('value');
				var dataScopes = table.datagrid('getData').rows;
				
				$.extend(data, formData);
				data.dataScopes = dataScopes;
				data._method = 'PUT';
				
				Core.ajax(Config.ContextPath+'system/optinfo/'+data.optId, {
					data: data,
					method: 'post'
				}).then(closeCallback);
			}
		};
	});
	
	return OptInfoPower;
});