$(function(){
     //设定选中值
    selectDefaultValue();
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

function selectDefaultValue(){
    var rfList = $("#rfList").val();
    rfList = rfList.replace("[","").replace("]","");
    var strs = rfList.split(",");
    for(var i=0;i<strs.length;i++){
        var funccode = strs[i].trim();
        $("input[funcCode='"+funccode+"']").prop("checked",'true');
    }
}

function rebackRFsearch() {
    window.location.href = context_path + "/auths/viewAuths";
}

function saveUpdateRF() {
    var roleId = $("#roleId").val();
    var authType = $("#authType").val();
    var enabled = $("#roleEnable").val();
    var s="";
    $(".functions:checkbox:checked").each(function() {
        s += $(this).attr("funcCode") + ",";
    });
    if (s.length > 0) {
        s = s.substring(0, s.length - 1);
    }
    $.ajax({
        url :context_path + "/auths/saveUpdateRF",
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
                alertInfo("提示", "修改成功", function() {
//                    window.location.href = context_path + "/auths/viewAuths";
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