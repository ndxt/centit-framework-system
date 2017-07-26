define(function(require) {
	var Page = require('core/page');
	
	// 删除字典明细
	var DictionaryItemRemove = Page.extend(function() {
		var vm = this;

		// @override
		this.submit = function(table, row) {
			// 列表
			if (this.parent.type == 'L') {
				this.removeListItem(table, row);
			}
			else {
				this.removeTreeItem(table);
			}

			
		};

		// 删除列表明细
		this.removeListItem = function (table, row) {
			var index = table.datagrid('getRowIndex', row);
			table.datagrid('deleteRow', index);
		}

		// 删除树明细
		this.removeTreeItem = function (table) {
			var row = table.treegrid('getSelected');

			if (row) {
				table.treegrid('remove', row._id);
			}
			
		}
	});
	
	return DictionaryItemRemove;
});