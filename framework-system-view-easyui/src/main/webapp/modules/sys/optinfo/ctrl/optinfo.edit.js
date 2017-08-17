define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var OptInfoAdd = require('../ctrl/optinfo.add');

	var OptInfoEdit = OptInfoAdd.extend(function() {
		var _self = this;
		
		// @override
		this.load = function(panel, data) {
			var form = panel.find('form');
			
			Core.ajax(Config.ContextPath+'system/optinfo/'+data.id, {
				type: 'json',
				method: 'get' 
			}).then(function(data) {
				_self.data = data;
				
				form.form('disableValidation')
					.form('load', data)
					.form('readonly', 'optId')
					.form('focus');
			});
		};
		
		// @override
		this.submit = function(panel, data, closeCallback) {
			var form = panel.find('form');
			
			// 开启校验
			form.form('enableValidation');
			var isValid = form.form('validate');
			
			if (isValid) {
				this.newObject = form.form('value');
			
				form.form('ajax', {
					url: Config.ContextPath + 'system/optinfo/' + data.optId,
					method: 'put',
					data: data
				}).then(function(){
					return require('loaders/cache/loader.system').loadAll()
				}).then(closeCallback);
			}

			return false;
		};
		
		// @override
		this.onClose = function(table) {
			var newObject = this.newObject;
			
			if (!newObject) return;
			
			newObject = $.extend(newObject, {
				id: newObject.optId,
				text: newObject.optName,
				url: newObject.optRoute
			})
		
			// 更新树中菜单
            table.treegrid('update', {
                id: newObject.id,
                row: newObject
            });
            
            var panel = table.treegrid('getPanel');
    		btns = panel.find('.easyui-linkbutton').linkbutton();
		};
	});

	return OptInfoEdit;
});