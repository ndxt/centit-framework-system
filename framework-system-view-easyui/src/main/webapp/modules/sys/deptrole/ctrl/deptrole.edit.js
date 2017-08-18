define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var DeptRoleAdd = require('../ctrl/deptrole.add');
	
	// 编辑角色信息
	var DeptRoleEdit = DeptRoleAdd.extend(function() {
		var _self = this;
	
		// @override
		this.load = function(panel, data) {
			var form = panel.find('form');
			
			Core.ajax(Config.ContextPath + 'system/roleinfo/' + data.roleCode, {
				method: 'get'
			}).then(function(data) {
				var optCodes = _self.getOptCodes(data).join(',');
				data.optCodes = optCodes;
				
				_self.data = $.extend(_self.object, data);
				
				form.form('disableValidation')
					.form('load', data)
					.form('readonly', 'roleCode')
					.form('addValidation', {
						roleName: {
							required:true,
							validType:{
								remote:[Config.ContextPath+
										'system/roleinfo/isNameUnique/{{roleName}}/'+data.roleCode+'/'+data.unitCode,
									'roleName']
							}
						}
					})
					.form('focus');
			});
		};
		
		// @override
		this.submit = function(panel, data, closeCallback) {
			var form = panel.find('form');
			
			// 开启校验
			var isValid = form.form('enableValidation').form('validate');
			
			if (isValid) {
				form.form('ajax', {
					url: Config.ContextPath + 'system/roleinfo/' + data.roleCode,
					method: 'put',
					data: data
				}).then(closeCallback);
			}
			
			return false;
		}
		
		// 从角色信息中获取OptCodes集合
		this.getOptCodes = function(role) {
			var powers = role.rolePowers;
			
			if (!powers) return [];
			
			return powers.map(function(obj) {
				return obj.optCode;
			});
		};
	});
	
	return DeptRoleEdit;
});