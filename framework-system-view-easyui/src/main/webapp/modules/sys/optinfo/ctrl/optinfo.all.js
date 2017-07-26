define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');
	
	var OptInfoPowerAdd = require('../ctrl/optinfo.optdef.add');
    var OptInfoPowerRemove = require('../ctrl/optinfo.optdef.remove');
    var OptInfoDSAdd = require('../ctrl/optinfo.datascope.add');
    var OptInfoDSRemove = require('../ctrl/optinfo.datascope.remove');
	
	var OptInfoAll = Page.extend(function() {
		var _self = this;
		
		this.injecte([
              new OptInfoPowerAdd('optinfo_optdef_add'),
              new OptInfoPowerRemove('optinfo_optdef_remove'),
              new OptInfoDSAdd('optinfo_datascope_add'),
              new OptInfoDSRemove('optinfo_datascope_remove')
		]);
		
		// @override
		this.load = function(panel, data) {
			var form = panel.find('form');
			var table = this.table = panel.find('table');
			var table_optdef = panel.find('#optdef');
			var table_datascope=panel.find('#datascope');
			Core.ajax(Config.ContextPath + 'system/optinfo/' + data.id, {
				method: 'get'
			}).then(function(data) {
				_self.data = data;
				
				form.form('load', data);
				
				// 表格数据
				table_optdef.cdatagrid({
						controller: _self
					})
					.datagrid('loadData', data.optMethods);
				
				table_datascope.cdatagrid({
					controller: _self
				})
				.datagrid('loadData', data.dataScopes);
			});
		};
		
		// @override
		this.submit = function(panel, data, closeCallback) {
			var form = panel.find('form');
			var table_optdef = panel.find('#optdef');
			var table_datascope=panel.find('#datascope');
			
			if (form.form('validate') && table_optdef.cdatagrid('endEdit')&& table_datascope.cdatagrid('endEdit')) {
				var formData = form.form('value');
				var optDefs = table_optdef.datagrid('getData').rows;
				var dataScopes=table_datascope.datagrid('getData').rows;
				
				$.extend(data, formData);
				data.optDefs = optDefs;
				data.dataScopes=dataScopes;
				data._method = 'PUT';
				
				Core.ajax(Config.ContextPath+'system/optinfo/'+data.optId, {
					data: data,
					method: 'post'
				}).then(closeCallback);
			}
		};
	});
	
	return OptInfoAll;
});