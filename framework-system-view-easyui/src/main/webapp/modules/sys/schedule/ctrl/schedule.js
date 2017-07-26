define(function (require) {
	require('plugins/es5-array');

    var Config = require('config');
    var Core = require('core/core');
    var Page = require('core/page');
    var Mustache = require('plugins/mustache.min');
    var Dialog = require('centit/centit.dialog');
    
    var TaskAdd = require('./task.add.ctrl');
    var TaskEdit = require('./task.edit.ctrl');

    // 日历
    var Calendar = Page.extend(function () {
        var _self = this;
        
        var TaskAddController = new TaskAdd('taskAddManager');
        var TaskEditController = new TaskEdit('taskEditManager');

        // 获取事件
        this.getEvents = function () {
        	var url = Config.ContextPath + 'system/calendar/list/{{appBean}}/{{beginTime}}/{{endTime}}';
        	url = Mustache.render(url, {
        		appBean: 'taskListManager',
	    		beginTime: this.startTime,
	    		endTime: this.endTime
	    	});
        	
        	Core.ajax(url).then(function(data) {
				var events = (data || []).map(function(obj) {
					return $.extend({
						id: 	obj.taskId,
						title: 	obj.title,
						start: 	obj.startTime,
						end:	obj.endTime
					}, obj);
				});
				
				_self.calendar.fullCalendar( 'removeEventSource', _self.events);
				_self.calendar.fullCalendar( 'addEventSource', events);
				_self.events = events;
        	});
        };
        

        // @override
        this.load = function (panel) {
        	panel.css({
        		'text-align': 'center'
        	});
        	
        	var height = Core.height('.impls') - 30;
			width = height * 1.1;
			
			Core.ajax(Config.ContextPath + 'system/calendar/info/taskListManager')
				.then(function(data) {
					// 获取日程信息
					_self.operator = data;
				
					// 生成日历
					_self.calendar = panel.find('#calendar')
						.width(width)
						.fullCalendar($.extend({}, _self.options));
				});
        };

        /**
         * 日历设置
         */
        this.options = {
            editable: true,

            eventStartEditable: true,

            lang: 'zh-cn',

            businessHours: {
                start: '8:30', // a start time (10am in this example)
                end: '17:30', // an end time (6pm in this example)

                dow: [1, 2, 3, 4, 5]
                // days of week. an array of zero-based day of week integers (0=Sunday)
                // (Monday-Thursday in this example)
            },

            allDaySlot: true,

            header: {
                left: 'month agendaWeek agendaDay',
                center: 'title',
                right: 'today prev,next'
            },

            eventLimit: true,

            viewRender: function (view, element) {
                var startTime = view.start.format('YYYYMMDD');
                var endTime = view.end.format('YYYYMMDD');
                
                _self.startTime = startTime;
                _self.endTime = endTime;

                _self.getEvents();
            },

            selectable: true,
            selectHelper: true,
            
            select: function (start, end, jsEvent, view) {
            	var data = $.extend({}, _self.operator, {
            		planBegintime: start.format('YYYY-MM-DD HH:mm:ss'),
            		planEndtime: end.format('YYYY-MM-DD HH:mm:ss')
            	});
            	
            	Dialog.open({
                    id: 'dialog_calendar',
                    title: _self.operator.optHint,
                    href: _self.operator.optUrl,
                    width: _self.operator.width || 480,
                    height: _self.operator.height || 368,
            		okValue: '添加'
                }, data, TaskAddController);
            },

            eventClick: function (calEvent, jsEvent, view) {
            	var taskId = calEvent.taskId;
            	var data = {};
            	
            	// 只保留字符串和数字，不然提交会报错
            	for (var name in calEvent) {
            		if (typeof calEvent[name] == 'string' || typeof calEvent[name] == 'number') {
            			data[name] = calEvent[name];
            		}
            	}
            	
            	Dialog.open({
                    id: 'dialog_calendar',
                    title: data.title,
                    href: data.optUrl,
                    width: data.width || 480,
                    height: data.height || 368
                }, data, TaskEditController);
            }
        };
    });

    return Calendar;
});