(function () {

    "use strict";

    angular.module('xtApp')

        .controller("UserSettingListCtrl", ["$http", "$scope", "$modal", "UnitInfoService", "xtPagerService", 'xtChartService', 'UserUnitService',
            "CentitConstant", "UserSettingService", "xtAlertService","$rootScope","$timeout",
            function ($http, $scope, $modal, UnitInfoService, xtPagerService, xtChartService, UserUnitService, CentitConstant, UserSettingService, xtAlertService,$rootScope,$timeout) {

                //加载usersetting页面中所有的页面地址，所有地址均有usersetting-page.json文件配置
                $scope.pages = [];
                $http.get(CentitConstant.CTX + '/modules/sys/usersetting/config/usersetting-page.json').success(function (data) {
                    for (var i = 0; i < data.length; i++) {
                        $scope.pages.push(data[i]);
                    }
                });

                $scope.field = ['paramCode', 'paramValue', 'paramName', 'createDate'];
                $scope.filter = {};
                new BaseController($scope, UserSettingService);
                $scope.search();

                $timeout(function(){
                	$scope.themes = xtPagerService.getThemes();;
                	$scope.colors = xtPagerService.getColors();
                	$scope.currentTheme = xtPagerService.getCurrentTheme();
                	$scope.currentColor = xtPagerService.getColor();              	
                },200);
            	//$rootScope.themeColor.curColor = $scope.currentColor;
            	$rootScope.$watch('themeColor',function(value){
            		if(value){
            			$scope.currentColor = value.curColor;           			
            		}
            	});
                $scope.receiveways = [{code: 'I', name: '即时消息', selected: false},
                    {code: 'M', name: '邮件', selected: false},
                    {code: 'P', name: '短信', selected: false}];             
                UserSettingService.one("receiveways").get().then(function (data) {
                    if (data) {
                        $scope.receiveway = data;
                        if (data) {
                            if (data.paramValue == 'null')
                                $scope.userways = [];
                            else if (data.paramValue.length == 1)
                                $scope.userways = [data.paramValue];
                            else
                                $scope.userways = data.paramValue.split(',');

                        }
                        else {
                            $scope.userways = [];
                        }
                        var userways = $scope.userways;
                        for (var i = 0; i < userways.length; i++) {
                            for (var j = 0; j < $scope.receiveways.length; j++) {
                                if ($scope.receiveways[j].code == userways[i])
                                    $scope.receiveways[j].selected = true;
                            }
                        }

                    }
                });


                /**
                 * 变更主题
                 */
                $scope.changeTheme = function (theme, colors) {
                    $scope.colors = colors;
                    $rootScope.themeColor = {
							curColor:$rootScope.themeColor.curColor,
							colors:colors
					};
                    $scope.currentTheme = theme;
                    $scope.Page.theme = theme;
                    if ($scope.theme) {
                        if ($scope.currentColor && $.inArray($scope.currentColor, colors)) {
                            $scope.theme.paramValue = "{theme:'" + $scope.currentTheme + "',color:'" + $scope.currentColor + "'}";
                        }
                        else {
                            $scope.theme.paramValue = "{theme:'" + $scope.currentTheme + "',color:'blue'}";
                            xtPagerService.setColor("blue");
            				xtChartService.refreshTheme("blue");
            				$rootScope.themeColor.curColor = "blue";
                        }
                        $scope.theme.customPOST(null, "themes", {_method: 'PUT'}).then(function (data) {
                        });
                    }
                    else {
                        var newSetting;
                        if ($scope.currentColor && $.inArray($scope.currentColor, colors)) {
                            newSetting = {
                                paramValue: "{theme:'" + theme + "',color:'" + $scope.currentColor + "'}",
                                optId: "themes",
                                paramName: "当前用户主题"
                            };
                        }
                        else {
                            newSetting = {
                                paramValue: "{theme:'" + theme + "',color:'blue'}",
                                optId: "themes",
                                paramName: "当前用户主题"
                            };
                            xtPagerService.setColor("blue");
            				xtChartService.refreshTheme("blue");
            				$rootScope.themeColor.curColor = "blue";
                        }
                        UserSettingService.one("themes").customPOST(newSetting, null, {_method: 'POST'});
                    }
                };
                /**
                 * 变更颜色
                 */
                $scope.changeColor = function (color) {
                    $scope.currentColor = color;
                    $rootScope.themeColor = {
							curColor:color,
							colors:$scope.colors
					};
                    $scope.Page.color = color;
                    if (color) {
                        xtPagerService.setColor(color);
                        xtChartService.refreshTheme(color);
                    }
                };
                /**
                 * 改变接收消息方式
                 */
                $scope.changeReceiveWays = function (obj) {
                    obj.selected = !obj.selected;
                    var userways = '';
                    var ways = $scope.receiveways;
                    for (var i = 0; i < ways.length; i++) {

                        if (ways[i].selected == true) {
                            if (userways.length != 0)
                                userways += ',';
                            userways += ways[i].code;
                        }
                    }
                    if (userways == '')
                        userways = 'null';
                    if ($scope.receiveway) {
                        $scope.receiveway.paramValue = userways;
                        $scope.receiveway.customPOST(null, "receiveways", {_method: 'PUT'}).then(function (data) {
                            xtAlertService.success("接收方式已变成" + userways);
                        });
                    } else {
                        var newSetting;
                        newSetting = {paramValue: userways, optId: "receiveways", paramName: "当前用户接受信息方式"};
                        UserSettingService.one("receiveways").customPOST(newSetting, null, {_method: 'POST'}).then(function (data) {
                            xtAlertService.success("接受方式已变成" + userways);
                            UserSettingService.one("receiveways").get().then(function (data) {
                                $scope.receiveway = data;
                            })
                        });
                        
                    }
                    //$scope.receiveway={paramValue:userways};
                };
            }]);

}).call(this);