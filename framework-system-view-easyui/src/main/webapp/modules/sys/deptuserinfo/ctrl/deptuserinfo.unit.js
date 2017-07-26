define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');
	
	var UserInfoUnitAdd = require('../ctrl/deptuserinfo.unit.add');
	var UserInfoUnitEdit = require('../ctrl/deptuserinfo.unit.edit');
	var UserInfoUnitRemove = require('../ctrl/deptuserinfo.unit.remove');
	
	var UserInfoUnit = Page.extend(function() {
		var _self = this;
		
		this.injecte([
		  new UserInfoUnitAdd('deptuserinfo_unit_add'),   
		  new UserInfoUnitEdit('deptuserinfo_unit_edit'),
		  new UserInfoUnitRemove('deptuserinfo_unit_remove')
		]);
		
		// @override
		this.load = function(panel, data) {
			this.data = data;
			
			var form = panel.find('form').form('load', data);
			var table =this.table= panel.find('table');
			table.cdatagrid({
				controller: _self,
				url: Config.ContextPath+'system/userunit/userunits/'+data.userCode
			})
		};
	});
	
	return UserInfoUnit;
});