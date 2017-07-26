define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');
	
	var DictionaryAdd = require('../ctrl/dictionary.add');
    var DictionaryEdit = require('../ctrl/dictionary.edit');
    var DictionaryItem = require('../ctrl/dictionary.item');
    var DictionaryRemove = require('../ctrl/dictionary.remove');
	
    // 数据字典列表
	var Dictionary = Page.extend(function() {
		
		this.injecte([
          new DictionaryAdd('dictionary_add'), 
          new DictionaryEdit('dictionary_edit'), 
          new DictionaryItem('dictionary_item'),
          new DictionaryRemove('dictionary_remove')
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
	
	return Dictionary;
});