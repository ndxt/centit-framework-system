define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var $ = require('jquery');

	var Page = require('core/page');

	// 机构添加用户
	var UnitInfoUserAdd = Page.extend(function() {

		// @override
		this.object = {
			isPrimary: 'T'
		};

		// @override
		this.load = function(panel) {
			// 获取父窗口的机构信息
			var unitinfo = this.parent.data;

			var data = this.data = $.extend({}, this.object, {
				unitCode: unitinfo.unitCode,
				unitName: unitinfo.unitName
			});

			panel.find('input[name=userCode]').combobox({
        url: this.$findUp('queryUnitUserUrl'),
        textField: 'userName',
        valueField: 'userCode'
      });

			panel.find('form')
				.form('disableValidation')
        .form('load', data)
				.form('focus');
		};

		// @override
		this.submit = function(panel, data, closeCallback) {
			var form = panel.find('form');
			form.form('enableValidation');
			var isValid = form.form('validate');

			if (isValid) {
				form.form('ajax', {
					url: Config.ContextPath+'system/userunit',
					data: data
				}).then(function(){
          closeCallback(true)
				})
			}

			return false;
		};

		// @override
		this.onClose = function(table, submitted) {
		  if (submitted) {
        table.datagrid('reload');
      }
		};
	});

	return UnitInfoUserAdd;
});
