
define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');

	var RoleInfoAdd = require('../ctrl/roleinfo.add');
	var RoleInfoEdit = require('../ctrl/roleinfo.edit');
	var RoleInfoRemove = require('../ctrl/roleinfo.remove');
	var RoleInfoOperate = require('../ctrl/roleinfo.operate');
	var RoleUser = require('../ctrl/roleinfo.user');

	RoleInfoEdit = new RoleInfoEdit('roleinfo_edit');
	RoleInfoOperate = new RoleInfoOperate('roleinfo_operate');
	RoleUser = new RoleUser('roleinfo_user');



	// 角色信息列表
	var RoleInfo = Page.extend(function() {
		//var RoleUserPanel = $('#roleinfo_sub_layout').layout('panel', 'east');


		this.injecte([
	        new RoleInfoAdd('roleinfo_add'),
	        new RoleInfoRemove('roleinfo_remove'),
	        RoleInfoEdit,
	        RoleInfoOperate,
	        RoleUser
	    ]);

		// @override
		this.load = function(panel) {
		  var vm = this;

		  this.$autoHeight('north', $('.role-info-main', panel));

			panel.find('table').cdatagrid({
				// 必须要加此项!!
				controller: this,

				queryParams: {
					s_isValid: 'T'
				},

				rowStyler: function(index, row) {
					if (row.isValid === 'F') {
						return {'class': 'ban'};
					}
					if (row.roleType === 'F') {
					  return {'class': 'fix'}
          }
 				},

        columns: {
          roleType: {
            formatter: this.roleTypeFormatter
          }
        },

				onSelect: function(index, row) {
          vm.selectRole(panel, row);
				},

        onLoadSuccess: function() {
          var rows = $(this).datagrid('getRows');
          if (rows.length) {
            $(this).datagrid('selectRow', 0)
          } else {
            vm.clearRole(panel)
          }
        }
			})
		};

		this.selectRole = function(panel, row) {
      var RoleUserPanel = $('#roleinfo_layout', panel).layout('panel', 'east');
      RoleUserPanel.data('panel').options.onLoad = function() {
        RoleUser.init(RoleUserPanel, row);
      };
      RoleUserPanel.panel('setTitle', row.roleName + ' 角色用户');
      RoleUserPanel.panel('refresh', Config.ViewContextPath + 'modules/sys/roleinfo/roleinfo-user.html');
    };

    this.clearRole = function(panel) {
      var RoleUserPanel = $('#roleinfo_layout', panel).layout('panel', 'east');
      RoleUserPanel.data('panel').options.onLoad = $.noop;
      RoleUserPanel.panel('clear');
    };

    this.roleTypeFormatter = function(value) {
      var types = {
        G: '全局角色',
        P: '公共角色',
        F: '固定角色'
      };

      return types[value] ? types[value] : value;
    };
	});

	return RoleInfo;
});
