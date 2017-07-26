define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');
	
	var UnitInfoAdd = require('../ctrl/deptpow.add');
	var UnitInfoEdit = require('../ctrl/deptpow.edit');
	var UnitInfoRemove = require('../ctrl/deptpow.remove');
	
	var UnitInfoUser = require('../ctrl/deptpow.user');
	var unitInfoUser = new UnitInfoUser('deptpow_user');
	
	var DeptPow = Page.extend(function() {
		
		this.injecte([
		  new UnitInfoAdd('deptpow_add'),
		  new UnitInfoEdit('deptpow_edit'),
		  new UnitInfoRemove('deptpow_remove'),
		  unitInfoUser
		]);
		var userpanel = $('#deptpow_panel').layout('panel', 'east');
		var currentUnit=null;
		// @override
		this.load = function(panel) {
			var table = this.table = panel.find('table');
			
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
					userpanel.data('panel').options.onLoad = function() {
						unitInfoUser.init(userpanel, row);
					};
					userpanel.panel('setTitle', row.unitName + ' 组织信息');
					userpanel.panel('refresh', Config.ViewContextPath + 'modules/sys/deptpow/deptpow-user.html');
				}
			});
		};

	});
	
	return DeptPow;
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