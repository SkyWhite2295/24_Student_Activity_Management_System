$(function () {
	$("#jqGrid").jqGrid({
		url: baseURL + 'sysType/list',
		datatype: "json",
		colModel: [
			{ label: 'ID', name: 'id', index: "id", width: 45, key: true },
			{ label: '分类名称', name: 'name', index: "qcmc", width: 75 },
			{ label: '显示序号', name: 'xh', index: "qcmc", width: 75 },

		],
		viewrecords: true,
		height: 385,
		rowNum: 10,
		rowList : [10,30,50],
		rownumbers: true,
		rownumWidth: 25,
		autowidth:true,
		multiselect: true,
		pager: "#jqGridPager",
		jsonReader : {
			root: "page.records",
			page: "page.current",
			total: "page.pages",
			records: "page.total"
		},
		prmNames : {
			page:"page",
			rows:"limit",
			order: "order"
		},
		gridComplete:function(){
			//隐藏grid底部滚动条
			$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
		}
	});
});

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
			typeName: null
		},
		importModle: true,
		showList: true,
		showDelect: true,
		title:null,
		equipmentList:{},
		allFiles: [],
		type:{
			id:'',
			qcmc:'',
			bh:'',
			username:'',
			createdate:'',
			enddate:'',
			equipmentIdList:[],
			equipmentId:'',
			typechild:[],
			deptName:[],
			deptid:'',
			files: [],
		}
	},
	methods: {
		deleteFile: function(id) {
			if(id == null) {
				alert("请选择要删除的文件!");
				return;
			}
			vm.deleteFles = {"id":id};
			confirm('确定要删除该记录？', function(){
				$.ajax({
					type: "POST",
					url: baseURL + "tMaterialFile/deleteByFileId",
					contentType: "application/json",
					data: JSON.stringify(vm.deleteFles),
					success: function(r){
						if(r.code == 0){
							alert('文件删除成功', function(){
								vm.reload();
							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},
		download: function (id) {
			console.log("id)id)id)id)" + id)
			$.get(baseURL + "tMaterialFile/ishSingleFile/" + id, function (r) {
				if (r.code == 0) {
					if (r.fileName != '无下载文件' && r.fileName != '文件不存在') {
						var url = baseURL + "tMaterialFile/downFile?id=" + id + "&token=" + token;
						window.location.href = url;
					} else {
						alert(r.fileName)
					}
				}
			});
		},
		saveFile: function () {
			var value = document.querySelectorAll('*[name="abc"]')
			$("#box").val(value);
			$("#myModalPreachData").modal('hide');
		},
		openPreachData: function () {
			$("#myModalPreachData").modal('show');
		},
		shutdowPreach: function () {
			$("#myModalPreachData").modal('hide');
		},
		importFile: function () {
			if ($("#fileList").val() == null || $("#fileList").val() == "") {
				alert("请选择具体附件再上传!");
				return;
			}
			var form = document.getElementById('upload');
			$.ajax({
				url: baseURL + "tMaterialFile/importPsot",
				type: 'post',
				data: new FormData(form),
				processData: false,
				contentType: false,
				dataType: "json",
				success: function (r) {
					console.log(JSON.stringify(r))
					if (r.msg == 'false') {
						alert('您不具备上传该密级条件');
						return;
					}
					if (r.msg == 'false1') {
						alert('密标程序未启动');
						return;
					}
					var obj = new Object();
					$("#fileList").val("");
					obj['id'] = r.id;
					obj['filePath'] = r.path;
					obj['fileName'] = r.fileName;
					obj['mbfklj'] = r.mbfklj;
					vm.allFiles.push(obj);
					vm.type.files = vm.allFiles;
					alert("导入成功！");
				},
				error: function () {
					alert("导入失败！");
				}
			})
		},
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.equipmentList = {};
			vm.title = "新增";
			vm.type = {deptName:null, deptId:null};
			vm.getDept();
		},
		update: function () {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}

			vm.showList = false;
			vm.title = "修改";
			vm.getType(id)
		},
		del: function () {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}

			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
					url: baseURL + "sysType/delete",
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
		getType: function(typeId){
			$.get(baseURL + "sysType/info/"+typeId, function(r){
				vm.type = r.type;
				vm.getDept();
			});
		},
		saveOrUpdate: function () {
			var url = vm.type.id == null ? "sysType/save" : "sysType/update";
			$.ajax({
				type: "POST",
				url: baseURL + url,
				contentType: "application/json",
				data: JSON.stringify(vm.type),
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
		},
		getMenuTree: function(id) {
			//加载菜单树
			$.get(baseURL + "sys/menu/list", function(r){
				menu_ztree = $.fn.zTree.init($("#menuTree"), menu_setting, r);
				//展开所有节点
				menu_ztree.expandAll(true);

				if(id != null){
					vm.getType(id);
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
				var node = dept_ztree.getNodeByParam("deptId", vm.type.deptid);
				if(node != null){
					dept_ztree.selectNode(node);

					vm.type.deptName = node.name;
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
					vm.type.deptid = node[0].deptId;
					vm.type.deptName = node[0].name;

					layer.close(index);
				}
			});
		},
		reload: function () {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
				postData:{'name': vm.q.typeName},
				page:page
			}).trigger("reloadGrid");
		},

	}
});

laydate.render({
	elem: '#faultTime', //指定元素
	format:'yyyy-MM-dd HH:mm:ss',
	//日期时间选择器
	type: 'datetime',
	done: function(value, date, endDate){
		vm.type.createdate= value;
	}
});

laydate.render({
	elem: '#enddateTime', //指定元素
	format:'yyyy-MM-dd HH:mm:ss',
	//日期时间选择器
	type: 'datetime',
	done: function(value, date, endDate){
		vm.type.enddate= value;
	}
});