(function () {

    "use strict";

    angular.module('xtApp')

        .controller("CalendarListCtrl", ["$scope", "$modal", "CentitConstant", "CalendarService", function ($scope, $modal, CentitConstant, CalendarService) {

            // 获取应用名称
            var getImplNames = function() {
                return $scope.implNames.filter(function(o) {
                    return $scope.implObject[o.key];
                }).map(function(o) {
                    return o.key;
                }).join(',');

            };

            // 获取事件
            var getEvents = function() {
                CalendarService.one('').get({
                    s_beginTime: $scope.startTime,
                    s_endTime: $scope.endTime,
                    s_implNames: getImplNames()
                }).then(function(data) {
                    // 清空事件和可新增事件列表
                    $scope.events.length = 0;
                    $scope.newOperator.events.length = 0;

                    data.forEach(function(dataO) {

                        if (dataO.newOperator) {
                            $scope.newOperator.newOperator = true;
                            $scope.newOperator.events.push(dataO);
                        }

                        // 读取events
                        dataO.calendarEnties && dataO.calendarEnties.forEach(function(o) {
                            $scope.events.push(o);
                        });
                    })
                });
            };
            $scope.getEvents = getEvents;

            $scope.implNames = [];
            $scope.implObject = {};
            $scope.startTime = "";
            $scope.endTime = "";

            // 新增操作
            $scope.newOperator = {
                newOperator: false,
                events: []
            };


            // 获取日程类型
            CalendarService.one('implNames').get().then(function(data) {
                $scope.implNames = data;

                angular.forEach(data, function(o) {
                    $scope.implObject[o.key] = true;
                });

                getEvents();
            });


            // 日历配置
            $scope.uiConfig = {
                calendar:{
                    editable: true,
                    eventStartEditable: true,

                    lang: 'zh-cn',

                    businessHours: {
                        start: '9:00', // a start time (10am in this example)
                        end: '17:30', // an end time (6pm in this example)

                        dow: [ 1, 2, 3, 4, 5 ]
                        // days of week. an array of zero-based day of week integers (0=Sunday)
                        // (Monday-Thursday in this example)
                    },

                    allDaySlot: true,

                    header:{
                        left: 'month agendaWeek agendaDay',
                        center: 'title',
                        right: 'today prev,next'
                    },

                    eventLimit: true,

                    viewRender: function(view, element) {
                        $scope.startTime = view.start.format('YYYY-MM-DD HH:mm:ss');
                        $scope.endTime = view.end.format('YYYY-MM-DD HH:mm:ss');

                        getEvents();
                    },

                    selectable: true,
                    selectHelper: true,
                    select: function(start, end, jsEvent, view) {

                        // 没有可新增事件
                        if (!$scope.newOperator.newOperator) return;

                        if ($scope.newOperator.events.length == 1) {
                            var event = $scope.newOperator.events[0];
                        }

                        var modalInstance = $modal.open({
                            // 弹出窗口内容所对应的页面
                            templateUrl: CentitConstant.CTX + '/modules/sys/schedule/schedule-modal.jsp',

                            // 弹出窗口页面所对应的控制器
                            controller: 'CalendarModalCtrl',

                            size: 'lg',

                            // 传给窗口控制器的参数
                            resolve: {
                                calEvent: function() {
                                    return {
                                        optUrl: event.newOptUrl,
                                        title: event.newOptHint,
                                        start: start,
                                        end: end
                                    };
                                }
                            }
                        });

                        // 关闭窗口时的回调方法
                        modalInstance.result.then(function (result) {
                            $scope.getEvents();
                        });
                    },

                    eventClick: function(calEvent, jsEvent, view) {

                        var modalInstance = $modal.open({
                            // 弹出窗口内容所对应的页面
                            templateUrl: CentitConstant.CTX + '/modules/sys/schedule/schedule-modal.jsp',

                            // 弹出窗口页面所对应的控制器
                            controller: 'CalendarModalCtrl',

                            size: 'lg',

                            // 传给窗口控制器的参数
                            resolve: {
                                calEvent: function() {
                                    return calEvent;
                                }
                            }
                        });

                        // 关闭窗口时的回调方法
                        modalInstance.result.then(function (result) {
                            $scope.getEvents();
                        });
                    }
                }

            };

            $scope.events = [];
            $scope.eventSources =[$scope.events];


        }])

        .controller("CalendarModalCtrl", ["$scope", "$modalInstance", "calEvent",
            function ($scope, $modalInstance, calEvent) {

                // 打开控制器所传入的参数
                $scope.calEvent = calEvent;

                $scope.$modalInstance = $modalInstance;

                // 取消
                $scope.cancel = function () {
                    $modalInstance.dismiss();
                }
            }])
}).call(this);