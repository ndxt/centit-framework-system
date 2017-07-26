function TaskListController($scope, Restangular, CentitConstant, TaskListService) {

    var taskId = $scope.calEvent.taskId;

    if (taskId) {
        $scope.object = TaskListService.one(taskId).get().$object;
    }
    else {
        $scope.object = {
            planBegintime: $scope.calEvent.start.format('YYYY-MM-DD HH:mm:ss'),
            planEndtime: $scope.calEvent.end.format('YYYY-MM-DD HH:mm:ss')
        };
    }

    $scope.save = function() {
        if(!taskId) {
            save();
        } else {
            update();
        }
    };

    var callback = function(respData) {
        $scope.$modalInstance.close($scope.object);
    };

    var save = function () {
        TaskListService.post($scope.object).then(callback);
    };

    var update = function() {
        $scope.object._method = 'PUT';
        TaskListService.one(taskId).customPOST($scope.object).then(callback);

    };

    $scope.remove = function() {
        $scope.object._method = 'delete';
        TaskListService.one(taskId).customPOST($scope.object).then(callback);
    };
}

/**
 * 确保压缩发布时参数正确性
 * @type {string[]}
 */
TaskListController.$inject = ['$scope', 'Restangular', 'CentitConstant', 'CalendarService'];
