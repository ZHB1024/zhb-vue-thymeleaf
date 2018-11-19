
function goto_register() {
	$("#register-username").val("");
	$("#register-password").val("");
	$("#register-repassword").val("");
	$("#register-code").val("");
	$("#tab-2").prop("checked", true);
}

function goto_login() {
	$("#login-username").val("");
	$("#login-password").val("");
	$("#tab-1").prop("checked", true);
}

function goto_forget() {
	$("#forget-username").val("");
	$("#forget-password").val("");
	$("#forget-code").val("");
	$("#tab-3").prop("checked", true);
}

//登录
function login() {
	var username = $("#login-username").val(), 
		password = $("#login-password").val(), 
		flag = false;
	//判断用户名密码是否为空
	if (username == "") {
		myVue.$Message.error({
            content: "用户名不能为空",
            duration: 3,
            closable: true
        });
		return false;
	}
	if (password == "") {
		myVue.$Message.error({
            content: "密码不能为空",
            duration: 3,
            closable: true
        });
		return false;
	}
	//用户名只能是15位以下的字母或数字
	var regExp = new RegExp("^[a-zA-Z0-9_]{1,15}$");
	if (!regExp.test(username)) {
		myVue.$Message.error({
            content: "用户名必须为15位以下的字母或数字",
            duration: 3,
            closable: true
        });
		return false;
	}

	//调用后台登录验证
	let param = new URLSearchParams(); 
	param.append("userName",username); 
    param.append("password",password); 
	axios.post('/logincontroller/login/api', param)
		  .then(function (response) {
			  if(response.data.flag){
				var redirectUrl = $("input[name='redirectUrl']").val().trim();
				if(null == redirectUrl || "" == redirectUrl){
					window.location.href='/htgl/indexcontroller/index';
			    }else{
			    	window.location.href= redirectUrl;
			    }
			  }else{
				  myVue.$Message.error({
                  content: response.data.errorMessages,
                  duration: 3,
                  closable: true
              });
			  }
		  })
}

//发送验证码
function sendCode(type){
	var registerEmail = $("#register-email").val();
	var forgetEmail = $("#forget-email").val();
	var userName = $("#forget-username").val();
	
	if(0 == type){
		if(null == registerEmail || registerEmail == ''){
			myVue.$Message.error({
	            content: "邮箱不能为空",
	            duration: 3,
	            closable: true
	        });
			return false;
		}
	}
	if(1 == type){
		if(null == userName || userName == ''){
			myVue.$Message.error({
	            content: "用户名不能为空",
	            duration: 3,
	            closable: true
	        });
			return false;
		}
		if(null == forgetEmail || forgetEmail == ''){
			myVue.$Message.error({
	            content: "邮箱不能为空",
	            duration: 3,
	            closable: true
	        });
			return false;
		}
	}
	let param = new URLSearchParams(); 
	
	if(type == 1){
		param.append("userName",userName);
		param.append("type",1);
		param.append("email",forgetEmail);
	}else{
		param.append("type",0);
		param.append("email",registerEmail);
	}
	
	//等待动画，用户不可编辑
	myVue.$Spin.show();
	
	axios.post('/logincontroller/sendverificationcode/api', param)
		  .then(function (response) {
			  //结束动画
			  myVue.$Spin.hide();
			  if(response.data.flag){
				  myVue.$Message.success({
	                  content: "发送成功，请登录邮箱查收",
	                  duration: 3,
	                  closable: true
	              });
			  }else{
				  myVue.$Message.error({
					  content: response.data.errorMessages,
					  duration: 3,
					  closable: true
				  });
			  }
		  })
}

//注册
function register() {
	var username = $("#register-username").val(), 
		password = $("#register-password").val(), 
		repassword = $("#register-repassword").val(), 
		email = $("#register-email").val(), 
		code = $("#register-code").val();
	//判断用户名密码是否为空
	if (username == "") {
		myVue.$Message.error({
            content: "用户名不能为空",
            duration: 3,
            closable: true
        });
		return false;
	}
	if (password == "") {
		myVue.$Message.error({
            content: "密码不能为空",
            duration: 3,
            closable: true
        });
		return false;
	} else {
		if (password != repassword) {
			myVue.$Message.error({
	            content: "两次输入的密码不一致",
	            duration: 3,
	            closable: true
	        });
			return false;
		}
	}
	//用户名只能是15位以下的字母或数字
	var regExp = new RegExp("^[a-zA-Z0-9_]{1,15}$");
	if (!regExp.test(username)) {
		myVue.$Message.error({
            content: "用户名必须为15位以下的字母或数字",
            duration: 3,
            closable: true
        });
		return false;
	}

	if (code == "") {
		myVue.$Message.error({
            content: "注册码不能为空",
            duration: 3,
            closable: true
        });
		return false;
	}
	
	//调用后台注册验证
	let param = new URLSearchParams(); 
	param.append("userName",username); 
	param.append("password",password); 
	param.append("confirmPassword",repassword); 
	param.append("email",email); 
	param.append("code",code); 
	axios.post('/logincontroller/register/api', param)
		  .then(function (response) {
			  if(response.data.flag){
				myVue.$Message.success({
					content: "注册成功,请登录",
					duration: 3,
					closable: true
				});
				window.location.href='/logincontroller/tologin';
			  }else{
				  myVue.$Message.error({
                  content: response.data.errorMessages,
                  duration: 3,
                  closable: true
              });
			  }
		  })
	
	/*spop({
		template : '<h4 class="spop-title">注册成功</h4>即将于3秒后返回登录',
		position : 'top-center',
		style : 'success',
		autoclose : 3000,
		onOpen : function() {
			var second = 2;
			var showPop = setInterval(function() {
				if (second == 0) {
					clearInterval(showPop);
				}
				$('.spop-body').html(
						'<h4 class="spop-title">注册成功</h4>即将于' + second
								+ '秒后返回登录');
				second--;
			}, 1000);
		},
		onClose : function() {
			goto_login();
		}
	});*/
}

//重置密码
function forget() {
	var username = $("#forget-username").val(),
		password = $("#forget-password").val(), 
		repassword = $("#forget-repassword").val(), 
		email = $("#forget-email").val(), 
		code = $("#forget-code").val();
	//判断用户名密码是否为空
	if (username == "") {
		myVue.$Message.error({
            content: "用户名不能为空",
            duration: 3,
            closable: true
        });
		return false;
	}
	if (email == "") {
		myVue.$Message.error({
			content: "邮箱不能为空",
			duration: 3,
			closable: true
		});
		return false;
	}
	if (password == "") {
		myVue.$Message.error({
            content: "密码不能为空",
            duration: 3,
            closable: true
        });
		return false;
	}
	//用户名只能是15位以下的字母或数字
	var regExp = new RegExp("^[a-zA-Z0-9_]{1,15}$");
	if (!regExp.test(username)) {
		myVue.$Message.error({
			content: "用户名必须为15位以下的字母或数字",
			duration: 3,
			closable: true
		});
		return false;
	}

	if (code == '') {
		myVue.$Message.error({
			content: "请输入验证码",
			duration: 3,
			closable: true
		});
		return false;
	}

	//调用后台
	let param = new URLSearchParams(); 
	param.append("userName",username); 
	param.append("password",password); 
	param.append("confirmPassword",repassword); 
	param.append("email",email); 
	param.append("code",code); 
	axios.post('/logincontroller/updatepassword/api', param)
		  .then(function (response) {
			  if(response.data.flag){
				myVue.$Message.success({
					content: "修改成功,请登录",
					duration: 3,
					closable: true
				});
				window.location.href='/logincontroller/tologin';
			  }else{
				  myVue.$Message.error({
                  content: response.data.errorMessages,
                  duration: 3,
                  closable: true
              });
			  }
		  })
		  
	/*spop({
		template : '<h4 class="spop-title">重置密码成功</h4>即将于3秒后返回登录',
		position : 'top-center',
		style : 'success',
		autoclose : 3000,
		onOpen : function() {
			var second = 2;
			var showPop = setInterval(function() {
				if (second == 0) {
					clearInterval(showPop);
				}
				$('.spop-body').html(
						'<h4 class="spop-title">重置密码成功</h4>即将于' + second
								+ '秒后返回登录');
				second--;
			}, 1000);
		},
		onClose : function() {
			goto_login();
		}
	});
	return false;*/
}