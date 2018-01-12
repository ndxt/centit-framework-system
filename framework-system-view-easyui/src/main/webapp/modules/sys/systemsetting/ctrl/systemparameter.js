define(function (require) {
  var Config = require('config');
  var Page = require('core/page');
  var Core = require('core/core');
  var $ = require('jquery');
  require('myuploader');
  // require('upload');


  return Page.extend(function () {
    var _self = this;

    // @override
    this.load = function (panel, data) {
      this.data = data;

      $('#fb').filebox({
        buttonText: 'Choose File',
        buttonAlign: 'left'
      })

        // panel.find('#systemIcon').uploader({
        //   UploaderPath: Config.ContextPath+'system/systemsetting/uploadico',
        // });

      // requirejs(['upload'], function($) {

      //   $(document).ready(function () {
      //     $("#demo1").AjaxFileUpload({
      //       action: ContextPath + 'service/file/upload'
      //     });
      //   });
      // });



        panel.find('form').on('submit', function() {

      })

    };

    this.submit = function (panel, data, closeCallback) {
      var form = panel.find('form');
      var table = this.table;

      if (form.form('validate') && table.cdatagrid('endEdit')) {
        var formData = form.form('value');
        var optDefs = table.datagrid('getData').rows;

        $.extend(data, formData);
        data.optDefs = optDefs;
        data._method = 'PUT';

        Core.ajax(Config.ContextPath + 'system/usersetting/' + data.paramCode, {
          data: data,
          method: 'post'
        }).then(closeCallback);
      }
    };
  });
});

