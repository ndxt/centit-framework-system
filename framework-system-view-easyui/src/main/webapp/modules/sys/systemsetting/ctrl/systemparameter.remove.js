define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');

	// 删除系统参数
	var SystemParameterRemove = Page.extend(function() {

    this.renderButton = function (btn, row) {
      if (row.paramValue === 'null') {
        return false;
      }
    };

		// @override
		this.submit = function(table, data) {

			Core.ajax(Config.ContextPath+'system/usersetting/deletedefault/'+data.paramCode, {
				method: 'delete'
			}).then(function(){
				return require('loaders/cache/loader.system').loadAll()
			}).then(function() {
				table.datagrid('reload');
			});
		};
	});

	return SystemParameterRemove;
});
