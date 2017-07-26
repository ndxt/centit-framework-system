define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Utils = require('core/utils');
	var Page = require('core/page');
	
	var RoleInfoOperate = Page.extend(function() {
		var _self = this;
		
		// @override
		this.load = function(panel, data) {
			var tree = panel.find('ul');
			Core.ajax(Config.ContextPath + 'system/roleinfo/' + data.roleCode, {
				method: 'get'
			}).then(function(data) {
				_self.data = data;
			
				var powers = data.rolePowers.map(function(obj) {
					return obj.optCode;
				});
			
				_createOptInfoTree(tree, powers);
			});
		};
		
		// @override
		this.submit = function(panel, data, closeCallback) {
			
			// 选中的节点
			var nodes = panel.find('ul.tree').tree('getChecked');
			
			// 过滤得到选中的optCodes
			var optCodes = nodes.filter(function(obj) {
				return obj.optCode;
			}).map(function(obj) {
				return obj.optCode
			});
			
			data.optCodes = optCodes.join(',');
			data._method = 'PUT';
			
			Core.ajax(Config.ContextPath + 'system/roleinfo/' + data.roleCode, {
				method: 'post',
				data: data
			}).then(closeCallback);
		
			return false;
		};
		
		// 创建选择操作权限树
		var _createOptInfoTree = function(tree, powers) {
			Core.ajax(Config.ContextPath + 'system/optinfo/unitpoweropts/'+_self.data.unitCode+'?field=id&field=iconCls&field=text&field=optMethods&field=children', {
				method: 'get'}).then(function(data) {
					Utils.walkTree(data, function(obj) {
						var isLeaf = !(obj.children && obj.children.length);						
						// 非叶子节点不考虑权限
						if (!isLeaf) {
							return;
						}						
						// 操作定义
						var optDefs = obj.optMethods;
						if (optDefs) {
							optDefs.forEach(function(def) {
								def.id = def.optCode;
								def.text = def.optName;
								
								// 选中角色已有的操作定义
								if (powers.indexOf(def.id) > -1) {
									def.checked = true;
								}
							});
							
							// 将操作定义放到叶子节点下
							obj.children = optDefs;
							obj.state = "closed";
						}
					});
				
					tree.tree({
						data: data
					});
				});
		};
	});
	
	return RoleInfoOperate;
});