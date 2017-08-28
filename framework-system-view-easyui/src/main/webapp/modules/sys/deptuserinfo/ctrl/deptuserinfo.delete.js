define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');
	
	// 删除数据字典
	var UserInfoDelete = Page.extend(function() {
		
		// @override
		this.submit = function(table, data) {
            var userCodes = [];
            for(var i = 0; i < data.length; i++){
                userCodes.push(data[i].userCode);
            }
			Core.ajax(Config.ContextPath+'system/userinfo/'+userCodes, {
            	type: 'json',
                method: 'post',
                data: {
                    _method: 'delete'
                }
			}).then(function(){
				return require('loaders/cache/loader.system').loadAll()
			}).then(function() {
				table.datagrid('reload');
				var layout = $('#deptuserinfo_panel').layout('panel', 'east');
                layout.panel('setTitle','机构与权限');
				layout.panel('clear');
            });
		};
	});
	
	return UserInfoDelete;
});