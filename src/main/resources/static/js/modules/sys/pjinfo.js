$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'sysPjinfo/list',
        datatype: "json",
        colModel: [
			{ label: 'ID', name: 'id', index: "id", width: 45, key: true },
			{ label: '评价类型', name: 'pjtype', width: 80,
				formatter: function(value, options, row){
					if(value === "1"){
						return '<span class="label label-success">建议</span>';
					}else if(value === "2"){
						return '<span class="label label-danger">投诉</span>';
					}else{
						return '<span class="label label-success">其他</span>';
					}
				}
			},
			{ label: '评价公司', name: 'deptName', index: "title", width: 75 },
			{ label: '评价标题', name: 'title', index: "title", width: 75 },
            { label: '评价内容', name: 'content', width: 75 },
			{ label: '评价人员', name: 'author', width: 100 },
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
			pjinfoName: null
		},
		showList: true,
		title:null,
		equipmentList:{},
		allFiles:[],

		pjinfo:{
			id:'',
			qcmc:'',
			bh:'',
			username:'',
			createdate:'',
			enddate:'',
			equipmentIdList:[],
			equipmentId:'',
			title:'',
			content:'',
			author:'',
			deptName:'',
			deptid:'',

		}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.equipmentList = {};
			vm.title = "新增";
			vm.pjinfo = {deptName:null, deptId:null};
			vm.getEquipmentList();
			vm.getDept();

		},
		getEquipmentList: function(){
			$.get(baseURL + "sysEquipment/select", function(r){
				vm.equipmentList = r.equipmentList;
			});
		},

		update: function () {
			var id = getSelectedRow();
			console.log("idididididid"+id)
			if(id == null){
				return ;
			}
			
			vm.showList = false;
            vm.title = "修改";
			vm.getDept();
			vm.getPjinfo(id)
			vm.getEquipmentList();
		},
		qxsh: function () {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}

			confirm('确定取消在申请的记录？', function(){
				$.ajax({
					type: "POST",
					url: baseURL + "sysPjinfo/qxsh",
					contentType: "application/json",
					data: JSON.stringify(ids),
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
		shzt: function () {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}

			confirm('确定要审批选中的记录？', function(){
				$.ajax({
					type: "POST",
					url: baseURL + "sysPjinfo/shzt",
					contentType: "application/json",
					data: JSON.stringify(ids),
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
		del: function () {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "sysPjinfo/delete",
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
		getPjinfo: function(pjinfoId){
            $.get(baseURL + "sysPjinfo/info/"+pjinfoId, function(r){
            	vm.pjinfo = r.pjinfo;
    		});
		},
		savePlxx: function () {
			console.log("idididididididid"+vm.pjinfo.id)
			// var url = vm.pjinfo.id == null ? "sysPjinfo/save" : "sysPjinfo/update";
			$.ajax({
				type: "POST",
				url: baseURL + "sysPjinfo/savePlxx",
				contentType: "application/json",
				data: JSON.stringify(vm.pjinfo),
				success: function(r){
					if(r.code === 0){
						alert('操作成功', function(){
							vm.reloadallFiles();
						});
					}else{
						alert(r.msg);
					}
				}
			});
		},
		saveOrUpdate: function () {
			var url = vm.pjinfo.id == null ? "sysPjinfo/save" : "sysPjinfo/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.pjinfo),
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
					vm.getPjinfo(id);
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
                var node = dept_ztree.getNodeByParam("deptId", vm.pjinfo.deptid);
                if(node != null){
                    dept_ztree.selectNode(node);

                    vm.pjinfo.deptName = node.name;
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
                    vm.pjinfo.deptid = node[0].deptId;
                    vm.pjinfo.deptName = node[0].name;

                    layer.close(index);
                }
            });
        },
	    reload: function () {
	    	vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                postData:{'pjinfoName': vm.q.pjinfoName},
                page:page
            }).trigger("reloadGrid");
		},
		reloadallFiles: function () {
			var id = getSelectedRow();
			console.log("ididisssssssssssssdididid"+id)
			$.get(baseURL + "sysPjinfo/infoprent/"+id, function(r){
				console.log("dfsdfdsfddddddddddddddddddddddddd"+JSON.stringify(r))
				vm.pjinfo = r.pjinfo;
				vm.allFiles = r.allFiles;
			});
		},
		dygn: function () {
			// $("#jqGrid").printArea();
			// bdhtml=window.document.body.innerHTML;
			// sprnstr="<!--startprint-->";
			// eprnstr="<!--endprint-->";
			// prnhtml=bdhtml.substr(bdhtml.indexOf(sprnstr)+17);
			// prnhtml=prnhtml.substring(0,prnhtml.indexOf(eprnstr));
			// window.document.body.innerHTML=prnhtml;
			// window.print();

			var id = getSelectedRow();
			console.log("idididididid"+id)
			if(id == null){
				return ;
			}
			vm.pjinfo.id = id;

			// vm.getPjinfo(id)
			//
			$.get(baseURL + "sysPjinfo/infoprent/"+id, function(r){
				console.log("dfsdfdsfddddddddddddddddddddddddd"+JSON.stringify(r))
				vm.pjinfo = r.pjinfo;
				vm.pjinfo.content = "";
				vm.allFiles = r.allFiles;
			});
			// vm.getEquipmentList();

			$("#myModalPreachData").modal('show');
		}
	}
});

laydate.render({
	elem: '#faultTime', //指定元素
	format:'yyyy-MM-dd HH:mm:ss',
	//日期时间选择器
	type: 'datetime',
	done: function(value, date, endDate){
		vm.pjinfo.createdate= value;
	}
});

laydate.render({
	elem: '#enddateTime', //指定元素
	format:'yyyy-MM-dd HH:mm:ss',
	//日期时间选择器
	type: 'datetime',
	done: function(value, date, endDate){
		vm.pjinfo.enddate= value;
	}
});