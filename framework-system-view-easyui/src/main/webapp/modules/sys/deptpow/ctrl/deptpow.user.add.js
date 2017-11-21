define(function(require) {
	var Config = require('config');
	var Core = require('core/core');

	var Page = require('core/page');

	// 机构添加用户
	var DeptPowUserAdd = Page.extend(function() {

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

      $('#unituser').combobox({
        url:Config.ContextPath+'system/unitinfo/currentusers/T',
        valueField:'userCode',
        textField:'userName',
        multiple: true,
        multiline: true
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
				}).then(closeCallback);
			}

			return false;
		};

		// @override
		this.onClose = function(table) {
			table.datagrid('reload');
		};
	});

	return DeptPowUserAdd;
});
