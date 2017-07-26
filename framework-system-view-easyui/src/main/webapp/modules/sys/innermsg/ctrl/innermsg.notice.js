define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');
	
	var InnerMsgView = require('../ctrl/innermsg.view');
    var InnerMsgRemove = require('../ctrl/innermsg.remove');
	var NotifyAdd=require('../ctrl/innermsg.notice.add');
    
	var Notify = Page.extend(function() {
		
		this.injecte([
	          new InnerMsgView('innermsg_view'), 
	          new InnerMsgRemove('innermsg_remove'),
	          new NotifyAdd('notice_add')
	        ]);
		
		// @override
		this.load = function(panel, data) {
			var table=this.table=panel.find('table');
			table.cdatagrid({
				// 必须要加此项!!
				controller: this
			});
			
		};
		
	});
	
	return Notify;
});
function filterData(datas, callback, childField, level, filter) {
	callback = callback || $.noop;
	childField = childField || "children";
	level = level || 0;
	var recursion = function(data, callback, parent, level) {
//		data.forEach(function(obj, index) {
		for (var i = 0; i < data.length; i++) {
			var obj = data[i];
			if (filter && obj.unitCode == filter) {
				parent.children.splice(i, 1);
				i--;
			} else {
				obj["id"] = obj.unitCode;
				obj["text"] = obj.unitName;
				var children = obj[childField];
				if (children && children.length)
					recursion(children, callback, obj, level + 1);
				// 当前对象、父级对象、层级、当前对象的index
				callback.call(obj, obj, parent, level, i);
			}
		}
//		});
	};
	datas.length ? recursion(datas, callback, null, 0) : recursion([datas], callback, null, 0);
	return datas;
}