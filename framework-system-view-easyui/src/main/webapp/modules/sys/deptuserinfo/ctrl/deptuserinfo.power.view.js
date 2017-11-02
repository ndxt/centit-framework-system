define(function(require) {
  var Config = require('config');
  var Core = require('core/core');
  var Utils = require('core/utils');
  var Page = require('core/page');

	// 查看角色的权限
	var DeptUserInfoPowerView = Page.extend(function() {
		var _self = this;

		// @override
    this.load = function(panel, data) {
      var tree = panel.find('ul');
      _self.data=data;
      Core.ajax(Config.ContextPath + 'system/roleinfo/power/unit/' + data.unitCode, {
        method: 'get'
      }).then(function(data) {
        //_self.data = data;

        var powers = data.map(function(obj) {
          return obj.optCode;
        });

        _createOptInfoTree(tree, powers);
      });
    };
    // 创建选择操作权限树
    var _createOptInfoTree = function(tree, powers) {
      Core.ajax(Config.ContextPath + 'system/optinfo/poweropts?field=id&field=iconCls&field=text&field=optMethods&field=children', {
        method: 'get'}).then(function(data) {
       /* Utils.walkTree(data, function(obj) {
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
        });*/

        tree.tree({
          data: data,
          checkbox:false
        });
      });
    };

	});

	return DeptUserInfoPowerView;
});
