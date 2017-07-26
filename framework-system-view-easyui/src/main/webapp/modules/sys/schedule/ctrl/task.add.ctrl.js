define(function(require) {
	'use strict';
	
	var Core = require('core/core');
	var Config = require('config');
	
	var Page = require('core/page');
	
	var TaskController = Page.extend(function() {
		
		this.load = function(panel, data) {
			var form = panel.find('form');
			
			form.form('disableValidation').form('load', data)
				.form('focus');
		};
		
		this.submit = function(panel, data, closeCallback) {
			var form = panel.find('form');
			
			// 开启校验
			var isValid = form.form('enableValidation').form('validate');
			
			if (isValid) {
				form.form('ajax', {
					url: Config.ContextPath + 'system/schedule',
					method: 'post'
				}).then(closeCallback);
			}
			
			return false;
		};

	});
	
	return TaskController;
});