define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');
	
	// 删除数据字典
	var UnitInfoRemove = Page.extend(function() {
		
		// @override
		this.submit = function(table, data) {
			Core.ajax(Config.ContextPath+'system/unitinfo/'+data.unitCode, {
            	type: 'json',
                method: 'post',
                data: {
                    _method: 'delete'
                }
			}).then(function(){
				return require('loaders/cache/loader.system').loadAll()
			}).then(function() {
            	table.treegrid('remove', data.unitCode);
				var layout = $('#unitinfo_panel').layout('panel', 'east');
				layout.panel('setTitle','用户信息');
				layout.panel('clear');
            });
		};
		this.renderButton=function(target,data){
			if(data.state=='closed')
				return false;
			else{
				if(data.children.length==0){
					return true;
				}
				return false;
			}
		};
	});
	
	return UnitInfoRemove;
});