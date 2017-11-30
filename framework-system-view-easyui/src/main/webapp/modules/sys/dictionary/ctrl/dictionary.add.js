define(function (require) {
  var Config = require('config');
  var Core = require('core/core');
  var Page = require('core/page');


  // 添加数据字典
  var DictionaryAdd = Page.extend(function () {

    var vm = this;

    this.url = 'system/dictionary/';

    // @override
    this.object = {
      // 用户类型
      catalogStyle: 'U',

      // 需要缓存
      needCache: '1',

      // 列表结构
      catalogType: 'L'
    };


    // 将字段描述字符串转换成对象
    this.parseFieldDesc = function (value) {
      // 保存json，格式类似于 {dataValue: {value: '编码', isUse: 'T'}, ...}
      var fieldMap = {};
      try {
        fieldMap = JSON.parse(value);
      } catch (e) {
      }
      finally {
        // 字段默认值
        fieldMap = $.extend(true, {
          dataCode: {value: '编码', isUse: 'T'},
          dataValue: {value: '数值', isUse: 'T'},
          extraCode: {value: '扩展编码', isUse: 'F'},
          extraCode2: {value: '排序', isUse: 'F'},
          dataTag: {value: '数据标记', isUse: 'F'},
          dataDesc: {value: '数据描述', isUse: 'T'}
        }, fieldMap);
      }

      return fieldMap;
    };

    // 添加字段命名列表，顺序分别是：编码、扩展编码、扩展编码2、数据标记、数值、数据描述 ，共6个字段。在明细中按照这个顺序显示。
    //扩展编码2改为排序
    this.initPropertyFields = function (panel, value) {
      // 列描述
      var columns = [[
        {field: 'name', title: '字段', width: '35%'},
        {
          field: 'value',
          title: '描述',
          width: '35%',
          editor: {type: 'checkbox', options: {required: true}}
        },
        {
          field: 'isUse', title: '是否使用', width: '30%',
          align: 'center',
          editor: {type: 'checkbox', options: {on: 'T', off: 'F'}},
          formatter: function (value) {
            return value === 'T' ? '是' : '否'
          }
        }
      ]];

      var fieldMap = vm.parseFieldDesc(value);

      // 字段描述表格展示数据
      var data = {
        "total": 6, "rows": [
          {field: 'dataCode', "name": "编码", "value": fieldMap.dataCode.value, "isUse": fieldMap.dataCode.isUse},
          {field: 'dataValue', "name": "数值", "value": fieldMap.dataValue.value, "isUse": fieldMap.dataValue.isUse},
          {field: 'extraCode', "name": "扩展编码", "value": fieldMap.extraCode.value, "isUse": fieldMap.extraCode.isUse},
          {field: 'extraCode2', "name": "排序", "value": fieldMap.extraCode2.value, "isUse": fieldMap.extraCode2.isUse},
          {field: 'dataTag', "name": "数据标记", "value": fieldMap.dataTag.value, "isUse": fieldMap.dataTag.isUse},
          {
            field: 'dataDesc',
            "name": "数据描述",
            "value": fieldMap.dataDesc.value,
            "isUse": fieldMap.dataDesc.isUse
          }
        ]
      };

      panel.find('table#property_desc')
        .datagrid({
          columns: columns
        })
        .datagrid('loadData', data);
    };

    // 获取字段描述值
    this.stringifyFieldDesc = function (panel) {
      var table = panel.find('table#property_desc');

      var rows = table.datagrid('getData').rows;

      // 数组转换成json对象
      var json = {};
      rows.forEach(function (obj) {
        json[obj.field] = {
          value: obj.value,
          isUse: obj.isUse
        };
      });

      return JSON.stringify(json);
    };

    // @override
    this.load = function (panel) {
      var form = panel.find('form');

      form.form('disableValidation')
        .form('load', this.object)
        .form('addValidation', {
          catalogCode: {
            required: true,
            validType: {
              remote: [Config.ContextPath + 'system/dictionary/notexists/{{catalogCode}}', 'catalogCode']
            }
          }
        })
        .form('focus');

      this.initPropertyFields(panel);
    };

    // @override
    this.submit = function (panel, data, closeCallback) {
      var form = panel.find('form');

      // 开启校验
      form.form('enableValidation');
      var isValid = form.form('validate');

      if (isValid) {
        form.form('ajax', {
          url: Config.ContextPath + this.url,
          method: 'post',
          data: $.extend({}, this.object, {
            fieldDesc: this.stringifyFieldDesc(panel)
          })
        }).then(closeCallback);
      }

      return false;
    };

    // @override
    this.onClose = function (table) {
      table.datagrid('reload');
    };
  });

  return DictionaryAdd;
});
