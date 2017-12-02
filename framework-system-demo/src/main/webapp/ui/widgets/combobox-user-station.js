define(function (require) {
    var $ = require('jquery');
    var Config = require('config');
    var Core = require('core/core');
    var Mustache = require('plugins/mustache.min');

    var queryUrl = Config.ContextPath + 'system/mainframe/userpositions';
    var currentUrl = Config.ContextPath + 'system/mainframe/usercurrposition';
    var setUrl = Config.ContextPath + 'system/mainframe/setuserposition/{{userUnitId}}';

    $.fn.comboboxUserStation = function (options) {
        var target = this;

        $(target).combobox($.extend({}, $.comboboxUserStation, options));

        Core.ajax(currentUrl)
            .then(function (current) {
                $(target).combobox('setValue', current.userUnitId);
            });

        return target;
    };

    $.comboboxUserStation = {
        valueField: 'userUnitId',
        textField: 'name',
        url: queryUrl,
        width: 360,
        height: 18,
        panelWidth: 520,
        clearBtn: false,
        editable: false,
        label: '当前岗位：',
        labelAlign: 'right',

        loadFilter: function (datas) {

            datas.forEach(function (data) {
                var result = [];
                result.push(data.unitName ? data.unitName : '未知机构');
                result.push(data.userRankText ? data.userRankText : '未知职务');
                result.push(data.userStationText ? data.userStationText : '未知岗位');

                data.name = result.join(' - ');
            });

            return datas;
        },

        formatter: function (row) {
            var s = '<span style="font-weight:bold">' + row.unitName + '</span><br/>' +
                '<span style="color:#888; padding: 2px 0; margin-right: 4px;">' + row.userRankText + '</span>' +
                '<span style="color:#888; padding: 2px 0;">' + row.userStationText + '</span>';
            return s;
        },

        onSelect: function (value) {
            Core.ajax(Mustache.render(setUrl, value), {
                method: 'PUT'
            })
        }
    };
});
