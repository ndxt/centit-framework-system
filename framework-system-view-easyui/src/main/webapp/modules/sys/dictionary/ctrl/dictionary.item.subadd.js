define(function (require) {
  var ItemAdd = require('./dictionary.item.add');

  return ItemAdd.extend(function () {

    // @override
    this.submit = function () {
      var table = this.parent.table;

      if (!table.ctreegrid('endEdit')) {
        return;
      }

      var row = table.treegrid('getSelected');
      var id = new Date().getTime(), newRow;

      if (row) {
        newRow = $.extend({
          _id: id,
          icon: "",
          extraCode: row.dataCode
        }, this.object, {catalogCode: this.parent.data.catalogCode});

        table.treegrid('append', {
          parent: row._id,
          data: [newRow]
        });

        // 开启编辑
        table.ctreegrid('beginEdit', 'dataCode', newRow);
      }
    };
  });

});
