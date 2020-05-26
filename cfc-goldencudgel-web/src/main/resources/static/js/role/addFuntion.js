$(function(){
   
	//用来判断是否是web权限的新增
	var webType = $("#webType").val();
    if(webType!='') {
    	changeType(2);
    }else{
    	changeType(1);
    }
    
});


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
function changeType(type) {
    //改变样式
    var authType;
    if(type == 1){
        $("#hi_type").val(1)
        $("#app").attr("class","vsb");
        $("#web").attr("class","vsw");
        authType = '1';
    }else{
        $("#hi_type").val(2);
        $("#web").attr("class","vsb");
        $("#app").attr("class","vsw");
        authType='2';
    }
    $("#app").removeAttr("onclick");
    $("#web").removeAttr("onclick");
    bodymask("請稍等...");
    $.ajax({
        url:context_path + "/auths/getFuntions",
        type:"post",
        data:{"authType":authType},
        datatype: "html",
        success: function(result) {
            bodyunmask();
            $("#func_list").empty();
            $("#func_list").html(result);
            $("#app").attr("onclick","changeType(1)");
            $("#web").attr("onclick","changeType(2)");
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            bodyunmask();
            alert(errorThrown);
            $("#app").attr("onclick","changeType(1)");
            $("#web").attr("onclick","changeType(2)");
        }
    });
}

function rebackSearchPage() {
    window.location.href = context_path + "/auths/viewAuths";
}

function selectHome(obj) {
    setUpLeave(obj)
    setDownLeave(obj);
}

function setDownLeave(obj) {
    var funcCode = $(obj).attr("funcCode");
    var flag = $(obj).is(':checked');
    $("input[_parentCode='"+funcCode+"']").each(function () {
        $(this).prop("checked",flag);
        setDownLeave($(this));
    })
}

function setUpLeave(obj) {
    var parentCode = $(obj).attr("_parentCode");
    var flag = $(obj).is(':checked');
    if(!flag) return;
    $("input[funcCode='"+parentCode+"']").each(function () {
        $(this).prop("checked",flag);
        setUpLeave($(this));
    })
}

function saveAddFunctions() {
    var flag = true;
    var roleId = $("#roleId").find("option:selected").val();
    if (roleId == null || roleId == 'null' || roleId == "" ) {
        alertInfo("提示", "請選擇角色！");
        flag = false;
        return;
    }
    if(flag){
        var authType = $("#hi_type").val();
        var enabled = $("#roleEnable").val();
        var s="";
        var funcFlag = true;
        $(".functions:checkbox:checked").each(function() {
            s += $(this).attr("funcCode") + ",";
            funcFlag = false;
        });
        if(funcFlag){
            alertInfo("提示", "請選擇權限！");
            return;
        }
        if (s.length > 0) {
            s = s.substring(0, s.length - 1);
        }
        $("#add_save_funs").removeAttr("onclick");
        $.ajax({
            url :context_path + "/auths/saveAddFunctions",
            data : {
                roleId : roleId,
                enabled : enabled,
                funcStr   : s,
                authType :authType
            },
            type : "post",
            dataType : "json",
            success : function(result) {
                if (result.flag) {
                    alertInfo("提示", "儲存成功", function() {
//                        window.location.href = context_path + "/auths/viewAuths";
                    });
                } else {
                    alertInfo("提示", result.message, null);
                    $("#add_save_funs").attr("onclick","saveAddFunctions()");
                }
            },
            error : function(XMLHttpRequest, error, errorThrown) {
               alert("系統異常");
            }
        });
    }
}


function tabFunction(obj) {
    if (obj != null) {
        var hh = $(obj).parent().find("ul");
        var display = $(hh).css("display");
        if (display == 'none') {
            $(obj).attr("src", context_path + "/images/selected_down.png");
            $(hh).css("display", "block");
        } else {
            $(obj).attr("src", context_path + "/images/select.png");
            $(hh).css("display", "none");
        }
    }
}