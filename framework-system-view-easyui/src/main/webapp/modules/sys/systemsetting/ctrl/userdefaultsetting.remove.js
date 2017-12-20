define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
  var $ = require('jquery');
	var Page = require('core/page');

	// 删除用户个人设置
	var UserDefaultSettingRemove = Page.extend(function() {

    this.renderButton = function (btn, row) {
      if (row.paramValue === 'null') {
        return false;
      }
    };

		// @override
		this.submit = function(table, data) {
      if (data.paramValue === 'null') {
		    $.messager.alert("警告","值已为null！","warning");
		    return;
      }

			Core.ajax(Config.ContextPath+'system/usersetting/deletedefault/'+data.paramCode, {
				method: 'delete'
			}).then(function(){
				return require('loaders/cache/loader.system').loadAll()
			}).then(function() {
				table.datagrid('reload');
			});
		};
	});

	return UserDefaultSettingRemove;
});
