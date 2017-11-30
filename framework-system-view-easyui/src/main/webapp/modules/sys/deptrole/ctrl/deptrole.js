define(function(require) {
  var Config = require('config');
  var RoleController = require('modules/sys/roleinfo/ctrl/roleinfo');
  var Cache = require('core/cache');

	return RoleController.extend(function() {

    var loginUser = Cache.get('loginuser');
    this.queryRoleUrl = Config.ContextPath + "system/roleinfo/unit/" + loginUser.primaryUnit;

	});

});
