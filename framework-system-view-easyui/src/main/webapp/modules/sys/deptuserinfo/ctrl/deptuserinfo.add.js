define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');
    var Cache = require('core/cache');
	
	// 编辑角色信息
	var DeptUserInfoAdd = Page.extend(function() {

		// @override
		this.object = {
			isValid: 'T',
			isPrimary: 'T'
		};

        // @override
		this.load = function(panel) {
			var form = panel.find('form');
            var loginuser=Cache.get('loginuser');
            var primaryUnit=loginuser.primaryUnit;
            $('#primaryUnit').combotree({
                url: Config.ContextPath + 'system/unitinfo/underunits/',
                required: true
            });
            var newData={};
            $.extend(newData, this.object,{primaryUnit:this.parent.object.loginuser.primaryUnit});
            form.form('disableValidation')
				.form('load', newData)
				.form('focus');
        };

        // @override
		this.submit = function(panel, data, closeCallback) {
			var form = panel.find('form');
			
			// 开启校验
			var isValid = form.form('enableValidation').form('validate');
			
			if (isValid) {
				var value=this.newObject = form.form('value');
				$.extend(data,{unitCode:value.primaryUnit});
				form.form('ajax', {
					url: Config.ContextPath + 'system/userinfo',
					method: 'post',
					data: data
				}).then(function(){
					return require('loaders/cache/loader.system').loadAll();
				}).then(closeCallback);
			}
			
			return false;
		}
		
		// @override
		this.onClose = function(table) {
			var newObject = this.newObject;
			if (!newObject) return;
			table.datagrid('reload');
			this.newObject=null;
		};
	});
	
	return DeptUserInfoAdd;
});