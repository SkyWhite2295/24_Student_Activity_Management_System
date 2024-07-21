$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'sysXinwen/tzgglist',
        datatype: "json",
        colModel: [
			{ label: 'ID', name: 'id', index: "id", width: 45, key: true },
			{ label: '通知公告信息标题', name: 'title', index: "qcmc", width: 75 },
			{ label: '作者', name: 'zuozhe', index: "qcmc", width: 75 },
			{ label: '时间', name: 'createtime', index: "qcmc", width: 75 },
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
		hdlbs:[{"id":"1","name":"娱乐"},{"id":"2","name":"思想"},{"id":"3","name":"综合素质"}
			,{"id":"4","name":"校方"},{"id":"5","name":"社团"},{"id":"6","name":"个人"}],
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
		sp: function (spzt) {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			var param ={spzt:spzt,ids:ids}
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
		sendFile: function (val,files,editor,welEditable) {
			data = new FormData();
			data.append("files", files);
			$.ajax({
				data: data,
				dataType: 'json',
				type: "POST",
				url: baseURL + "tMaterialFile/ajaxUploadFile",
				cache: false,
				contentType: false,
				processData: false,
				responseType: "json",
				success: function (r) {
					console.log("successsuccesssuccess")
					$(val).summernote('editor.insertImage', r.path);
				},
				error: function (XMLHttpRequest, textStatus, errorThrown) {
					console.log("errorerrorerror")
					alert(XMLHttpRequest.status);
					alert(XMLHttpRequest.readyState);
					alert(textStatus);
				}
			});
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
					vm.xinwen.files = vm.allFiles;
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
			vm.xinwen = {deptName:null, deptId:null};
			$('#content_sn').summernote('code', null);
			vm.getXflxs()
			vm.getDept();
		},
		info: function () {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			window.open(baseURL + "/xinweninfo.html?id="+id,"_bland")

		},
		update: function () {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			
			vm.showList = false;
            vm.title = "修改";
            vm.getXflxs()
			vm.getXinwen(id)
		},
		del: function () {
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
		getXinwen: function(xinwenId){
			$.get(baseURL + "sysXinwen/info/"+xinwenId, function(r){
				vm.xinwen = r.xinwen;
				$('#content_sn').summernote('code', vm.xinwen.content);
				vm.allFiles = r.xinwen.files
			});
		},
		getXflxs: function(){
			$.get(baseURL + "sysType/getTypes", function(r){
				vm.xflxs = r.types;
			});
		},

		saveOrUpdate: function () {
			var content_sn = $("#content_sn").summernote('code');
			$("#content").val(content_sn);
			//vm.message.notifyContent=html_encode(content_sn);  //采用js中直接转义方式,调用公用的转义函数encode
			vm.xinwen.content=content_sn;                 //采用过滤器的方式,在数据提交时进行转义
			vm.xinwen.type='2';
			console.log("contentcontentcontent"+vm.xinwen.content)
			var url = vm.xinwen.id == null ? "sysXinwen/save" : "sysXinwen/update";
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
	    reload: function () {
	    	vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                postData:{'title': vm.q.xinwenName},
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
		vm.xinwen.createdate= value;
	}
});

laydate.render({
	elem: '#enddateTime', //指定元素
	format:'yyyy-MM-dd HH:mm:ss',
	//日期时间选择器
	type: 'datetime',
	done: function(value, date, endDate){
		vm.xinwen.enddate= value;
	}
});

laydate.render({
	elem: '#hdsj', //指定元素
	format:'yyyy-MM-dd HH:mm:ss',
	//日期时间选择器
	type: 'datetime',
	done: function(value, date, endDate){
		vm.xinwen.hdsj= value;
	}
});
