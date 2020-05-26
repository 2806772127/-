$(function(){
	queryRoles();
});
function findPage(curpage){
    queryRoles(curpage);
}
function queryRoles(curPage) {
        var roleName = $("#roleName").val();
        $.ajax({
            url:context_path + "/role/viewRoleList",
            type:"post",
            data:{"roleName":roleName,"curPage":curPage},
            datatype: "html",
            success: function(result) {
                $("#role_list").empty();
                $("#role_list").html(result);
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                alert(textStatus + " " + XMLHttpRequest.readyState + " "
                    + XMLHttpRequest.status + " " + errorThrown);
            }
        });
    }

function addRoles() {
    openDialog("hi_add_role", "新增角色", 399, 280);
    $("#add_save_button").attr("onclick","saveAddRole()");
}

function changeEnable(enable) {
    //改变样式
    if(enable == 1){
        $("#roleEnable").val(1)
        $("#qiyong").attr("class","vsb");
        $("#jingyong").attr("class","vsw");
    }else{
        $("#roleEnable").val(0);
        $("#jingyong").attr("class","vsb");
        $("#qiyong").attr("class","vsw");
    }
}
function openDialog(divId, title, width, height) {
    $("#" + divId).dialog({
        title : title,
        width : width,
        height : height,
        modal : true,
        draggable : false
    });
}
function closeWindow(id) {
    $("#"+id).dialog('close');
}
function checkRoleId() {
    var flag = false;
    var compares = ['CMLF_SYS','CMLF_SUP','CMLF_DIS','CMLF_TEA','CMLF_CRE','CMLF_SAL','CMLF_CASE'];
    var  roleId = $("#hi_role_id").val();
    for(var i = 0;i<compares.length;i++){
        var startFlag = roleId.indexOf(compares[i]);
        if(startFlag == 0){
            flag = true;
            break;
        }
    }
    if(flag == false){
        $("#hi_role_id").val('');
        return;
        alertInfo("提示","角色代號格式不正確");
    }

}

function saveAddRole() {
    var name = $("#hi_role_name").val().trim();
    var id = $("#hi_role_id").val().trim();
    var orderNum = $("#hi_order_num").val().trim();
    var status = $("#roleEnable").val().trim();
    if(name=="" || id==""||orderNum==""){
        alertInfo("提示","請輸入必填參數");
        return;
    }
    $("#add_save_button").removeAttr("onclick");
    $.ajax({
        url:context_path + "/role/saveAddRole",
        type:"post",
        data:{
            "name":name,
            "id":id,
            "orderNum":orderNum,
            "status":status
        },
        success: function(result) {
            $("#add_save_button").attr("onclick","saveAddRole()");
            if(result.flag){
                alertInfo("提示","儲存成功",function(){
                    window.location.href = context_path + "/role/viewAllRoles";
                });
            }else{
                alertInfo("提示",result.resultMsg,null);
            }
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            $("#add_save_button").attr("onclick","saveAddRole()");
            alert("鏈接超時");
        }
    });
}

function openEditRole(obj) {
    openDialog("hi_edit_role", "修改角色", 399, 280);
    $("#edit_save_button").attr("onclick","saveEditRole()");
    var id = $(obj).attr("roleId");
    var name = $(obj).attr("roleName");
    var status = $(obj).attr("status");
    var orderNum = $(obj).attr("orderNum");
    $("#edit_role_name").val(name);
    $("#edit_role_id").val(id);
    $("#edit_order_num").val(orderNum);
    if(status=='1'){
        $("#edit_qiyong").attr("class","vsb");
    }else{
        $("#edit_jingyong").attr("class","vsb");
    }

}
function changeEditEnable(enable) {
    //改变样式
    if(enable == 1){
        $("#edit_roleEnable").val(1)
        $("#edit_qiyong").attr("class","vsb");
        $("#edit_jingyong").attr("class","vsw");
    }else{
        $("#edit_roleEnable").val(0);
        $("#edit_jingyong").attr("class","vsb");
        $("#edit_qiyong").attr("class","vsw");
    }
}


function saveEditRole() {
    var name = $("#edit_role_name").val().trim();
    var id = $("#edit_role_id").val().trim();
    var orderNum = $("#edit_order_num").val().trim();
    var status = $("#edit_roleEnable").val().trim();
    if(name=="" || orderNum=="" ){
        alertInfo("提示","請輸入必填參數");
        return ;
    }
    $("#edit_save_button").removeAttr("onclick");
    $.ajax({
        url:context_path + "/role/saveEditRole",
        type:"post",
        data:{
            "name":name,
            "id":id,
            "orderNum":orderNum,
            "status":status
        },
        success: function(result) {
            $("#edit_save_button").attr("onclick","saveEditRole()");
            if(result.flag){
                alertInfo("提示","儲存成功",function(){
                    window.location.href = context_path + "/role/viewAllRoles";
                });
            }else{
                alertInfo("提示",result.resultMsg,null);
            }

        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            alert(errorThrown);
            $("#edit_save_button").attr("onclick","saveEditRole()");
        }
    });
}

function deleteRole(obj) {
    var id = $(obj).attr("roleId");
    alertInfoCOPY("提示","請確認是否刪除？",function deleteSelectRole() {
        $(obj).removeAttr("onclick");
        $.ajax({
            url:context_path + "/role/deleteRole",
            type:"post",
            data:{ "id":id },
            success: function(result) {
                if(result.flag){
                    alertInfo("提示","删除成功",function(){
                        window.location.href = context_path + "/role/viewAllRoles";
                    });
                }else{
                    alertInfo("提示",result.resultMsg,null);
                }

            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                alert(textStatus + " " + XMLHttpRequest.readyState + " "
                    + XMLHttpRequest.status + " " + errorThrown);
            }
        });
    })

}
function gotoPage(curPage) {
    queryRoles(curPage);
}


