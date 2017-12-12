define(function(require) {
	var Config = require('config');
	var Core = require('core/core');

	var Page = require('core/page');

	// 删除机构用户
	var UserSettingRemove = Page.extend(function() {

		// @override
		this.submit = function(table, data) {

			Core.ajax(Config.ContextPath+'system/userunit/'+data.userUnitId, {
				method: 'post',
				data: {
					_method: 'DELETE',
					userStation: data.userStation,
	                userRank: data.userRank
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
