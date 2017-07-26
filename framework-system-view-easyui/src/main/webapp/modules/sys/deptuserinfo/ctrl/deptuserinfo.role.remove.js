define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	
	var Page = require('core/page');
	
	// 删除用户角色
	var UserInfoRoleRemove = Page.extend(function() {
		
		// @override
		this.submit = function(table, data) {
			
		
			
			Core.ajax(Config.ContextPath+'system/userrole/'+data.roleCode+'/'+data.userCode, {
				method: 'post',
				data: {
					_method: 'DELETE',
						obtainDate:data.obtainDate
				}
			}).then(function() {
				table.datagrid('reload');
			});
		};
		
	    
		
	});
	
	return UserInfoRoleRemove;
});