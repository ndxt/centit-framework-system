define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');
	
	var UnitInfoAdd = require('../ctrl/unitinfo.add');
	var UnitInfoAddTop = require('../ctrl/unitinfo.add.top');
	var UnitInfoEdit = require('../ctrl/unitinfo.edit');
	var UnitInfoRemove = require('../ctrl/unitinfo.remove');
	var UnitInfoUser = require('../ctrl/unitinfo.user');
	var UnitOperate=require('../ctrl/unitinfo.operate');
	var UnitInfo = Page.extend(function() {
		
		var UnitInfoEditObj = new UnitInfoEdit('unitinfo_edit');
		var UnitInfoUserObj = new UnitInfoUser('unitinfo_user');

		this.injecte([
		  new UnitInfoAddTop('unitinfo_add_top'),            
		  new UnitInfoAdd('unitinfo_add'),
		  UnitInfoEditObj,
		  new UnitInfoRemove('unitinfo_remove'),
		  new UnitOperate('unitinfo_operate'),
		  UnitInfoUserObj
		]);
		
		
		// @override
		this.load = function(panel) {
			var table = this.table = panel.find('table');
			var currentUnit=null;
			
			table.ctreegrid({
				controller: this,
				
				rowStyler: function(row) {
					if (row && row.isValid == 'F') {
						return {
							'class': 'ban'
						};
					}
				},
				
				//给table增加单行点击事件
				onClickRow: function(row) {
					// 点击当前节点不响应
					if (row.unitCode == currentUnit) return;
					
					currentUnit = row.unitCode;
					var panel = $('#unitinfo_panel').layout('panel', 'east');
					
//					panel.panel(options);
					panel.data('panel').options.onLoad = function() {
						UnitInfoUserObj.init(panel, row);
					};
					panel.panel('setTitle', row.unitName + ' 组织信息');
					panel.panel('refresh', Config.ViewContextPath + 'modules/sys/unitinfo/unitinfo-user-default.html');
				}
			});
		};

	});
	
	return UnitInfo;
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