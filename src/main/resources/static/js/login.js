var vm = new Vue({
    el:'#rrapp', // 指定Vue实例挂载的元素
    data:{// 包含了Vue实例需要响应式处理的数据。这些数据会在模板中绑定和渲染，并且会在数据变化时更新视图
        showLogin:false,
        username: '',
        password: '',
        captcha: '',
        error: false,
        errorMsg: '',
        errorMsgreg:'',
        src: 'captcha.jpg',
        sxh:'',
        sname:'',
        xzraol:'',
        susername:'',
        spassword:'',
        college:'',
        major:'',
        classinfo:'',
        grade:'',
        zcspassword:'',
        yxyzm:"",
        email:'',
        mmzhshow:false,
        dlshow:true,
        zcshow:false,
        mibaowenti:'',
        mibaowentidaan:'',
    },
    beforeCreate: function(){// 生命周期钩子函数
        if(self != top){
            top.location.href = self.location.href;
        }
    },
    // mounted: function () {
    //     if (self !== top) {
    //         top.location.href = self.location.href;
    //     }
    // },
    methods: {
        zhaohui:function(){
            // if (!vm.user.email.trim()) {
            //     // trim() 方法可以去除字符串两端的空白字符（空格、制表符等）
            //     layer.alert('邮箱不能为空');
            //     vm.user.email = '';
            //     return;
            // }
            // if (!vm.user.susername.trim()) {
            //     // trim() 方法可以去除字符串两端的空白字符（空格、制表符等）
            //     layer.alert('账号不能为空');
            //     vm.user.susername = '';
            //     return;
            // }
            // var data = "zcspassword="+vm.zcspassword+"&susername="+vm.susername+"&spassword="+vm.spassword
            //     +"&email="+vm.email;//拼接成字符串
            var data = "susername="+vm.susername +"&email="+vm.email;//拼接成字符串
            //var data = "email="+$("#email").val()+"&susername="+vm.susername;
            //console.log("datadatadatadata"+JSON.stringify(data))
            $.ajax({
                type: "POST",
                url: baseURL + "sys/zhaohui",
                data: data,
                dataType: "json",
                success: function(r){
                    if(r.code == 0){//注册成功
                        // vm.error = false;
                        // vm.zhuce = false;

                        vm.error = true;
                        vm.errorMsgreg = '找回成功初始化密码已发送至您的邮箱';
                    }else{
                        vm.error = true;
                        vm.errorMsgreg = r.msg;
                    }
                }
            });
        },
        // 通过jQuery选择器$("#email").val()获取邮箱输入框中的值，以及vm.susername获取Vue实例中的用户名。
        // 然后将这些数据拼接成一个字符串data，格式为email=xxx&username=xxx
        getYxyzm: function(){
            var data = "email="+$("#email").val()+"&username="+vm.susername;
            console.log("datadatadatadata"+JSON.stringify(data))
            $.ajax({
                type: "POST",
                url: baseURL + "sys/getYxyzm",
                data: data,
                dataType: "json",
                success: function(r){
                    if(r.code == 0){//登录成功

                        // vm.errorMsgreg = r.msg;
                    }else{
                        vm.error = true;
                        vm.errorMsgreg = r.msg;
                    }
                }
            });
            console.log('aaaaaaaaaaaa'+data) // 使用console.log输出data
        },
        refreshCode: function(){
            // 添加?t=参数来强制浏览器重新加载图片
            // $.now()是jQuery函数，用于获取当前时间戳，确保每次请求的URL都是不同的，以避免浏览器缓存旧的验证码图片
            this.src = "captcha.jpg?t=" + $.now();
        },
        regsave:function(){
            var data = "zcspassword="+vm.zcspassword+"&susername="+vm.susername+"&spassword="+vm.spassword+"&yxyzm="+vm.yxyzm+"&email="+vm.email+"&mibaowenti="+vm.mibaowenti+"&mibaowentidaan="+vm.mibaowentidaan;

            $.ajax({
                type: "POST",
                url: baseURL + "sys/regsave",
                data: data,
                dataType: "json",
                success: function(r){
                    if(r.code == 0){//登录成功

                        vm.error = true;
                        vm.errorMsgreg = '注册成功';
                    }else{
                        vm.error = true;
                        vm.errorMsgreg = r.msg;
                    }
                }
            });
            console.log('aaaaaaaaaaaa'+data)
        },
        fh:function(){
            vm.mmzhshow = false;//隐藏密码找回界面
            vm.zcshow = false;//隐藏注册界面
            vm.dlshow = true;//显示登录界面
        },
        regt: function(){
            vm.mmzhshow = false;
            vm.zcshow = true;//显示注册界面
            vm.dlshow = false;
        },
        mmzh: function(){
            vm.mmzhshow = true;//显示密码找回界面
            vm.zcshow = false;//隐藏注册界面
            vm.dlshow = false;//隐藏登录界面
        },
        login: function () {
            var data = "username="+vm.username+"&password="+vm.password+"&captcha="+vm.captcha;
            $.ajax({
                type: "POST",
                url: baseURL + "sys/login",
                data: data,
                dataType: "json",
                success: function(r){
                    if(r.code == 0){//登录成功
                        localStorage.setItem("token", r.token);// 将服务器返回的 r.token 存储到本地存储 localStorage 中，用于后续的认证
                        parent.location.href ='index.html';//将页面重定向到 index.html
                    }else{
                        vm.error = true;
                        vm.errorMsg = r.msg;
                    }
                }
            });
        }
    }
});
