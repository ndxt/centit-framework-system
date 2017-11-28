define(function (require) {
  var Add = require('./unitinfo.add');

  var UnitInfoAdd = Add.extend(function () {


    // @override
    this.load = function (panel, data) {
      var form = panel.find('form');

      form.form('disableValidation').form('load', $.extend({}, this.object, {
        parentUnit: 0
      })).form('focus');

      if (!data.unitCode) {
        form.form('readonly', 'parentUnit');
      }
    };

  });

  return UnitInfoAdd;
});
