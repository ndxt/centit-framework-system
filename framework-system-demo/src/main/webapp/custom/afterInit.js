define(function (require) {
    var $ = require("jquery");
    require('widgets/combobox-user-station');

    return function () {
        $('<input style="margin-left: 10px;">')
            .appendTo($('#header > span'))
            .comboboxUserStation();
    };
});
