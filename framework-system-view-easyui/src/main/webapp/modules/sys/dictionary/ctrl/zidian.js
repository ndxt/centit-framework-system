define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');
	
	var ZiDianAdd = require('../ctrl/zidian.add');
    var ZiDianEdit = require('../ctrl/zidian.edit');
    var ZiDianItem = require('../ctrl/zidian.item');
    var ZiDianRemove = require('../ctrl/zidian.remove');
	
    // 数据字典列表
	var ZiDian = Page.extend(function() {
		this.injecte([
          new ZiDianAdd('zidian_add'), 
          new ZiDianEdit('zidian_edit'), 
          new ZiDianItem('zidian_item'),
          new ZiDianRemove('zidian_remove')
        ]);
		
		// @override
		this.load = function(panel) {

			panel.find('table').cdatagrid({
				// 必须要加此项!!
				controller: this,
//				onClickRow:function(row,data){
//					console.log(data);
//				}
			});
		};
	});
	
	return ZiDian;
});