$(function(){
    queryCase();

    $("#startImg").click(function() {
        $("input[name='startDate']").focus();
    });

    $("#endImg").click(function() {
        $("input[name='endDate']").focus();
    });
    $("input[name='startDate']").datetimepicker({
        onClose: function(selectedDate) {
            $("input[name='startDate']").datepicker("option", "minDate", selectedDate);
        }
    });
    $("input[name='endDate']").datetimepicker({
        onClose: function(selectedDate) {
            $("input[name='endDate']").datepicker("option", "maxDate", selectedDate);
        }
    });

    //開始默認未分配，隱藏部分條件
    showContent();
});

//改變分配狀態
function changeStatus() {
    showContent();
    queryCase();
}

function choseCity() {
    $.ajax({
        url : context_path + "/caseAllocation/choseCity",
        type : "POST",
        dataType : "JSON",
        data : {"cityCode":$("#cityCode").val()},
        success : function (result) {
            var areaSelect = document.getElementById("areaCode");
            areaSelect.options.length=0;
            areaSelect.add(new Option("全部",""));
            for(var i= 0 ;i<result.length;i++ ) {
                for(var code in result[i]) {
                    var option = document.createElement("option");
                    option.setAttribute("value", code);
                    option.text= result[i][code];
                    areaSelect.appendChild(option);
                }
            }
        }
    });
}

function showContent() {
    //根據分配狀態顯示查詢條件欄位
    $(".condition").each(function () {
        $(this).toggle();
    });
    //根據分配狀態顯示操作按鈕
    $(".allocation").each(function () {
        $(this).toggle();
    });
    //案件列表
    $(".caseList").each(function () {
        $(this).toggle();
    });
    //分配組/用戶類別
    $(".allocationUserList").each(function () {
        $(this).toggle();
    });
}

//查詢數據
function queryCase() {
    var allocationStatus = '0';//未分配
    var agent = allocationStatus == "1" ? $("#agent").val() : "";
    var startDate = allocationStatus == "1" ? $("input[name='startDate']").val() : "";//yyyy/mm/dd
    var endDate = allocationStatus == "1" ? $("input[name='endDate']").val() : "";//yyyy/mm/dd
    $.ajax({
        url : context_path + "/caseAllocation/queryCase",
        data : {
            "allocationStatus" : allocationStatus,
            "cityCode" : $("#cityCode").val(),
            "areaCode" : $("#areaCode").val(),
            "compCode" : $("#compCode").val(),
            "compName" : $("#compName").val(),
            "agent" : agent,
            "startDate" : startDate,
            "endDate" : endDate
        },
        type : "POST",
        dataType : "html",
        success : function (result) {
            $("#checkedCaseList").empty();
            $("#unCheckedCase").empty();
            $("#allocationCase").empty();
            if(allocationStatus == "0")
                $("#unCheckedCase").append(result);
            else
                $("#allocationCase").append(result);
            //设置条数
            setRowNum();
        }
    });
}

//選擇案件
function checkCase(obj) {
    $(obj).attr("checkStatus",$(obj).attr("checkStatus") == "false" ? "true" : "false");
    //设置背景颜色
    $(obj).css("background-color",$(obj).attr("checkStatus") == "true" ? "#BCEBE3" : "#FFFFFF");
}

//选择案件
function choseCase() {
    var checkedCaseList = document.getElementById("checkedCaseList");
    $(".unChoseCase").each(function () {
        if($(this).attr("checkStatus") == "true"){
            //设置为选中的属性
            checkCase(this);
            $(this).attr("class","choseCase");
            //复制数据
            checkedCaseList.appendChild($(this)[0].cloneNode(true));
            $(this).remove();
        }
    });
    //设置条数
    setRowNum();
    //排序
    var orderType = $("#checkedCaseOrderType").attr("orderType");
    showOrder(orderType,"applyTime","checkedCaseList");
}

//刪除
function unChoseCase() {
    var unCheckedCaseList = document.getElementById("unCheckedCaseList");
    $(".choseCase").each(function () {
        if($(this).attr("checkStatus") == "true"){
            var choseRowNum = $(this).attr("rowNum");
            var choseRow = null;
            //设置为选中的属性
            checkCase(this);
            $(".unChoseCase").each(function () {
                if(choseRow == null)
                    choseRow =  $(this);
                var unChoseRowNum = $(this).attr("rowNum");
                if(unChoseRowNum < choseRowNum)
                    choseRow =  $(this);
                else
                    return;
            });
            //选择案件没有数据时往 tbody后添加
            if(choseRow == null)
                choseRow =  $("#unCheckedCaseList");
            $(this).attr("class","unChoseCase");
            unCheckedCaseList.appendChild($(this)[0].cloneNode(true));
            $(this).remove();
        }
    });
    //设置条数
    setRowNum();
    //排序
    var orderType = $("#unCheckedCaseOrderType").attr("orderType");
    showOrder(orderType,"applyTime","unCheckedCaseList");
}

//设置条数
function setRowNum() {
    $("#unCheckNumText").html($(".unChoseCase").length);
    $("#checkNumText").html($(".choseCase").length);
    $("#checkAllocationNumText").html($(".choseCase").length);
}

//選擇分配人員
function choseUser(obj,type) {
    $("." + type).each(function () {
        $(this).css("background-color","#FFFFFF");
    });
    $(obj).css("background-color","#BCEBE3");
    var key = $(obj).attr(type);
    //设置分配人
    if("area" != type) {
        $("#allocationUser").val(key);
        return;
    }
    $.ajax({
        url : context_path + "/caseAllocation/findAllocationUser",
        data : { "type" : type,
                  "key": key
        },
        dataType : "json",
        type : "POST",
        success : function (result) {
            $("#allocationUser").val("");
            if("area" == type) {
                $("#groupList").empty();
                $("#userList").empty();
            } else if("group" == type){
                $("#userList").empty();
            }
            //塞数据
            var table = document.getElementById("area" == type ? "groupList" : "userList");
            for(var index in result){
                var userTr = document.createElement("tr");
                userTr.setAttribute("class","area" == type ? "group" : "agentUser");
                userTr.setAttribute("onclick","choseUser(this,'"+("area" == type ? "group" : "agentUser")+"')");
                var userTd = document.createElement("td");
                for(var key in result[index]) {
                    if(result[index] == '')
                        return;
                    userTr.setAttribute("area" == type ? "group" : "agentUser", key);
                    userTd.innerText = result[index][key];
                }
                userTr.appendChild(userTd);
                table.appendChild(userTr);
            }
        }
    });
}

//確認分配
function saveAllocation() {
    if($(".choseCase").length <= 0 ) {
        alertInfo_1("提示","請選擇授信戶");
        return;
    }
    if($("#allocationUser").val() == '' ) {
        var msg = "";
        if(!$("#areaList").is(":hidden") && $(".area").length >0 && $("#areaList").find("tr[style='background-color: rgb(188, 235, 227);']").text() == "") {
            alertInfo_1("提示","請選擇區中心");
            return;
        }
        if(!$("#groupList").is(":hidden") && $(".group").length >0 && $("#groupList").find("tr[style='background-color: rgb(188, 235, 227);']").text() == "") {
            alertInfo_1("提示","請選擇業務組別");
            return;
        }
        if(!$("#userList").is(":hidden") && $(".agentUser").length >0 && $("#userList").find("tr[style='background-color: rgb(188, 235, 227);']").text() == "") {
            alertInfo_1("提示","請選擇經辦人");
            return;
        }
        return;
    }
    var caseList = "";
    $(".choseCase").each(function () {
        caseList += (caseList == "" ? "" : ",") + $(this).attr("caseNum");
    });
    confirmInfo_1("提示","您確認分配嗎？",function() {
        $.ajax({
            url : context_path + "/caseAllocation/saveAllocation",
            type : "POST",
            data : {
                "userCode" : $("#allocationUser").val(),
                "caseList" : caseList
            },
            dataType : "json",
            success : function (result) {
                if(result)
                    alertInfo_1("提示","分配成功",function () {
                        queryCase();
                        $("#groupList").empty();
                        $("#allocationUser").val("");
                        $(".agentUser").each(function () {
                            $(this).css("background-color","#FFFFFF");
                        });
                        $(".area").each(function () {
                            $(this).css("background-color","#FFFFFF");
                        });
                    })
            }
        })
    });
}

//列為黑名單
function showBlackList() {
    if($(".unChoseCase[checkstatus='true']").length <= 0 ) {
        alertInfo_1("提示","請在未分配列表中選擇要列為不可開發名單的授信戶");
        return;
    }
    $("#remark").val("");//清空備註
    openDialog("blackList","提示", 500,300);
}

//黑名單備註可輸入數字的更新
function setCount(obj) {
    var count = $("#count");
    count.html(500-$(obj).val().length);
}

//保存黑名單
function saveBlackList() {
    var caseList = "";
    if("" == $("#remark").val()){
        alertInfo_1("提示","請輸入說明");
        return;
    }
    $(".unChoseCase[checkstatus='true']").each(function () {
        caseList += (caseList == "" ? "" : ",") + $(this).attr("caseNum");
    });
    $.ajax({
        url: context_path + "/caseAllocation/saveBlackList",
        type: "POST",
        data: {
            "caseList": caseList,
            "remark": $("#remark").val()
        },
        async : false,
        success: function (result) {
            alertInfo_1("提示",result ? "列入不可開發名單成功！" : "列入不可開發名單失敗！",clsoeBlackList());
        }

    });
    queryCase();
}

//關閉黑名單彈窗
function clsoeBlackList() {
    $("#blackList").dialog('close');
}

//單筆回收
function singleRecycling() {
    var caseList = "";
    if($(".choseCase[checkstatus='true']").length != 1) {
        alertInfo_1("提示","請只選擇一筆案件");
        return;
    }
    $(".choseCase[checkstatus='true']").each(function () {
        caseList += (caseList == "" ? "" : ",") + $(this).attr("caseNum");
    });
    confirmInfo({"title":"提示","content":"您確定回收至未分配列表嗎？","okCallback":function () {
        $.ajax({
            url: context_path + "/caseAllocation/caseRecycling",
            type: "POST",
            data: {
                "caseList": caseList
            },
            async : false,
            success: function (result) {
                alertInfo_1("提示",result.returnCode ? "回收成功！" : "所選案件已排程，無法回收！",queryCase());
            }
        })
    },"okButtonName":"回收","okButtonClass":"button_gray"
    });
}

//關閉彈窗
function clsoeInProcessCase() {
    $("#inProcessCaseDiv").dialog('close');
}

//檢視案件
function showCaseDetail() {
    if($("tr[checkstatus='true']").length != 1) {
        alertInfo_1("提示","請只選擇一筆案件");
        return;
    }
    $.ajax({
        url: context_path + "/caseAllocation/showCaseDetail",
        type: "POST",
        data: {
            "caseNum": $("tr[checkstatus='true']").attr("caseNum")
        },
        dataType : "html",
        async : false,
        success: function (result) {
            $("#caseDetail").empty();
            $("#caseDetail").html(result);
            openDialog("caseDetail","資訊詳情", 1000,500);
        }
    });
    formatMoney();
}

//關閉黑名單彈窗
function clsoeCaseDetail() {
    $("#caseDetail").dialog('close');
}

//排序
function showOrder(obj,orderItem,orderTable) {
    var orderType = "";
    if("desc" == obj || "asc" == obj) {
        orderType = obj;
    } else {
        orderType = "desc" == $(obj).attr("orderType")? "asc" : "desc";
        $(obj).attr("orderType",orderType);
        $(obj).find("img").attr("src","../images/caseAllocation/" + orderType + ".jpg");
    }
    var tempList = [];
    $("." + orderItem).each(function () {
        var temp = {"orderItem":$(this).html(),"caseNum": $(this).parent("tr").attr("caseNum")};
        tempList.push(temp);
    });
    tempList.sort(function (a, b) {
        return ("desc" == orderType ? a.orderItem < b.orderItem : a.orderItem > b.orderItem) ? 1 : -1;
    });
    for(var i in tempList) {
        var tempTr = $("#"+orderTable).find("tr[caseNum= "+tempList[i].caseNum+"]");
        $("#"+orderTable).remove("tr[caseNum= "+tempList[i].caseNum+"]");
        $("#"+orderTable).append(tempTr);
    }

}

function formatMoney(){
    $(".money").text(function(i,v){
        return v.split(".")[0].replace(/(?!^)(?=(\d{3})+($|\.))/g,",") + (v.split(".").length > 1 ?  ("." + v.split(".")[1]) :"");
    });
}

