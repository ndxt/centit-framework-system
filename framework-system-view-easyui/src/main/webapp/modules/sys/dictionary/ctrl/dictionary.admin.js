define(function (require) {
  var Dictionary = require('./dictionary');

  // 数据字典列表
  return Dictionary.extend(function () {
    this.isAdmin = true;
  });
});
