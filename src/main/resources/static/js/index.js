//生成菜单
var menuItem = Vue.extend({
	name: 'menu-item',
	props:{item:{},index:0},
	template:[
		'<li :class="{active: (item.type===0 && index === 0)}">',
		'<a v-if="item.type === 0" href="javascript:;">',
		'<i v-if="item.icon != null" :class="item.icon"></i>',
		'<span>{{item.name}}</span>',
		'<i class="fa fa-angle-left pull-right"></i>',
		'</a>',
		'<ul v-if="item.type === 0" class="treeview-menu">',
		'<menu-item :item="item" :index="index" v-for="(item, index) in item.list"></menu-item>',
		'</ul>',
		'<a v-if="item.type === 1" :href="\'#\'+item.url">' +
		'<i v-if="item.icon != null" :class="item.icon"></i>' +
		'<i v-else class="fa fa-circle-o"></i> {{item.name}}' +
		'</a>',
		'</li>'
	].join('')
});

//iframe自适应
$(window).on('resize', function() {
	var $content = $('.content');
	$content.height($(this).height() - 120);
	$content.find('iframe').each(function() {
		$(this).height($content.height());
	});
}).resize();

//注册菜单组件
Vue.component('menuItem',menuItem);

// var vm = new Vue({
// 	el: '#rrapp',
// 	data: {
// 		password: '',
// 		newPassword: ''
// 	},
// 	methods: {
// 		// 验证密码输入格式
// 		validatePasswordInput(field) {
// 			let inputValue = this[field];
// 			// 使用正则表达式验证输入内容，只允许6到15位的数字或字母
// 			this[field] = inputValue.replace(/[^\da-zA-Z]/g, ''); // 只保留数字和字母
// 			if (this[field].length > 15) {
// 				this[field] = this[field].slice(0, 15); // 超过15位则截断
// 			}
// 			if (this[field].length < 6) {
// 				this[field] = ''; // 少于6位则清空输入
// 			}
// 		}
// 	}
// });

var vm = new Vue({
	el:'#rrapp',
	data:{
		user:{},
		lj:'',
		menuList:{},
		main:"main.html",
		password:'',
		newPassword:'',
		navTitle:"欢迎页",
		password: '',
		newPassword: '',
		newDatas:new Date()
	},
	methods: {
		getMenuList: function () {
			$.getJSON(baseURL + "sys/menu/nav", function(r){
				vm.menuList = r.menuList;
				window.permissions = r.permissions;
			});
		},
		getUser: function(){
			$.getJSON(baseURL + "sys/user/info", function(r){
				vm.user = r.user;
				vm.lj = r.lj;
			});
		},

		updatePassword: function(){
			layer.open({
				type: 1,
				skin: 'layui-layer-molv',
				title: "修改密码",
				area: ['550px', '270px'],
				shadeClose: false,
				content: jQuery("#passwordLayer"),
				btn: ['修改','取消'],
				btn1: function (index) {
					//在弹窗确认修改时，再次检查密码长度
					if (vm.password.length < 6 || vm.newPassword.length < 6) {
						layer.alert('密码不能少于6位，请重新输入。');
						// 清空密码框
						vm.password = '';
						vm.newPassword = '';
						return;
					}
					var data = "password="+vm.password+"&newPassword="+vm.newPassword;
					$.ajax({
						type: "POST",
						url: baseURL + "sys/user/password",
						data: data,
						dataType: "json",
						success: function(r){
							if(r.code == 0){
								layer.close(index);
								layer.alert('修改成功', function(){
									location.reload();
								});
							}else{
								layer.alert(r.msg);
							}
						}
					});
				}
			});
		},
		logout: function () {
			//删除本地token
			localStorage.removeItem("token");
			//跳转到登录页面
			location.href = baseURL + 'login.html';
		},
		donate: function () {
			layer.open({
				type: 2,
				title: false,
				area: ['806px', '467px'],
				closeBtn: 1,
				shadeClose: false,
				content: ['http://cdn.renren.io/donate.jpg', 'no']
			});
		}
	},
	created: function(){
		this.getMenuList();
		this.getUser();
	},
	updated: function(){
		//路由
		var router = new Router();
		routerList(router, vm.menuList);
		router.start();
	}
});



function routerList(router, menuList){
	for(var key in menuList){
		var menu = menuList[key];
		if(menu.type == 0){
			routerList(router, menu.list);
		}else if(menu.type == 1){
			router.add('#'+menu.url, function() {
				var url = window.location.hash;

				//替换iframe的url
				vm.main = url.replace('#', '');

				//导航菜单展开
				$(".treeview-menu li").removeClass("active");
				$(".sidebar-menu li").removeClass("active");
				$("a[href='"+url+"']").parents("li").addClass("active");

				vm.navTitle = $("a[href='"+url+"']").text();
			});
		}
	}
}

