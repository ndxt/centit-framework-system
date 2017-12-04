define(function (require) {
  var Config = require('config');
  var Page = require('core/page');

  return Page.extend(function () {

    // @override
    this.object = {
      isInToolbar: 'Y', 	// 显示
      pageType: 'D',		// DIV方式打开
      optType: 'O'		// 普通业务
    };

    // @override
    this.load = function (panel, data) {
      var form = panel.find('form');

      //
      form.form('disableValidation')
        .form('load', $.extend({}, this.object, {
          // 0 为顶级节点
          preOptId: data ? data.id : " "
        }))
        .form('addValidation', {
          optId: {
            required: true,
            validType: {
              remote: [Config.ContextPath + 'system/optinfo/notexists/{{optId}}', 'optId']
            }
          }
        })
        .form('focus');
    };

    // @override
    this.submit = function (panel, data, closeCallback) {
      var form = panel.find('form');

      // 开启校验
      form.form('enableValidation');
      var isValid = form.form('validate');

      this.newObject = null;
      if (isValid) {
        this.newObject = form.form('value');

        form.form('ajax', {
          url: Config.ContextPath + 'system/optinfo',
          method: 'post'
        }).then(closeCallback);
      }

      return false;
    };

    // @override
    this.onClose = function (table, data) {
      if (!data) {
        return;
      }

      this.parent.currentOpt = data.optId;

      // 展开节点
      table.treegrid('reload', data.pid);

      require('loaders/cache/loader.system').loadAll();
    };
  });

});
