define(function(require) {
	var Page = require('core/page');

	var DictionaryItemAdd = Page.extend(function() {

		// @override
		this.object = {
			dataTag: 'T',
			dataStyle: 'U'
		};

		// @override
		this.submit = function(panel, data) {
			var table = this.parent.table;

			if (this.parent.type == 'L') {
				this.editDatagrid(table);
			}
			else if (this.parent.type == 'T') {
				this.editTreegrid(table);
			}
		};

		// 编辑普通表格
		this.editDatagrid = function(table) {
			if (!table.cdatagrid('endEdit')) {
				return;
			}

			// 插入新数据
			table.datagrid('appendRow', $.extend({}, this.object, {
				catalogCode: this.parent.data.catalogCode,
				dataOrder: table.datagrid('getRows').length + 1
			}));

			var index = table.datagrid('getRows').length-1;
			table.datagrid('selectRow', index);

			// 开启编辑
			table.cdatagrid('beginEdit', index, 'dataCode');
		};

		// 编辑树形表格
		this.editTreegrid = function(table) {
			if (!table.ctreegrid('endEdit')) {
				return;
			}

			var row = table.treegrid('getSelected');

			var id = new Date().getTime(), newRow;

			if (row) {
				newRow = $.extend({
					_id: id,
					icon: ""
				}, this.object, {catalogCode: this.parent.data.catalogCode});

				table.treegrid('insert', {
					after: row._id,
					data: newRow
				});
			}
			else {
				newRow = $.extend({
					_id: id,
					icon: ""
				}, this.object, {catalogCode: this.parent.data.catalogCode});

				table.treegrid('append', {
					parent: null,
					data: [newRow]
				});
			}

			// 开启编辑
			table.ctreegrid('beginEdit', 'dataCode', newRow);
		}
	});

	return DictionaryItemAdd;
});
