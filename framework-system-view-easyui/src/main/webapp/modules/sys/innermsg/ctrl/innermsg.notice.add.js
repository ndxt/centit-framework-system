define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');
	var Cache = require('core/cache');
    //发送公告
	var NotifyAdd = Page.extend(function() {
		var loginuser=Cache.get('loginuser');
		// @override
		this.object = {
			msgType: 'P',
			
			// 类型：消息
			mailType: 'O',
			
			holdUsers: 2,
			
			// 状态：未读
			msgState: 'U',
			sender:loginuser.userCode
		};
		// @override
		this.load = function(panel, data) {
			panel.find('form').form('disableValidation')
			.form('focus');
		};
		
		
		this.submit = function(panel,data,closeCallback){
			var form = panel.find('form');
			var table=this.parent.table;
			form.form('enableValidation');
			var isValid = form.form('validate');
			var formData = form.form('value');
			var unitCode=formData.unitCode;
			if (isValid) {
				form.form('ajax', {
					url: Config.ContextPath+'system/innermsgrecipient/notify/'+unitCode,
					method: 'post',
					data: this.object
				}).then(function(){
					table.datagrid('reload');
					closeCallback();
				});
			}
			return false;
		}
		// @override
		this.onClose = function() {
			
		};
	});
	
	return NotifyAdd;
});