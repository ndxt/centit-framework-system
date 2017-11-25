define(function(require) {
  var Config = require('config');
  var Core = require('core/core');
  var Utils = require('core/utils');
  var Page = require('core/page');

  // 查看角色的权限
  var DeptUserInfoPowerView = Page.extend(function() {
    var _self = this;

    // @override
    this.load = function(panel, data) {
      var tree = panel.find('ul');
      /*Core.ajax(Config.ContextPath + 'system/roleinfo/' + data.userCode, {
        method: 'get'
      }).then(function(data) {
        _self.data = data;
        var powers = data.rolePowers.map(function(obj) {
          return obj.optCode;
        });

        _createOptInfoTree(tree, powers,data.rolePowers);
      });*/
      _createOptInfoTree(tree);
    };
    // 创建选择操作权限树
    var _createOptInfoTree = function(tree) {
      Core.ajax(Config.ContextPath + 'system/optinfo/userpoweropts/'+_self.data.userCode, {
        method: 'get'}).then(function(data) {
        /*var dataMap=new Map();
        Utils.walkTree(data, function(obj) {
          /!*var a = new Map();
                    for (var i=0;i<obj.optMethods.length;i++){
                        for(var t=0;t<rolePower.length;t++){
                            if(obj.optMethods[i].optCode==rolePower[t].optCode){
                                a.put(rolePower[t].optCode,rolePower[t].optScopeCodeSet);
                                break;
                            }
                        }
                    }*!/
          for(var i=0;i<rolePower.length;i++){
            dataMap.put(rolePower[i].optCode,rolePower[i].optScopeCodeSet);
          }

          var isLeaf = !(obj.children && obj.children.length);

          // 非叶子节点不考虑权限
          if (!isLeaf) {
            return;
          }

          // 操作定义
          var optDefs = obj.optMethods;
          if (optDefs) {
            optDefs.forEach(function(def) {
              def.id = def.optCode;
              def.text = def.optName;

              // 选中角色已有的操作定义
              if (powers.indexOf(def.id) > -1) {
                def.checked = true;
              }
            });

            // 将操作定义放到叶子节点下
            obj.children = optDefs;
            obj.state = "closed";
          }
        });

        _self.dataMap = dataMap;*/

        tree.tree({
          data: data
        });
      });
    };
    var Map = function(){
      this._entrys = new Array();

      this.put = function(key, value){
        if (key == null || key == undefined) {
          return;
        }
        var index = this._getIndex(key);
        if (index == -1) {
          var entry = new Object();
          entry.key = key;
          entry.value = value;
          this._entrys[this._entrys.length] = entry;
        }else{
          this._entrys[index].value = value;
        }
      };
      this.get = function(key){
        var index = this._getIndex(key);
        return (index != -1) ? this._entrys[index].value : null;
      };
      this.remove = function(key){
        var index = this._getIndex(key);
        if (index != -1) {
          this._entrys.splice(index, 1);
        }
      };
      this.clear = function(){
        this._entrys.length = 0;;
      };
      this.contains = function(key){
        var index = this._getIndex(key);
        return (index != -1) ? true : false;
      };
      this.getCount = function(){
        return this._entrys.length;
      };
      this.getEntrys =  function(){
        return this._entrys;
      };
      this._getIndex = function(key){
        if (key == null || key == undefined) {
          return -1;
        }
        var _length = this._entrys.length;
        for (var i = 0; i < _length; i++) {
          var entry = this._entrys[i];
          if (entry == null || entry == undefined) {
            continue;
          }
          if (entry.key === key) {//equal
            return i;
          }
        }
        return -1;
      };
    }

  });

  return DeptUserInfoPowerView;
});
