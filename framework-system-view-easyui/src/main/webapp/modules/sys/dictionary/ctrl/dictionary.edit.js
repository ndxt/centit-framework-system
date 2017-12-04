define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var DictionaryAdd = require('../ctrl/dictionary.add');

  return DictionaryAdd.extend(function () {
		var _self = this;

		// @override
		this.load = function(panel, data) {

			var form = panel.find('form');

			Core.ajax(Config.ContextPath+this.url+data.catalogCode, {
				method: 'get'
			}).then(function(data) {
				_self.data = $.extend(_self.object, data);
				form.form('disableValidation')
					.form('load', data)
					.form('readonly', 'catalogCode')
					.form('focus');

				// 初始化字段描述
				this.table = _self.initPropertyFields(panel, data.fieldDesc);
			});


		};

		// @overload
		this.submit = function(panel, data, closeCallback) {
			var form = panel.find('form');
      var table = panel.find('table#property_desc');

			// 开启校验
			form.form('enableValidation');
			var isValid = form.form('validate') && table.cdatagrid('endEdit');

			if (isValid) {
				form.form('ajax', {
					url: Config.ContextPath + this.url + data.catalogCode,
					method: 'put',
					// 字段描述值需要特殊引入
					data: $.extend({}, this.data, {
						fieldDesc: this.stringifyFieldDesc(panel)
					})
        }).then(function () {
          _self.parent.table.datagrid('reload');
          closeCallback()
        });
			}

			return false;
		};
	});

});
