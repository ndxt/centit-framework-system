define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');
	
	var InnerMsgView = require('../ctrl/innermsg.view');
	var OutBoxView=require('../ctrl/outbox.view');
    var InnerMsgRemove = require('../ctrl/innermsg.remove');
    var OutBoxRemove=require('../ctrl/outbox.remove');
    var InnerMsgReply = require('../ctrl/innermsg.reply');
    var InnerMsgWrite = require('../ctrl/innermsg.write');
	var InnerMsgNotice=require('../ctrl/innermsg.notice');
	var NotifyAdd=require('../ctrl/innermsg.notice.add');
    // 数据字典列表
	var InnerMsg = Page.extend(function() {
		
		this.injecte([
          new InnerMsgView('innermsg_view'), 
          new InnerMsgRemove('innermsg_remove'),
          new InnerMsgReply('innermsg_reply'),
          new InnerMsgWrite('innermsg_write'),
          new NotifyAdd('notice_add'),
          new InnerMsgNotice('innermsg_notice'),
          new OutBoxView('outbox_view'),
          new OutBoxRemove('outbox_remove')
        ]);
		
		// @override
		this.load = function(panel) {
			var table=this.table=panel.find('table');
			table.cdatagrid({
				// 必须要加此项!!
				controller: this,
				
				rowStyler: function(index, row) {
					if (row.msgState == 'U') {
						return {'class':'mask'};	
					}
				}
			});
		};
	});
	
	return InnerMsg;
});