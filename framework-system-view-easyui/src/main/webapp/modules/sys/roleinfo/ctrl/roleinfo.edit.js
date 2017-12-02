define(function (require) {
  var Config = require('config');
  var Core = require('core/core');
  var RoleInfoAdd = require('../ctrl/roleinfo.add');
  var Mustache = require('plugins/mustache.min');

  // 编辑角色信息
  return RoleInfoAdd.extend(function () {
    var _self = this;

    this.renderButton = function(btn, row) {
      return 'F' !== row.roleType;
    };

    this.validateRoleNameWhenEdit = Config.ContextPath + 'system/roleinfo/issysroleunique/{{roleName}}/{{roleCode}}';

    // @override
    this.load = function (panel, data) {
      var form = panel.find('form');

      Core.ajax(Config.ContextPath + 'system/roleinfo/' + data.roleCode, {
        method: 'get'
      }).then(function (data) {

        $('#role_type', panel)
          .textbox('setValue', _self.parent.roleTypeFormatter(data.roleType));

        form.form('disableValidation')
          .form('load', data)
          .form('readonly', 'roleCode')
          .form('addValidation', {
            roleName: {
              required: true,
              validType: {
                remote: [
                  Mustache.render(_self.$findUp('validateRoleNameWhenEdit'), data),
                  'roleName'
                ]
              }
            }
          })
          .form('focus');
      });
    };

    // @override
    this.submit = function (panel, data, closeCallback) {
      var form = panel.find('form');

      // 开启校验
      var isValid = form.form('enableValidation').form('validate');

      if (isValid) {
        form.form('ajax', {
          url: Config.ContextPath + 'system/roleinfo/' + data.roleCode,
          method: 'put',
          data: data
        }).then(function () {
          return require('loaders/cache/loader.system').loadAll()
        }).then(function() {
          _self.parent.table.datagrid('reload');
          closeCallback()
        });
      }
      return false;
    };

    // 从角色信息中获取OptCodes集合
    this.getOptCodes = function (role) {
      var powers = role.rolePowers;

      if (!powers) return [];

      return powers.map(function (obj) {
        return obj.optCode;
      });
    };
  });

});
