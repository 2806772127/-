$(function () {
	 queryFunctions(1);
})

function changeClass(flag) {
    if(flag == 1){
        $("#hi_type").val(1)
        $("#APP").attr("class","vsb");
        $("#WEB").attr("class","vsw");
        queryFunctions();
    }else{
        $("#hi_type").val(2);
        $("#WEB").attr("class","vsb");
        $("#APP").attr("class","vsw");
        queryFunctions();
    }
}

function queryFunctions(curPage) {
    var type = $("#hi_type").val();
    var roleId = $("#roleId").val();
    $.ajax({
        url:context_path + "/auths/viewFuncList",
        type:"post",
        data:{"roleId":roleId,"type":type,"curPage":curPage},
        datatype: "html",
        success: function(result) {
            $("#func_list").empty();
            $("#func_list").html(result);
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            alert(textStatus + " " + XMLHttpRequest.readyState + " "
                + XMLHttpRequest.status + " " + errorThrown);
        }
    });
}

function goAddFunctions() {
	//通过class属性判断是web还是app
	 var typeEle=document.getElementById('WEB');
	 var webVal = $("#WEB").val();
    	 if(typeEle.classList.contains('vsb')){
    		 window.location.href = context_path + "/auths/goAddFunctions?webVal="+webVal; 
    	 }else{
    		 window.location.href = context_path + "/auths/goAddFunctions"; 
    	 }
    
}

function deleteRoleFunc(obj) {
    var roleId = $(obj).attr("roleId");
    var authType = $(obj).attr("authType");

    alertInfoCOPY("提示","請確認是否刪除？",function deleteSelectRole() {
        $(obj).removeAttr("onclick");
    $.ajax({
        url:context_path + "/auths/deleteRoleFunc",
        type:"post",
        data:{"roleId":roleId,"authType":authType},
        dataType : "json",
        success : function(result) {
            if (result.flag) {
                alertInfo("提示", "删除成功", function() {
                    window.location.href = context_path + "/auths/viewAuths";
                });
            } else {
                alertInfo("提示", result.message, null);
            }
        },
        error : function(XMLHttpRequest, error, errorThrown) {
            alert(error);
            alert(errorThrown);
        }
    });
    })

}

function updateRoleFunc(obj) {
    var roleId = $(obj).attr("roleId");
    var authType = $(obj).attr("authType");
    var roleName =encodeURI(encodeURI($(obj).attr("roleName")));
    var enabled = $(obj).attr("enabled");
    window.location.href = context_path + "/auths/updateRoleFunc?roleId="+roleId+"&authType="+authType+"&roleName="+roleName+"&enabled="+enabled;
}










