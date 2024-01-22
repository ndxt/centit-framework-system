define(function(require) {
//	var Core = require('po/po');
	var Page = require('core/page');
	var Config = require('config');
	
	// 新增角色信息
	var DatabaseInfoAdd = Page.extend(function() {
		
		// @override
		this.load = function(panel) {
			var form = panel.find('form');
			
			form.form('load', this.object)
				.form('addValidation', 'databaseName', {
					required: true,
				    validType: {
				    	remote: Config.ContextPath+'system/sys/database/noexists/'
				    }
				})
				.form('disableValidation')
//				.form('readonly', 'databaseUrl')
				.form('focus');
			
			
//			var formUrl=function(){
//				var value=form.form('value');
//	            var hostPort = $.trim(panel.find("#hostPort").textbox('getValue'));
//	            if(null!=hostPort && ""!=hostPort)
//	            if(!/:\d{1,5}$/.test(hostPort)) {
//	                
//	                alert("主机和端口号规则不正确");
//	                return;
//	            }
//
//
//	            var databaseNames =value.databaseNames;
//	            var databaseType = value.databaseType;
//	            var databesrurl = null;
//	            if (databaseType == '1') {
//	                databesrurl = "jdbc:sqlserver://" + hostPort + ";databaseName=" + databaseNames;
//	            }
//	            if (databaseType == '2') {
//	                databesrurl = "jdbc:oracle:thin:@" + hostPort + ":" + databaseNames;
//	            }
//	            if (databaseType == '3') {
//	                databesrurl = "jdbc:db2://" + hostPort + "/" + databaseNames;
//	            }
//	            if (databaseType == '5') {
//	                databesrurl = "jdbc:mysql://" + hostPort + "/" + databaseNames+"?useUnicode=true&characterEncoding=utf8";
//	            }
//	            panel.find('#databaseUrl').textbox('setValue',databesrurl);
//			}
//			
//			panel.find("#databaseNames").textbox({onChange:function () {
//				formUrl();
//	        }});
//			panel.find("#databaseType").combobox({onChange:function () {
//				var hostPort = $.trim(panel.find("#hostPort").textbox('getValue'));
//				var databaseNames = panel.find("#databaseNames").textbox('getValue')
//				if(hostPort!="" && databaseNames!="")
//					{
//						formUrl();
//					}
//	        }});
//			
			
			
		};
		// @override
		this.submit = function(panel, data, closeCallback) {
			var form = panel.find('form');
			
			var isValid = form.form('enableValidation').form('validate');
			
			if (isValid) {
				form.form('ajax', {
					url: Config.ContextPath + 'system/sys/database',
					method: 'post'
				}).then(closeCallback);
			}
			
			return false;
		};
		
		// @override
		this.onClose = function(table, data) {
			table.datagrid('reload');
		};

		this.close = function(panel, data,closeCallback){
            closeCallback();
        }

		this.testConnect = function(panel, data) {
			var form = panel.find('form');

			var isValid = form.form('enableValidation').form('validate');

			if (isValid) {
				form.form('ajax', {
					url: Config.ContextPath + 'system/sys/database/testConnect',
					method: 'get'
				}).then(function (data) {
                    $.messager.alert('成功','连接测试成功！','info');
                });
			}

			return false;
		}
       
	});
	return DatabaseInfoAdd;
});