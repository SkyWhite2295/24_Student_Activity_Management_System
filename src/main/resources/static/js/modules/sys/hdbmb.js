

$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'sysHdbmb/list',
        datatype: "json",
        colModel: [
            { label: '活动标题', name: 'title', index: 'title', width: 170 }, // 新添加的列
            { label: '学分类型', name: 'xflx', index: 'xflx', width: 60,
                formatter: function(value, options, row){
                    if(value === "22bc6839dd9741d786966666660bdd"){
                        return '劳育';
                    } else if(value === "22bc6839dd9741d7869a6840f1bb0bdd"){
                        return '美育';
                    }else if(value === "6c78cfa0f5fa4ce39d13780fb9adeb1f"){
                        return '智育';
                    }else if(value === "bd6baed92abc4bd2a3a11095c304be33"){
                        return '体育';
                    } else if(value === "c4a0a8a7daae422a959fd75c743f5a1b"){
                        return '德语';
                    }
                }
            },
            { label: '活动分值', name: 'hdfz', index: 'hdfz', width: 60 },
            { label: '报名人员', name: 'username', index: "username", width: 100 },
            { label: '报名时间', name: 'bmsj', index: "bmsj", width: 100 },
            { label: '通过状态', name: 'tg', index: 'tg', width: 80,
                formatter: function(value, options, row){
                    if(value === "1"){
                        return '<span class="label label-success">通过</span>';
                    } else if(value === "2"){
                        return '<span class="label label-danger">未通过</span>';
                    } else {
                        return '<span class="label label-warning">结果未出</span>';
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
            hdbmbName: null
        },
        importModle: true,
        showList: true,
        showDelect: true,
        title:null,
        equipmentList:{},
        allFiles: [],
        szfsid:"",
        jys:[{"id":"1","name":"是"},{"id":"2","name":"否"}],
        sfzsyms:[{"id":"1","name":"是"},{"id":"2","name":"否"},{"id":"3","name":"未知"}],
        types:[{"id":"1","name":"狗"},{"id":"2","name":"猫"},{"id":"3","name":"猪"},{"id":"4","name":"其他"}],
        jkzts:[{"id":"1","name":"健康"},{"id":"2","name":"一般"},{"id":"3","name":"病危"}],
        hdbmb:{
            id:'',
            qcmc:'',
            bh:'',
            username:'',
            tg:'',
            hdfz:'',
            createdate:'',
            enddate:'',
            equipmentIdList:[],
            equipmentId:'',
            hdbmbchild:[],
            deptName:[],
            deptid:'',
            files: [],
        }
    },
    methods: {
        query: function () {
            vm.reload();
        },

        getHdbmb: function(hdbmbId){
            $.get(baseURL + "sysHdbmb/info/"+hdbmbId, function(r){
                vm.hdbmb = r.hdbmb;
                vm.allFiles = r.hdbmb.files;
                vm.getDept();
            });
        },

        getMenuTree: function(id) {
            //加载菜单树
            $.get(baseURL + "sys/menu/list", function(r){
                menu_ztree = $.fn.zTree.init($("#menuTree"), menu_setting, r);
                //展开所有节点
                menu_ztree.expandAll(true);

                if(id != null){
                    vm.getHdbmb(id);
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
                var node = dept_ztree.getNodeByParam("deptId", vm.hdbmb.deptid);
                if(node != null){
                    dept_ztree.selectNode(node);

                    vm.hdbmb.deptName = node.name;
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
                    vm.hdbmb.deptid = node[0].deptId;
                    vm.hdbmb.deptName = node[0].name;

                    layer.close(index);
                }
            });
        },
        reload: function () {
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam','page');
            $("#jqGrid").jqGrid('setGridParam',{
                postData:{'name': vm.q.hdbmbName},
                page:page
            }).trigger("reloadGrid");
        },
        tg: function (tg) {  //通过 √
            var ids = getSelectedRows();
            if(ids == null){
                return ;
            }
            var param ={tg:tg,ids:ids}

            var hdfzList = []; // 存储选中行的 hdfz
            var xflxList = [];
            var tgList = [];
            var usernameList = []; // 存储选中行的 username
            // 遍历选中行的 ID 数组，获取每行数据的 hdfz 和 username 字段值
            for (var i = 0; i < ids.length; i++) {
                var rowData = $("#jqGrid").jqGrid('getRowData', ids[i]);
                // 将 hdfz 和 username 添加到各自的数组中
                hdfzList.push(rowData.hdfz);
                tgList.push(rowData.tg);
                xflxList.push(rowData.xflx);
                usernameList.push(rowData.username);
            }

            var param1 = {
                tg:tgList,
                hdfz: hdfzList,
                xflx: xflxList,
                username: usernameList,
                ids: ids
            };

            if(tg==1){
                confirm('确定要审核当前的记录？(审核通过后学生将获得相应的学分！)', function(){
                    $.ajax({
                        type: "POST",
                        url: baseURL + "sysHdbmb/tg",
                        contentType: "application/json",
                        data: JSON.stringify(param),
                        success: function(r){
                            if(r.code == 0){
                                alert('通过成功', function(){
                                    vm.reload();
                                });
                            }else{
                                alert(r.msg);
                            }
                        }
                    });
                    $.ajax({
                        type: "POST",
                        url: baseURL + "sysXuefen/gx",
                        contentType: "application/json",
                        data: JSON.stringify(param1),
                        success: function(r){
                            if(r.code == 0){
                                alert('通过成功', function(){
                                    vm.reload();
                                });
                            }else{
                                alert(r.msg);
                            }
                        }
                    });
                });
            }
            else if(tg==2){
                confirm('确定要审核当前的记录？(当前选项是设置不通过！)', function(){
                    $.ajax({
                        type: "POST",
                        url: baseURL + "sysHdbmb/tg",
                        contentType: "application/json",
                        data: JSON.stringify(param),
                        success: function(r){
                            if(r.code == 0){
                                alert('设置不通过成功', function(){
                                    vm.reload();
                                });
                            }else{
                                alert(r.msg);
                            }
                        }
                    });
                });
            }

        },

    }
});
laydate.render({
    elem: '#bmsj', //指定元素
    format:'yyyy-MM-dd HH:mm:ss',
    //日期时间选择器
    type: 'datetime',
    done: function(value, date, endDate){
        vm.hdbmb.bmsj= value;
    }
});
laydate.render({
    elem: '#updatetime', //指定元素
    format:'yyyy-MM-dd HH:mm:ss',
    //日期时间选择器
    type: 'datetime',
    done: function(value, date, endDate){
        vm.hdbmb.updatetime= value;
    }
});
laydate.render({
    elem: '#createtime', //指定元素
    format:'yyyy-MM-dd HH:mm:ss',
    //日期时间选择器
    type: 'datetime',
    done: function(value, date, endDate){
        vm.hdbmb.createtime= value;
    }
});
