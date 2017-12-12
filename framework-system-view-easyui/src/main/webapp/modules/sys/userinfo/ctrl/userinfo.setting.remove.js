define(function(require) {
	var Config = require('config');
	var Core = require('core/core');

	var Page = require('core/page');

	// 删除机构用户
	var UserSettingRemove = Page.extend(function() {

		// @override
		this.submit = function(table, data) {

			Core.ajax(Config.ContextPath+'system/usersetting/'+data.cid.paramCode, {
				method: 'post',
				data: {
					_method: 'DELETE',
				}
			}).then(function(){
				return require('loaders/cache/loader.system').loadAll()
			}).then(function() {
				table.datagrid('reload');
			});
		};

	});

	return UserSettingRemove;
});
