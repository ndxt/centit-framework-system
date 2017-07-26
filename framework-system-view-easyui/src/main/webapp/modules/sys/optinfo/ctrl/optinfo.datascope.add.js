define(function(require) {
	var Page = require('core/page');

	var OptInfoDatascopeAdd = Page.extend(function() {

		// @override
		this.object = {
			optReq : 'R'
		};

		// @override
		this.submit = function(panel, data) {
			var table = this.parent.panel.find("#datascope");

			if (!table.cdatagrid('endEdit')) {
				return;
			}

			// 插入新数据
			table.datagrid('appendRow', $.extend({}, this.object));

			var index = table.datagrid('getRows').length - 1;
			table.datagrid('selectRow', index);

			// 开启编辑
			table.cdatagrid('beginEdit', index, 'dataCode');
		};
	});

	return OptInfoDatascopeAdd;
});