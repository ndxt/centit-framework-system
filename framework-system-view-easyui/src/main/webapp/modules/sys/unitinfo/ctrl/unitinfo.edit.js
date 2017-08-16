define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var UnitInfoAdd = require('../ctrl/unitinfo.add');
	
	var UnitInfoEdit = UnitInfoAdd.extend(function() {
		var _self = this;
		
		// @override
		this.load = function(panel, data) {
			var form = panel.find('form');
			Core.ajax(Config.ContextPath+'system/unitinfo/'+data.unitCode, {
				type: 'json',
				method: 'get'
			}).then(function(data) {
				_self.data = data;
				
				form.form('disableValidation').form('load', data)
					.form('focus');
				
			});
			
		};
		
		// @override
		this.submit = function(panel, data,closeCallback) {
			var form = panel.find('form');
			form.form('enableValidation');
			var isValid = form.form('validate');
			if (isValid) {
				this.newObject = form.form('value');
				data._method = 'PUT';
			
				form.form('ajax', {
					data: data,
					url: Config.ContextPath + 'system/unitinfo/' + data.unitCode,
					method: 'put'
				}).then(function(){
					return require('loaders/cache/loader.system').loadAll()
				}).then(closeCallback);
			}
			
			return false;
		};
		
		// @override
		this.onClose = function(table) {
			var newObject =this.newObject;
			if(!newObject)return ;
			table.treegrid('clearSelections');
			table.treegrid('reload');
			this.newObject=null;
        };
	});
	
	return UnitInfoEdit;
});