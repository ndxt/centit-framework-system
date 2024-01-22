define(function(require) {
	var Core = require('core/core');
	var Page = require('core/page');
	var Config = require('config');
	var OsInfoAdd = require('./osinfo.add');
	
	// 编辑角色信息
	var OsInfoEdit = OsInfoAdd.extend(function() {
		var _self = this;
	
		// @override
		this.load = function(panel, data) {
			var form = panel.find('form');
			
			Core.ajax(Config.ContextPath + 'system/sys/os/' + data.osId, {
				method: 'get'
			}).then(
				function(data) {
										
					_self.data = $.extend(_self.object, data);
					
					form.form('load', data)
						.form('disableValidation')
						.form('readonly', 'osId')
						.form('focus');
				}
			);
		};
		
		// @override
		this.submit = function(panel, data, ClosecallBack) {
			var form = panel.find('form');
			
			// 开启校验
			var isValid = form.form('enableValidation').form('validate');
			
			if (isValid) {
				form.form('ajax', {
					url: Config.ContextPath + 'system/sys/os/' + data.osId,
					method: 'put',
					data: data
				}).then(function(){
					var table=_self.panel.find('#osinfoTable');
					table.datagrid('reload');
					ClosecallBack();
				});
				
			}
			
			return false;
		};

	});
	
	return OsInfoEdit;
});