define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');
	
	var OptInfoPowerAdd = require('../ctrl/optinfo.optdef.add');
    var OptInfoPowerRemove = require('../ctrl/optinfo.optdef.remove');
	
	var OptInfoPower = Page.extend(function() {
		var _self = this;
		
		this.injecte([
              new OptInfoPowerAdd('optinfo_optdef_add'),
              new OptInfoPowerRemove('optinfo_optdef_remove')
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
					.datagrid('loadData', data.optDefs);
			});
		};
		
		// @override
		this.submit = function(panel, data, closeCallback) {
			var form = panel.find('form');
			var table = this.table;
			
			if (form.form('validate') && table.cdatagrid('endEdit')) {
				var formData = form.form('value');
				var optDefs = table.datagrid('getData').rows;
				
				$.extend(data, formData);
				data.optDefs = optDefs;
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