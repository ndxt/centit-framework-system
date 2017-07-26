define(function(require) {
    var Config = require('config');
    var Core = require('core/core');
    var Page = require('core/page');
    var _ = require('lodash');


    var RoleInfoPower = Page.extend(function() {
        var _self = this;

        // @override
        this.load = function(panel, data) {
            var arry = _self.parent.dataMap.get(data.optCode);
            if(!arry)
                arry = new Array();
            var table = panel.find('table');
            table.datagrid({
                data:data.objData,
                onCheck:function(rowIndex,rowData){
                    var dataScopes = $(this).cdatagrid('getChecked').length;
                    if (dataScopes > 0){
                        $('#tree_roleinfo_power').tree('check',data.objTarget);
                    }
                    var i=0;
                    for(var j=0;j<arry.length;j++){
                        if(arry[j] == rowData.optScopeCode)
                            i++;
                    }
                    if(i==0){
                        arry.push(rowData.optScopeCode);
                        _self.parent.dataMap.put(data.optCode,arry);
                    }

                },
                onUncheck:function(rowIndex,rowData){
                    _.remove(arry,function(n){return n==rowData.optScopeCode});
                    _self.parent.dataMap.put(data.optCode,arry);

                }

            });
            $.each(data.objData, function(index, item){
                if(arry.indexOf(item.optScopeCode)>-1){
                    table.datagrid('checkRow', index);
                }
            });
        };
        this.submit=function(){

        };
    });

    return RoleInfoPower;
});