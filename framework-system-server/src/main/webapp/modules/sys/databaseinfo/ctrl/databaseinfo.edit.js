define(function(require) {
	var Core = require('core/core');
	var Page = require('core/page');
	var Config = require('config');
	var DatabaseInfoAdd = require('../ctrl/databaseinfo.add');
	
	// 编辑角色信息
	var DatabaseInfoEdit = DatabaseInfoAdd.extend(function() {
		var _self = this;
	
		// @override
		this.load = function(panel, data) {
			var form = panel.find('form');
			Core.ajax(Config.ContextPath + 'system/sys/database/' + data.databaseCode, {
            	type: 'json',
                method: 'get'
			}).then(function(data) {
				
				_self.data = $.extend(_self.object, data);
				
				form.form('load', data)
					.form('disableValidation')
//					.form('readonly', 'databaseUrl')
					//.form('readonly', 'password')
					.form('focus');
            });
		};
		
		// @override
		this.submit = function(panel, data, closeCallback) {
			var form = panel.find('form');
			
			// 开启校验
			var isValid = form.form('enableValidation').form('validate');
			var value=form.form('value');
			//value.dataDesc=value.dataDesc.replace(/\+/g,"%2B");
			//value.dataDesc=value.dataDesc.replace(/\&/g,"%26");
			$.extend(data,value,{_method:'put',contentType:'application/json'});
			if (isValid) {
				Core.ajax(Config.ContextPath + 'system/sys/database/' + data.databaseCode, {
	            	type: 'json',
	            	method:'POST',
	            	data:data
				}).then(closeCallback);
				/*form.form('ajax', {
					url: Config.ContextPath + 'system/sys/database/' + data.databaseCode,
					method: 'put',
					data: data,
				})*/
			}
			
			return false;
		};

	});
	
	return DatabaseInfoEdit;
});