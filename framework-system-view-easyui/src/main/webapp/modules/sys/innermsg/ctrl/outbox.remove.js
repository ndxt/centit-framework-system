define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');

	// 删除消息
	var InnerMsgRemove = Page.extend(function() {

		// @override
		this.submit = function(table, data) {
			Core.ajax(Config.ContextPath+'system/innermsg/'+data.msgCode, {
            	type: 'json',
                method: 'post',
                data: {
                    _method: 'delete'
                }
			}).then(function() {
                table.datagrid('reload');
            });
		}
	});

	return InnerMsgRemove;
});
