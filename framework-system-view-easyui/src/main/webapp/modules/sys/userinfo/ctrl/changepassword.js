define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');
	function test1(){
		alert(888);
	}
	// 当前登录用户
	var ChangePassword = Page.extend(function() {
		
		// @override
		this.load = function(panel) {
			var _self=this;
			// 保存原始信息
			
			var form = panel.find('form');
			Core.ajax(Config.ContextPath+'system/userinfo/current', {
				data: {
				},
				method: 'get'
			}).then(function(data) {
				_self.data = data;
				userinfo={userName:data.userName,loginName:data.loginName}
				form.form('disableValidation').form('load', userinfo)
					.form('focus');
			});
		};
		
		
		// @override
		this.submit = function(panel, data, closeCallback) {
			var form = panel.find('form');
			
			form.form('enableValidation');
			var isValid = form.form('validate');
			var value=form.form('value');
			var bo=false;
			if(isValid) {
			$.ajax({  
		          type:"get", 
		          async:false,
		          url:Config.ContextPath+'system/userinfo/canchange/' + data.userCode+'/'+value.oldpassword, 
		          dataType:"json", 
		          data:value,
		          success:function(result){
		        	  if(result.data)
			        	 { 
		        		  if(value.newpassword!=value.newpasswordconf)
		      				{
		      					$.messager.alert('错误','两次输入新密码不一致！','error');
		      					bo=false;
		      				}
		        		  
		        		  else{
		        			  form.form('ajax', {
			      					url: Config.ContextPath+'system/userinfo/change/'+data.userCode,
			      					method: 'put',
			      					data:{password:value.oldpassword,newPassword:value.newpassword}
			      				
			      				}).then(function(){
			      					 $.messager.alert('提示','密码修改成功！','info');
			      					closeCallback();
			      				});
		        		  }
		      				
			        	 }else {
			        		 $.messager.alert('错误','旧密码不正确！','error');
			        		 bo=false;
			            }
		          	} 
		          });
			}
			return bo;
		};
		
		// @override
		this.onClose = function() {
		};
	});
	
	return ChangePassword;
});