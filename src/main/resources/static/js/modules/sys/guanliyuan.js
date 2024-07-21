$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'sys/user/guanliyuan',
        datatype: "json",
        colModel: [
            {label: '用户ID', name: 'userId', index: "user_id", width: 45, key: true},
            // { label: '学号/工号', name: 'stadynum', width: 75 },
            {label: '用户名', name: 'username', width: 75},
            {label: '姓名', name: 'realname', width: 75},
            {label: '角色', name: 'roalArraystr', width: 50},
            {label: '手机号', name: 'mobile', width: 80},
            {label: '出生日期', name: 'birthday', width: 90},
            {label: '通讯地址', name: 'address', width: 75},
            {label: '邮箱', name: 'email', width: 90},

            {label: '创建时间', name: 'createTime', index: "create_time", width: 90}
        ],
        viewrecords: true,
        height: 385,
        rowNum: 10,
        rowList: [10, 30, 50],
        rownumbers: true,
        rownumWidth: 25,
        autowidth: true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader: {
            root: "page.records",
            page: "page.current",
            total: "page.pages",
            records: "page.total"
        },
        prmNames: {
            page: "page",
            rows: "limit",
            order: "order"
        },
        gridComplete: function () {
            //隐藏grid底部滚动条
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
        }
    });
});

var setting = {
    data: {
        simpleData: {
            enable: true,
            idKey: "deptId",
            pIdKey: "parentId",
            rootPId: -1
        },
        key: {
            url: "nourl"
        }
    }
};
var ztree;

var vm = new Vue({
    el: '#rrapp',
    data: {
        q: {
            username: null
        },
        importModle: true,
        showListuploadsave: true,
        selected: '',
        enclosure: [],
        showList: true,
        showDelect: true,
        title: null,
        roleList: {},
        allFiles: [],
        usertypeAll: [
            {skey: "0", svalue: "学生"},
            {skey: "1", svalue: "老师"},
            {skey: "2", svalue: "管理员"},
            {skey: "3", svalue: "器材管理员"},
            {skey: "4", svalue: "器材维修员"}],
        user: {
            status: 1,
            deptId: null,
            deptName: null,
            roleIdList: [],
            files: [],
            username:'',
            realname:'',
            password:'',
            mobile:'',
            email:'',
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
            console.log("aaaaaaaaaaaaa")
            $("#fileList").val("");
            $("#myModalPreachData").modal('show');
            vm.selected = "请选择";
        },
        shutdowPreach: function () {
            $("#myModalPreachData").modal('hide');
        },
        importFile: function () {
            if ($("#fileList").val() == null || $("#fileList").val() == "") {
                alert("请选择具体附件再上传!");
                return;
            }
            // if (vm.selected == "请选择") {
            // 	alert("请选择密级");
            // 	return;
            // }
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
                    vm.user.files = vm.allFiles;
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
        add: function () {
            vm.showList = false;
            vm.title = "新增";
            vm.roleList = {};
            vm.user = {deptName: null, deptId: null, status: 1, roleIdList: []};
            vm.allFiles = [];
            vm.user.files = [];
            //获取角色信息
            this.getRoleList();

            vm.getDept();
        },
        getDept: function () {
            //加载部门树
            $.get(baseURL + "sys/dept/list", function (r) {
                ztree = $.fn.zTree.init($("#deptTree"), setting, r);
                var node = ztree.getNodeByParam("deptId", vm.user.deptId);
                if (node != null) {
                    ztree.selectNode(node);

                    vm.user.deptName = node.name;
                }
            })
        },
        update: function () {
            var userId = getSelectedRow();
            if (userId == null) {
                return;
            }

            vm.showList = false;
            vm.title = "修改";

            vm.getUser(userId);
            //获取角色信息
            this.getRoleList();
        },
        del: function () {
            var userIds = getSelectedRows();
            if (userIds == null) {
                return;
            }

            confirm('确定要删除选中的记录？', function () {
                $.ajax({
                    type: "POST",
                    url: baseURL + "sys/user/delete",
                    contentType: "application/json",
                    data: JSON.stringify(userIds),
                    success: function (r) {
                        if (r.code == 0) {
                            alert('操作成功', function () {
                                vm.reload();
                            });
                        } else {
                            alert(r.msg);
                        }
                    }
                });
            });
        },
        saveOrUpdate: function () {
            if (!vm.user.username || vm.user.username.length < 5) {
                layer.alert('账号不能少于5位。');
                // 清空密码框
                //vm.user.username = '';
                return;
            }
            if (!vm.user.realname) {
                // trim() 方法可以去除字符串两端的空白字符（空格、制表符等）
                layer.alert('姓名不能为空');
                // 清空姓名框
                //vm.user.realname = '';
                return;
            }
            // if (!vm.user.password || vm.user.password.length < 6) {
            //     layer.alert('密码不能少于6位。');
            //     // 清空密码框
            //     vm.user.password = '';
            //     return;
            // }
            if (!vm.user.mobile || vm.user.mobile.length < 11) {
                layer.alert('电话不能少于11位。');
                // 清空密码框
                //vm.user.mobile = '';
                return;
            }
            if (!vm.user.email) {
                // trim() 方法可以去除字符串两端的空白字符（空格、制表符等）
                layer.alert('邮箱不能为空');

                //vm.user.email = '';
                return;
            }
            // 检查邮箱格式是否合法
            const regex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
            //const isValidEmail = validateEmail(email);
            if (!regex.test(vm.user.email)) {
                layer.alert('邮箱格式不正确');
                //vm.user.email = ''; // 清空输入框
                return;
            }
            vm.user.roleIdList = ["1"]
            var url = vm.user.userId == null ? "sys/user/save" : "sys/user/update";
            if(vm.user.userId!=null){
                if(vm.user.password){
                    layer.alert('密码不可修改');
                    //vm.user.password = ''; // 清空输入框
                    return;
                }
            }else{
                if (!vm.user.password) {
                    layer.alert('密码不能为空。');
                    // 清空密码框
                   //vm.user.password = '';
                    return;
                }
                else if (vm.user.password.length < 6) {
                    layer.alert('密码不能少于6位。');
                    // 清空密码框
                   //vm.user.password = '';
                    return;
                }
            }
            vm.user.files = vm.allFiles;
            $.ajax({
                type: "POST",
                url: baseURL + url,
                contentType: "application/json",
                data: JSON.stringify(vm.user),
                success: function (r) {
                    if (r.code === 0) {
                        alert('操作成功', function () {
                            vm.reload();
                        });
                    } else {
                        alert(r.msg);
                    }
                }
            });
        },
        getUser: function (userId) {
            $.get(baseURL + "sys/user/info/" + userId, function (r) {
                vm.user = r.user;
                vm.allFiles = r.user.files;
                console.log("allFilesallFiles" + JSON.stringify(vm.allFiles))
                console.log("useruseruseruser" + JSON.stringify(r.user))
                vm.user.password = null;

                vm.getDept();
            });
        },
        getRoleList: function () {
            $.get(baseURL + "sys/role/select", function (r) {
                vm.roleList = r.list;
            });
        },
        deptTree: function () {
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
                    var node = ztree.getSelectedNodes();
                    //选择上级部门
                    vm.user.deptId = node[0].deptId;
                    vm.user.deptName = node[0].name;

                    layer.close(index);
                }
            });
        },
        reload: function () {
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {'username': vm.q.username},
                page: page
            }).trigger("reloadGrid");
        }
    }
});

laydate.render({
    elem: '#birthday', //指定元素
    // format: 'yyyy-MM-dd HH:mm:ss',
	format: 'yyyy-MM-dd',
    //日期时间选择器
    type: 'date',
    max: new Date().toISOString(), // 设置最大时间为当前时间
    done: function (value, date, endDate) {
        vm.user.birthday = value;
    }
});