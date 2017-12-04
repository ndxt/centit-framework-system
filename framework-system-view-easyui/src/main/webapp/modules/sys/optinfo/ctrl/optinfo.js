define(function (require) {

  var Page = require('core/page');

  var Mustache = require('plugins/mustache.min');

  var OptInfoAdd = require('../ctrl/optinfo.add');
  var OptInfoAdd1 = require('../ctrl/optinfo.add');
  var OptInfoEdit = require('../ctrl/optinfo.edit');
  var OptInfoAll = require('../ctrl/optinfo.all');
  var OptInfoRemove = require('../ctrl/optinfo.remove');

  // 业务信息
  return Page.extend(function () {

    var vm = this;

    this.injecte([
      new OptInfoAdd('optinfo_add'),
      new OptInfoAdd1('optinfo_add1'),
      OptInfoAll,
      new OptInfoEdit('optinfo_edit'),
      new OptInfoAll('optinfo_all'),
      new OptInfoRemove('optinfo_remove')
    ]);

    this.currentOpt = null;

    // @override
    this.load = function (panel) {
      var table = this.table = panel.find('table');

      table.ctreegrid({
        controller: this,

        rowStyler: function (row) {
          // 不在菜单上显示
          if (row && row.isInToolbar === 'N') {
            return {
              'class': 'ban'
            };
          }
        },

        onLoadSuccess: function () {
          var panel = $(this).treegrid('getPanel');
          panel.find('.easyui-linkbutton').linkbutton();

          if (vm.currentOpt) {
            var optId = vm.currentOpt;
            // 确保刷新时一定重新加载子页面
            vm.currentOpt = null;
            return $(this).treegrid('select', optId);
          }

          var root = $(this).treegrid('getRoot');
          if (root) {
            $(this).treegrid('select', root.optId);
          }
        }
      });
    };

    this.Format = {
      // ...不用显示
      Url: function (url) {
        if (!url) return "";
        url = url.replace(/^\s\t*/, '').replace(/\s\t*$/, '');

        if (url === '...') {
          return "";
        }

        return url;
      },

      // 转换为图标
      Icon: function (cls) {
        if (cls) {
          return Mustache.render('<a class="easyui-linkbutton icon" plain="true" iconCls="{{cls}}" style="height: 18px;"></a>', {
            cls: cls
          });
        }

        return "";
      }
    }
  });
});
