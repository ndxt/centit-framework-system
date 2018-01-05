define(function (require) {
  var Config = require('config');

  var Page = require('core/page');
  var Utils = require('core/utils');
  var Core = require('core/core');
  var Mustache = require('plugins/mustache.min');

  // 机构添加用户
  var UserInfoRoleAdd = Page.extend(function () {
    this.renderButton = function () {
      return 'F' !== this.parent.data.roleType;
    };

    this.initUnitCombotree = function (input) {
      input
        .attr('target', 'unit')
        .combotree({
          target: 'unit'
        });
    };

    // @override
    this.object = {
      obtainDate: Utils.formateDate(new Date(), 'yyyy-MM-dd')
    };

    // @override
    this.load = function (panel) {

      this.$findUp('initUnitCombotree')($('input[name=unitCode]', panel));

      this.refresh = false;

      var roleInfo = this.parent.data;

      var data = this.data = $.extend({}, this.object, {
        roleCode: roleInfo.roleCode,
        roleName: roleInfo.roleName
      });

      panel.find('form').form('disableValidation')
        .form('load', data);
    };

    // @override
    this.submit = function (panel, data, closeCallback) {
      var form = panel.find('form');
      var _self = this;
      form.form('enableValidation');
      var isValid = form.form('validate');

      if (isValid) {
        form.form('ajax', {
          url: Config.ContextPath + 'system/unitrole',
          data: data
        }).then(function () {
          return require('loaders/cache/loader.system').loadAll()
        }).then(function () {
          _self.refresh = true;

          var roleInfoUrl = Mustache.render(_self.parent.parent.roleInfoUsersInheritedUrl, _self.parent.parent.data);
          return Core.ajax(roleInfoUrl, {
            method: 'get'
          })
        }).then(function (data) {
          _self.parent.parent.panel.find('#roleinfoUserTable').datagrid('loadData', data);
          closeCallback();
        });
      }

      return false;
    };

    // @override
    this.onClose = function (table) {
      console.log(arguments);
      if (this.refresh)
        table.datagrid('reload');


    };
  });

  return UserInfoRoleAdd;
});
