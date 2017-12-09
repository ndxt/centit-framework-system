define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');

	// 添加数据字典
	var InnerMsgView = Page.extend(function() {

		// @override
		this.load = function(panel, data) {

			var sendData = $.extend({}, data, {
				_method: 'PUT',
				msgState: 'R'
			})
			// 不用发送这些字段
			delete sendData.mInnerMsg;

			Core.ajax(Config.ContextPath+'system/innermsg/recipient/'+data.id, {
				method: 'post',
				data: $.extend({},sendData,{'mInnerMsg':data.mInnerMsg,})
			});

			data.msgContent = data.mInnerMsg.msgContent;
			panel.find('form').form('load', data);
		};
		// @override
		this.onClose = function(table) {
			table.datagrid('reload');
		};
	});

	return InnerMsgView;
});
