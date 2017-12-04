define(function (require) {
  var Page = require('core/page');

  var DictionaryAdd = require('../ctrl/dictionary.add');
  var DictionaryEdit = require('../ctrl/dictionary.edit');
  var DictionaryItem = require('../ctrl/dictionary.item');
  var DictionaryRemove = require('../ctrl/dictionary.remove');

  // 数据字典列表
  return Page.extend(function () {
    this.injecte([
      new DictionaryAdd('dictionary_add'),
      new DictionaryEdit('dictionary_edit'),
      new DictionaryItem('dictionary_item'),
      new DictionaryRemove('dictionary_remove')
    ]);

    this.isAdmin = false;

    // @override
    this.load = function (panel) {
      this.$autoHeight();
      this.table = panel.find('table').cdatagrid({
        controller: this
      });
    };
  });
});
