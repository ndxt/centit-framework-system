define(function (require) {
  var Config = require('config');
  var Page = require('core/page');

  // 添加数据字典
  return Page.extend(function () {

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
          extraCode2: {value: '扩展编码2', isUse: 'F'},
          dataTag: {value: '数据标记', isUse: 'F'},
          dataDesc: {value: '数据描述', isUse: 'T'}
        }, fieldMap);
      }

      return fieldMap;
    };

    // 添加字段命名列表，顺序分别是：编码、扩展编码、扩展编码2、数据标记、数值、数据描述 ，共6个字段。在明细中按照这个顺序显示。
    this.initPropertyFields = function (panel, value) {
      var checkbox = {
        editor: {
          type: 'checkbox',
          options: {
            on: 'T',
            off: 'F'
          }
        },
        formatter: function (value) {
          return value === 'T' ? '是' : '否'
        }
      };

      var textbox = {
        editor: {
          type: 'textbox',
          options: {
            required: true
          }
        }
      };

      // 列描述
      var columns = [[
        {
          field: 'name',
          title: '字段',
          width: '35%'
        },
        {
          field: 'value',
          title: '描述',
          width: '35%',
          editor: textbox.editor
        },
        {
          field: 'isUse',
          title: '是否使用',
          width: '30%',
          align: 'center',
          editor: checkbox.editor,
          formatter: checkbox.formatter
        }
      ]];

      var fieldMap = vm.parseFieldDesc(value);

      // 字段描述表格展示数据
      var data = [
        {
          field: 'dataCode',
          "name": "dataCode",
          "value": fieldMap.dataCode.value,
          "isUse": 'T'
        },
        {
          field: 'dataValue',
          "name": "dataValue",
          "value": fieldMap.dataValue.value,
          "isUse": 'T'
        },
        {
          field: 'extraCode',
          "name": "extraCode",
          "value": fieldMap.extraCode.value,
          "isUse": fieldMap.extraCode.isUse
        },
        {
          field: 'extraCode2',
          "name": "extraCode2",
          "value": fieldMap.extraCode2.value,
          "isUse": fieldMap.extraCode2.isUse
        },
        {
          field: 'dataTag',
          "name": "dataTag",
          "value": fieldMap.dataTag.value,
          "isUse": fieldMap.dataTag.isUse
        },
        {
          field: 'dataDesc',
          "name": "dataDesc",
          "value": fieldMap.dataDesc.value,
          "isUse": fieldMap.dataDesc.isUse
        }
      ];

      panel.find('table#property_desc')
        .cdatagrid({
          controller: this,
          columns: columns
        })
        .cdatagrid('loadData', data);

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
      var table = panel.find('table#property_desc');

      // 开启校验
      form.form('enableValidation');
      var isValid = form.form('validate') && table.cdatagrid('endEdit');

      if (isValid) {
        form.form('ajax', {
          url: Config.ContextPath + this.url,
          method: 'post',
          data: $.extend({}, this.object, {
            fieldDesc: this.stringifyFieldDesc(panel),
            catalogStyle: this.$findUp('isAdmin') ? 'S' : 'U'
          })
        }).then(function () {
          vm.parent.table.datagrid('reload');
          closeCallback();
        });
      }

      return false;
    };

  });

});
