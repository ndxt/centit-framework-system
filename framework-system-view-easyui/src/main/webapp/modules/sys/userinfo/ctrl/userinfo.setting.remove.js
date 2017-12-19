define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
  var $ = require('jquery');
	var Page = require('core/page');

	// 删除用户个人设置
	var UserSettingRemove = Page.extend(function() {

    this.renderButton = function (btn, row) {
      if (row.defaultValue) {
        return false;
      }
    };

		// @override
		this.submit = function(table, data) {
		  if(data.defaultValue){
		    $.messager.alert("警告","默认设置不能删除！","warning");
		    return;
      }

			Core.ajax(Config.ContextPath+'system/usersetting/'+data.userCode+'/'+data.paramCode, {
				method: 'delete'
			}).then(function(){
				return require('loaders/cache/loader.system').loadAll()
			}).then(function() {
				table.datagrid('reload');
			});
		};
	});

	return UserSettingRemove;
});
