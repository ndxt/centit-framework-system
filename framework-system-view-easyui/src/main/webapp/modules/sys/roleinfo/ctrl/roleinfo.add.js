define(function (require) {
  var Config = require('config');
  var Page = require('core/page');

  // 新增角色信息
  return Page.extend(function () {
    var vm = this;

    // @override
    this.object = {
      isValid: 'T'
    };

    this.validateRoleNameWhenAdd = Config.ContextPath + 'system/roleinfo/issysroleunique/{{roleName}}';

    // @override
    this.load = function (panel) {
      var form = panel.find('form');

      var doDeptRoleAdd = this.$findUp('doDeptRoleAdd');
      if (doDeptRoleAdd) {
        doDeptRoleAdd(this)
      }

      form.form('disableValidation')
        .form('load', this.object)
        .form('addValidation', {
          roleName: {
            required: true,
            validType: {
              remote: [
                this.$findUp('validateRoleNameWhenAdd'),
                'roleName'
              ]
            }
          }
        })
        .form('focus');
    };

    // @override
    this.submit = function (panel, data, closeCallback) {
      var form = panel.find('form');

      var isValid = form.form('enableValidation').form('validate');

      if (isValid) {
        form.form('ajax', {
          url: Config.ContextPath + 'system/roleinfo',
          method: 'post',
          data: this.object
        }).then(function () {
          return require('loaders/cache/loader.system').loadAll()
        }).then(function () {
          vm.parent.table.datagrid('reload');
          closeCallback();
        });
      }

      return false;
    };

  });

});
