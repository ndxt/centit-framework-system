define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var InnerMsgWrite = require('../ctrl/innermsg.write');
	var ReplyTemplate = require('text!../reply.template.html');
	var Mustache = require('plugins/mustache.min');
	
	// 回复消息
	var InnerMsgReply = InnerMsgWrite.extend(function() {
		
		// @override
		this.load = function(panel, data) {
			data.receive = data.sender;
			data.msgContent = Mustache.render(ReplyTemplate, data);
			data.msgTitle = "回复：" + data.msgTitle;
			delete data.sender;
		
			panel.find('form').form('disableValidation')
				.form('load', data)
				.form('readonly', 'receive')
				.form('focus');
		};
		
	});
	
	return InnerMsgReply;
});