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
					url: Config.ContextPath + 'system/schedule/' + data.taskId,
					data: data,
					method: 'put'
				}).then(closeCallback);
			}
			
			return false;
		};
		
		this.remove = function(panel, data, closeCallback) {
			$.messager.confirm('删除', '是否确认删除任务？' function (r) {
                if (r) {
                	Core.ajax(Config.ContextPath + 'system/schedule/' + _self.taskId, {
            	  		method: 'delete' 
            	  	}).then(closeCallback);
                }
            });
		
			return false;
		}

	});
	
	return TaskController;
});