// 页面加载时调用加载表格数据的函数
$(function() {
	loadGridData();
});

function loadGridData() {
	var token = localStorage.getItem("token");//验证身份
	console.log("tokentokentokentoken" + token)
	$.ajaxSettings.async = false;
	$.post("/jeefast/sysLiaotian/aaaa?aaaa="+token,function(data){//post请求
		console.log("datadatadata"+JSON.stringify(data))
		user = data.sysuser.realname;
		uid = data.sysuser.userId;
		username = data.sysuser.username;
		usertype=data.sysuser.type;
	});

	// 设置表格加载的 URL 和其他参数
	var gridOptions = {
		url: baseURL + 'sysXinwen/list',
		datatype: "json",
		colModel: [
			{ label: '活动标题', name: 'title', index: "title", width: 150,
				formatter: function(cellvalue, options, rowObject) {
					// return '<a href="#" onclick="window.open(\'' + baseURL + '/xinweninfo.html?id=' + rowObject.id + '\', \'_blank\'); return false;">' + cellvalue + '</a>';
					return '<a href="#" onclick="info(\'' + rowObject.id + '\'); return false;">' + cellvalue + '</a>';
				}
			},
			{ label: '发布负责人', name: 'zuozhe', index: "zuozhe", width: 40 },
			{ label: '活动时间', name: 'hdsj', index: "hdsj", width: 75},  // 设置该列可排序
			{ label: '学分类型', name: 'xflxname', index: "xflxname", width: 30 },
			{ label: '活动分值', name: 'hdfz', index: "hdfz", width: 30 },
			{ label: '活动地点', name: 'hddd', index: "hddd", width: 75 },
			{ label: '审批状态', name: 'spzt', index: 'spzt', width: 80,
				formatter: function(value, options, row){
					if(value == "1"){
						return '<span class="label label-success">审核通过</span>';
					} else if(value == "2"){
						return '<span class="label label-danger">驳回</span>';
					} else {
						return '<span class="label label-warning">待审批</span>';
					}
				}
			},
		],
		viewrecords: true,
		height: 385,
		rowNum: 10,
		rowList : [10,30,50],
		rownumbers: true,
		rownumWidth: 25,
		autowidth: true,
		multiselect: true,
		pager: "#jqGridPager",
		jsonReader : {
			root: "page.records",
			page: "page.current",
			total: "page.pages",
			records: "page.total"
		},
		prmNames : {
			page: "page",
			rows: "limit",
			order: "order"
		},
		gridComplete: function() {
			// 隐藏grid底部滚动条
			$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
		}
	};

	// 根据用户类型设置不同的查询条件或者过滤规则
	if (usertype == 2) {
		// 只显示审批状态为 "审核通过" 的记录
		gridOptions.postData = {
			// 'title': '体育' // 只查询审核通过的记录
			'spzt': '1'
			// 'xflxname': '美育'
		};
		// 加载表格数据
	}
	$("#jqGrid").jqGrid(gridOptions);
}

$().ready(function() {
	$('.summernote').summernote({
		height: 250,//高度
		tabsize: 2,//页面上的summernote编辑框的个数
		lang: 'zh-CN',//语言
		callbacks:{//回调函数，重写onImageUpload方法
			onImageUpload: function(files, editor, welEditable) {
				vm.sendFile(this,files[0],editor,welEditable);
			}
		}
	});
});


function info(id) {
	// 这里可以根据具体需求进行修改
	// 示例中直接打开新窗口，你可以在这里加入你的逻辑
	$.ajaxSettings.async = false;
	vm.getXinwen(id);
	$.ajaxSettings.async = true;
	console.log("spztspztspztspztspzt"+JSON.stringify(vm.xinwen.spzt));
	// if(vm.xinwen.spzt == null || vm.xinwen.spzt == undefined || vm.xinwen.spzt != '1') {
	// 	alert("待审批或者驳回状态无法报名");
	// 	return;
	// }
	window.open(baseURL + "/xinweninfo.html?id="+id,"_blank");
}

//菜单树
var menu_ztree;
var menu_setting = {
	data: {
		simpleData: {
			enable: true,
			idKey: "menuId",
			pIdKey: "parentId",
			rootPId: -1
		},
		key: {
			url:"nourl"
		}
	},
	check:{
		enable:true,
		nocheckInherit:true
	}
};

//部门结构树
var dept_ztree;
var dept_setting = {
	data: {
		simpleData: {
			enable: true,
			idKey: "deptId",
			pIdKey: "parentId",
			rootPId: -1
		},
		key: {
			url:"nourl"
		}
	}
};

//数据树
var data_ztree;
var data_setting = {
	data: {
		simpleData: {
			enable: true,
			idKey: "deptId",
			pIdKey: "parentId",
			rootPId: -1
		},
		key: {
			url:"nourl"
		}
	},
	check:{
		enable:true,
		nocheckInherit:true,
		chkboxType:{ "Y" : "", "N" : "" }
	}
};

var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{
			xinwenName: null
		},
		importModle: true,
		showList: true,
		showDelect: true,
		title:null,
		equipmentList:{},
		allFiles: [],
		xflxs: [],
		jys:[{"id":"1","name":"是"},{"id":"2","name":"否"}],
		sfzsyms:[{"id":"1","name":"是"},{"id":"2","name":"否"},{"id":"3","name":"未知"}],
		hdlbs:[],
		xinwen:{
			id:'',
			qcmc:'',
			bh:'',
			username:'',
			createdate:'',
			enddate:'',
			equipmentIdList:[],
			equipmentId:'',
			xinwenchild:[],
			deptName:[],
			deptid:'',
			files: [],
		}
	},
	methods: {
		query: function () {      //模糊查询√
			if (vm.q.xinwenName.length>25) {
				alert('标题查询不可超过25个字！');
			}
			else{vm.reload();}
		},
		reload: function () {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
				postData:{'title': vm.q.xinwenName},
				page:page
			}).trigger("reloadGrid");
		},
		add: function(){          //添加√
			vm.showList = false;
			vm.equipmentList = {};
			vm.title = "新增";
			vm.xinwen = {deptName:null, deptId:null};
			$('#content_sn').summernote('code', null);
			vm.getXflxs()
			vm.getDept();
		},

		update: function () {         //修改√
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			var rowData = $("#jqGrid").jqGrid('getRowData', id);
			if(rowData.spzt == '<span class="label label-success">审核通过</span>') {
				alert("已通过审核的活动不可修改！");
				return;
			}
			else{
				vm.showList = false;
				vm.title = "修改";
				vm.getXflxs()
				vm.getXinwen(id)       //先从数据库里读出来原本的信息
			}

		},

		// info: function (id) {       //查看活动详情页 跳转->  √
		// 	// var id = getSelectedRow();
		// 	// if(id == null){
		// 	// 	return ;
		// 	// }
		// 	$.ajaxSettings.async = false;
		// 	vm.getXinwen(id)
		// 	$.ajaxSettings.async = true;
		// 	console.log("spztspztspztspztspzt"+JSON.stringify(vm.xinwen.spzt))
		// 	if(vm.xinwen.spzt == null || vm.xinwen.spzt == undefined || vm.xinwen.spzt != '1'){
		// 		alert("待审批或者驳回状态无法报名")
		// 		return;
		// 	}
		// 	window.open(baseURL + "/xinweninfo.html?id="+id,"_bland")
		// },

		del: function () {     //删除  xinwen-controller      √
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
					url: baseURL + "sysXinwen/delete",
					contentType: "application/json",
					data: JSON.stringify(ids),
					success: function(r){
						if(r.code == 0){
							alert('操作成功', function(){
								vm.reload();
							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},

		getXinwen: function(xinwenId){   //从xinwen数据库里读出来改活动原本的信息   xinwen——controller
			$.get(baseURL + "sysXinwen/info/"+xinwenId, function(r){
				vm.xinwen = r.xinwen;
				$('#content_sn').summernote('code', vm.xinwen.content);
				vm.allFiles = r.xinwen.files
			});
		},

		getXflxs: function(){   //从type数据库里读出来类型   type——controller
			$.get(baseURL + "sysType/getTypes", function(r){
				vm.xflxs = r.types;
			});
		},

		saveOrUpdate: function () {
			if(vm.xinwen.title==null || vm.xinwen.title==""){
				alert('活动标题未填！不可发布！');
			}
			else if(vm.xinwen.hdsj==null){
				alert('活动时间未填！不可发布！');
			}
			else if(vm.xinwen.xflx==null){
				alert('学分类型未填！不可发布！');
			}
			else if(vm.xinwen.hdfz==null){
				alert('活动分值未填！不可发布！');
			}
			else if(vm.xinwen.hddd==null || vm.xinwen.hddd==""){
				alert('活动地点未填！不可发布！');
			}
			else if (vm.xinwen.title.length>25) {
				alert('活动标题不可以超过25个字！');
			}
			else if (!this.isValidNumber(vm.xinwen.hdfz)) {
				alert('活动分值一栏请输入有效的数字或浮点数');
			}
			else if (vm.xinwen.hdfz<0 || vm.xinwen.hdfz==0) {
				alert('活动分值不可小于0或等于0');
			}
			else if (vm.xinwen.hdfz>2 ) {
				alert('活动分值不可大于2');
			}
			else if (vm.xinwen.hdfz.length>3 ) {
				alert('活动分值最多为1位小数');
			}
			else if (vm.xinwen.hddd.length>30 ) {
				alert('活动地点不可以超过30个字！');
			}
			else{
				var content_sn = $("#content_sn").summernote('code');
				$("#content").val(content_sn);
				vm.xinwen.content=content_sn;                 //采用过滤器的方式,在数据提交时进行转义
				vm.xinwen.type='1';
				console.log("contentcontentcontent"+vm.xinwen.content)
				var url = vm.xinwen.id == null ? "sysXinwen/save" : "sysXinwen/update";
				if(url === "sysXinwen/update"){
					vm.sp(0);
					vm.xinwen.spzt='0';
				}
				$.ajax({
					type: "POST",
					url: baseURL + url,
					contentType: "application/json",
					data: JSON.stringify(vm.xinwen),
					success: function(r){
						if(r.code === 0){
							alert('操作成功', function(){
								vm.reload();
							});
						}else{
							alert(r.msg);
						}
					}
				});
			}
		},

		sp: function (spzt) {  //审批 √
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			var param ={spzt:spzt,ids:ids}
			if(spzt!=0)
			{
				confirm('确定要审批选中的记录？', function(){
					$.ajax({
						type: "POST",
						url: baseURL + "sysXinwen/sp",
						contentType: "application/json",
						data: JSON.stringify(param),
						success: function(r){
							if(r.code == 0){
								alert('审批成功', function(){
									vm.reload();
								});
							}else{
								alert(r.msg);
							}
						}
					});
				});
			}
			else {
				var param ={spzt:0,ids:ids}
				$.ajax({
					type: "POST",
					url: baseURL + "sysXinwen/sp",
					contentType: "application/json",
					data: JSON.stringify(param),
				});
			}
		},
		reload: function () {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
				postData:{'title': vm.q.xinwenName},
				page:page
			}).trigger("reloadGrid");
		},
		isValidNumber: function (value) {
			return !isNaN(parseFloat(value)) && isFinite(value);
		},
		getMenuTree: function(id) {
			//加载菜单树
			$.get(baseURL + "sys/menu/list", function(r){
				menu_ztree = $.fn.zTree.init($("#menuTree"), menu_setting, r);
				//展开所有节点
				menu_ztree.expandAll(true);

				if(id != null){
					vm.getXinwen(id);
				}
			});
		},
		getDataTree: function(id) {
			//加载菜单树
			$.get(baseURL + "sys/dept/list", function(r){
				data_ztree = $.fn.zTree.init($("#dataTree"), data_setting, r);
				//展开所有节点
				data_ztree.expandAll(true);
			});
		},

		getDept: function(){
			//加载部门树
			$.get(baseURL + "sys/dept/list", function(r){
				dept_ztree = $.fn.zTree.init($("#deptTree"), dept_setting, r);
				var node = dept_ztree.getNodeByParam("deptId", vm.xinwen.deptid);
				if(node != null){
					dept_ztree.selectNode(node);

					vm.xinwen.deptName = node.name;
				}
			})
		},
		deptTree: function(){
			layer.open({
				type: 1,
				offset: '50px',
				skin: 'layui-layer-molv',
				title: "选择部门",
				area: ['300px', '450px'],
				shade: 0,
				shadeClose: false,
				content: jQuery("#deptLayer"),
				btn: ['确定', '取消'],
				btn1: function (index) {
					var node = dept_ztree.getSelectedNodes();
					//选择上级部门
					vm.xinwen.deptid = node[0].deptId;
					vm.xinwen.deptName = node[0].name;

					layer.close(index);
				}
			});
		},

	}
});


laydate.render({
	elem: '#hdsj', //指定元素
	format: 'yyyy-MM-dd HH:mm:ss',
	type: 'datetime',
	min: new Date().toISOString(), // 设置最小时间为当前时间
	done: function(value, date, endDate){
		vm.xinwen.hdsj = value;
	}
});

