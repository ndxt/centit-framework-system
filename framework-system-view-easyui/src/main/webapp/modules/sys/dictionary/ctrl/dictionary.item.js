define(function (require) {
  var Config = require('config');
  var Core = require('core/core');
  var Utils = require('core/utils');
  var DictionaryAdd = require('../ctrl/dictionary.add');
  var DictionaryItemSubAdd = require('./dictionary.item.subadd');
  var DictionaryItemAdd = require('../ctrl/dictionary.item.add');
  var DictionaryItemRemove = require('../ctrl/dictionary.item.remove');
  var FilterDictionay = require('core/filter/filter.dictionary');

  // 数据字典明细
  return DictionaryAdd.extend(function () {
    var _self = this;

    this.injecte([
      new DictionaryItemAdd('dictionary_item_add'),
      new DictionaryItemRemove('dictionary_item_remove'),
      new DictionaryItemSubAdd('dictionary_item_subadd')
    ]);

    // @override
    this.load = function (panel, data) {
      var form = panel.find('form');
      var table = panel.find('table');
      this.table = table;

      Core.ajax(Config.ContextPath + this.url + data.catalogCode, {
        method: 'get'
      }).then(function (data) {
        _self.data = $.extend(_self.object, data);
        _self.type = data.catalogType;

        // 基本信息
        form.form('load', data);

        // 树形结构
        if ('T' === data.catalogType) {
          // 树形明细有额外的按钮
          _createTreeToolbar(panel);
          _createTreeList(table, data);
        }
        // 列表结构
        else {
          _createDataList(table, data);
        }
      });
    };

    // @override
    this.submit = function (panel, data, closeCallback) {
      var form = panel.find('form');
      var table = this.table;

      if (form.form('validate') && (data.catalogType === 'T' ? table.ctreegrid('endEdit') : table.cdatagrid('endEdit'))) {
        var items = [], order = 0;

        // 树形结构
        if ('T' === data.catalogType) {
          var temp = table.treegrid('getData');

          Utils.walkTreeBefore(temp, function (obj, parent) {
            if (parent) {
              obj.extraCode = parent.dataCode;
            }
            items.push(obj);
          })
        }

        // 列表结构
        else {
          items = table.datagrid('getData').rows;
        }
        var t = 0;
        for (var i = 0; i < items.length; i++) {
          for (var j = 0; j < items.length; j++) {
            if (items[i].dataCode === items[j].dataCode) {
              t++;
              if (t > 1) break;
            }
          }
          if (t > 1) break;
          else t = 0;
        }
        if (t > 1) {
          $.messager.confirm("error", '编码不能重复！');
          return false;
        }
        // 给id重新赋值
        items.forEach(function (item) {
          if (item.id) {
            item.id.dataCode = item.dataCode;
          }

          item.dataOrder = order++;

          // 无需传到后台的值
          delete item.children;
        });

        data.dataDictionaries = items;
        data._method = 'PUT';
        Core.ajax(Config.ContextPath + this.url + "/update/" + data.catalogCode, {
          data: data,
          method: 'post'
        }).then(closeCallback);

      }

      return false;
    };

    // 默认列属性
    var DefautColumns = {
      icon: {
        field: 'icon'
      },
      // dataOrder: {
      //     field: 'dataOrder',
      //     title: '排序',
      //     align: 'center',
      //     width: 50,
      //     editor: {type: 'numberbox', options: {required: true}}
      // },
      dataCode: {
        field: 'dataCode',
        title: '编码',
        width: 150,
        editor: {type: 'textbox', options: {required: true}}
      },
      dataValue: {
        field: 'dataValue',
        title: '数值',
        width: 150,
        editor: {type: 'textbox', options: {required: true}}
      },
      dataStyle: {
        field: 'dataStyle',
        title: '类型',
        width: 100,
        formatter: function (value, row, index) {
          return FilterDictionay.convert.apply(this, ['CatalogStyle', value, row, index]);
        }
      },
      extraCode: {
        field: 'extraCode',
        title: '扩展代码',
        width: 150,
        editor: 'text'

      },
      extraCode2: {
        field: 'extraCode2',
        title: '排序',
        width: 150,
        editor: 'text',
        sortable: 'true'
      },
      dataTag: {
        field: 'dataTag',
        title: '标记',
        width: 80,
        editor: 'text'
      },
      dataDesc: {
        field: 'dataDesc',
        title: '数据描述',
        width: 600,
        editor: 'text'
      }
    };

    // 列表列
    var ListColumnNames = [
      //{field: 'dataOrder', require: true, frozen: true},
      {field: 'dataCode', require: true, frozen: true},
      {field: 'dataValue', require: true, frozen: true},
      {field: 'dataStyle', require: true, frozen: true},
      {field: 'extraCode', frozen: true},
      {field: 'extraCode2', require: true, frozen: true},
      {field: 'dataTag', frozen: true},
      {field: 'dataDesc', require: true}];

    // 树列
    var TreeColumnNames = [
      {field: 'icon', require: true, frozen: true},
      {field: 'dataCode', require: true, frozen: true},
      {field: 'dataValue', require: true, frozen: true},
      {field: 'extraCode2', require: true, frozen: true},
      {field: 'dataTag', frozen: true},
      {field: 'dataDesc', require: true}];

    /**
     * 创建表格所需的列属性
     */
    this.createColumns = function (fieldDesc, isTree) {
      // 解析字段描述json
      fieldDesc = _self.parseFieldDesc(fieldDesc);

      var columnNames = isTree ? TreeColumnNames : ListColumnNames;
      var frozenColumns = [[]], columns = [[]];
      var column;

      // 加载需要的列
      columnNames.forEach(function (name) {
        var field = name.field;
        // 默认需要加入 或者在字段定义中选择使用
        if (name.require || fieldDesc[field].isUse === 'T') {
          // 覆盖默认值
          if (fieldDesc[field]) {
            column = $.extend(true, {}, DefautColumns[field], {
              title: fieldDesc[field].value
            });
          }
          else {
            column = DefautColumns[field];
          }

          // 固定列
          if (name.frozen) {
            frozenColumns[0].push(column);
          }
          else {
            columns[0].push(column);
          }

        }
      });

      return {
        columns: columns,
        frozenColumns: frozenColumns
      };
    };

    function _createTreeToolbar(panel) {
      var toolbar = panel.find('.temp-toolbar');
      toolbar.find('a[iconCls="icon-add"]')
        .after($('<a>新增下级</a>').attr({
          iconCls: 'icon-sub-add',
          trigger: 'single',
          target: 'custom',
          rel: 'dictionary_item_subadd'
        }));
    }

    // 创建列表明细
    function _createDataList(table, data) {
      var columnsMap = _self.createColumns(data.fieldDesc);

      // 重新写入排序号（因为有很多数据原来没有排序）
      data.dataDictionaries.forEach(function (item, index) {
        item.dataOrder = index + 1;
      });

      table.cdatagrid({
        // 必须要加此项!!
        controller: _self,

        editable: true,

        columns: columnsMap.columns,

        frozenColumns: columnsMap.frozenColumns

      })
        .datagrid('loadData', data.dataDictionaries);
    }

    // 创建树形明细
    function _createTreeList(table, data) {
      var treeData = Utils.makeTree(data.dataDictionaries, function (parent) {
        return this.extraCode === parent.dataCode;
      });

      // 设定虚拟id和pid
      Utils.walkTreeBefore(treeData, function (obj, parent) {
        obj._id = obj.dataCode + new Date().getTime();
        obj.icon = "";
        if (parent) {
          obj._pid = parent._id;
        }
      });

      var columnsMap = _self.createColumns(data.fieldDesc, true);

      table.ctreegrid({
        controller: _self,
        idField: '_id',
        treeField: 'icon',
        editable: true,
        columns: columnsMap.columns,
        frozenColumns: columnsMap.frozenColumns
      })
        .treegrid('loadData', treeData);
    }
  });

});
