define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');

	// 回复消息
	var InnerMsgWrite = Page.extend(function() {

		// @override
		this.object = {
			receive:null,
			mInnerMsg:null,
			msgType: 'P',
			// 类型：消息
			mailType: 'O',
			// 状态：未读
			msgState: 'U',
		};

		// @override
		this.load = function(panel) {
			panel.find('form').form('disableValidation')
				.form('focus');
		};

		// @override
		this.submit = function(panel, data, closeCallback) {

			var form = panel.find('form');

			form.form('enableValidation');
			var isValid = form.form('validate');
			var formData = form.form('value');
			this.object.mInnerMsg={
					msgTitle:formData.msgTitle,msgContent:formData.msgContent,msgType: 'P',
					// 类型：消息
					mailType: 'O',
					// 状态：未读
					msgState: 'U',};
			this.object.receive=formData.receive;
			if (isValid) {
				 Core.ajax(Config.ContextPath+'system/innermsg/sendMsg', {
						method: 'post',
						data: this.object
					}).then(closeCallback);
			}
			return false;
		};

		// @override
		this.onClose = function(table) {
			table.datagrid('reload');
		};
	});

	return InnerMsgWrite;
});
