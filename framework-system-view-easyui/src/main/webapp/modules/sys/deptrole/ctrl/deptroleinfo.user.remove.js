define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	
	var Page = require('core/page');
	
	// 删除用户角色
	var DeptRoInfoUserRemove = Page.extend(function() {
		
		// @override
		this.submit = function(table, data) {		
			
			var  selRow = table.datagrid("getSelections");//获取取中元素
			var ids=[];//选 中的djID
			for(var i=0;i<selRow.length;i++){
				var id=selRow[i].userCode;
				ids.push(id);
			}
			
			Core.ajax(Config.ContextPath+'system/userrole/'+this.parent.data.roleCode+'/'+ids, {
				method: 'post',
				data: {
					_method: 'DELETE',
						obtainDate:data.obtainDate
				}
			}).then(function(){
				return require('loaders/cache/loader.system').loadAll()
			}).then(function() {
				table.datagrid('reload');
			});
		};		

	});
	
	return DeptRoInfoUserRemove;
});