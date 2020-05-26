$(function(){
    queryPendingCase(1);

    //分配时间
    $("#allocationStartImg").click(function() {
        $("input[name='allocationStartDate']").focus();
    });
    $("#allocationEndImg").click(function() {
        $("input[name='allocationEndDate']").focus();
    });
    $("input[name='allocationStartDate']").datetimepicker({
        onClose: function(selectedDate) {
            $("input[name='allocationEndDate']").datepicker("option", "minDate", selectedDate);
        }
    });
    $("input[name='allocationEndDate']").datetimepicker({
        onClose: function(selectedDate) {
            $("input[name='allocationStartDate']").datepicker("option", "maxDate", selectedDate);
        }
    });
    //进件时间
    $("#applyStartImg").click(function() {
        $("input[name='applyStartDate']").focus();
    });
    $("#applyEndImg").click(function() {
        $("input[name='applyEndDate']").focus();
    });
    $("input[name='applyStartDate']").datetimepicker({
        onClose: function(selectedDate) {
            $("input[name='applyEndDate']").datepicker("option", "minDate", selectedDate);
        }
    });
    $("input[name='applyEndDate']").datetimepicker({
        onClose: function(selectedDate) {
            $("input[name='applyStartDate']").datepicker("option", "maxDate", selectedDate);
        }
    });
    //註記時間
    $("#handleStartImg").click(function() {
        $("input[name='handleStartDate']").focus();
    });
    $("#handleEndImg").click(function() {
        $("input[name='handleEndDate']").focus();
    });
    $("input[name='handleStartDate']").datetimepicker({
        onClose: function(selectedDate) {
            $("input[name='handleEndDate']").datepicker("option", "minDate", selectedDate);
        }
    });
    $("input[name='handleEndDate']").datetimepicker({
        onClose: function(selectedDate) {
            $("input[name='handleStartDate']").datepicker("option", "maxDate", selectedDate);
        }
    });
});

//查詢數據
function queryPendingCase(curPage) {
    if(curPage!="" && curPage!='undefined'){
        curPage = curPage;
    }else{
        curPage = 1;
    }
    var allocationStartDate = $("input[name='allocationStartDate']").val();//yyyy/mm/dd
    var allocationEndDate = $("input[name='allocationEndDate']").val();//yyyy/mm/dd
    var applyStartDate = $("input[name='applyStartDate']").val();//yyyy/mm/dd
    var applyEndDate = $("input[name='applyEndDate']").val();//yyyy/mm/dd
    var handleStartDate = $("input[name='handleStartDate']").val();//yyyy/mm/dd
    var handleEndDate = $("input[name='handleEndDate']").val();//yyyy/mm/dd
    $.ajax({
        url : context_path + "/caseAllocation/queryPendingCase",
        data : {
            "allocationType" : $("#allocationType").val(),
            "cityCode" : $("#cityCode").val(),
            "areaCode" : $("#areaCode").val(),
            "compCode" : $("#compCode").val(),
            "compName" : $("#compName").val(),
            "allocationStartDate" : allocationStartDate,
            "allocationEndDate" : allocationEndDate,
            "applyStartDate" : applyStartDate,
            "applyEndDate" : applyEndDate,
            "handleStartDate" : handleStartDate,
            "handleEndDate" : handleEndDate,
            "handleFlag" : $("#handleFlag").val(),
            "curPage" : curPage
        },
        type : "POST",
        dataType : "html",
        async : false,
        success : function (result) {
            $("#queryResult").empty();
            $("#queryResult").html(result);
        }
    });
    updateCaseChose();
}

function  findPage(curPage) {
    queryPendingCase(curPage);
}

//標記為已處理
function caseDetail(obj) {
    $.ajax({
        url: context_path + "/caseAllocation/showCaseDetail",
        type: "POST",
        data: {
            "caseNum": $(obj).parents("tr").attr("caseNum")
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

//標註案件
function handleCase(obj) {
    confirmInfo({"title":"提示","content":"您確定標註【已處理】嗎？","okCallback":function () {
            $.ajax({
                url: context_path + "/caseAllocation/handleCase",
                type: "POST",
                data: {
                    "caseNum": $(obj).parents("tr").attr("caseNum")
                },
                dataType : "html",
                async : false,
                success: function (result) {
                    // if(result) {
                    //     alertInfo_1("提示","標註成功！");
                    // }
                }
            });
            queryPendingCase(1);
        },"okButtonName":"確認","okButtonClass":"button_gray"
    });
}

//案件回收列表
var caseList = new Array();

//單選
function checkedCase(obj) {
    var caseNum = $(obj).parents("tr").attr("caseNum");
    var flag = $(obj).is(':checked');
    updateCaseList(flag,caseNum);
}

//全選
function checkAllCase(obj) {
    var flag = $(obj).is(':checked');
    $(".caseCheckbox").each(function () {
        $(this).prop("checked",flag);
        var caseNum = $(this).parents("tr").attr("caseNum");
        updateCaseList(flag,caseNum);
    })
}

function updateCaseList(flag,caseNum) {
    if(flag) {
        caseList.push(caseNum);
    } else {
        for(var i = 0,len = caseList.length;i<len ;i++ ) {
            if(caseList[i] == caseNum) {
                caseList.splice(i,1);
                break;
            }
        }
    }
}

//多筆回收
function wholeRecycling() {
    if(caseList.length<=0) {
        alertInfo_1("提示","請至少勾選一筆或多筆案件");
        return;
    }
    confirmInfo({"title":"提示","content":"您確定回收至未分配列表嗎？","okCallback":function () {
            $.ajax({
                url: context_path + "/caseAllocation/caseRecycling",
                type: "POST",
                data: {
                    "caseList": caseList.toString()
                },
                async : false,
                success: function (result) {
                    if(result.returnCode)
                        alertInfo_1("提示", "回收成功",function (){
                            caseList.length = 0;//清空选中列表
                            queryPendingCase();
                        });
                    else {
                        $("#inProcessCase").empty();
                        $("#inProcessCase").html(result.returnMessage);
                        openDialog("inProcessCaseDiv","提示", 400,300);
                    }
                }
            })
        },"okButtonName":"回收","okButtonClass":"button_gray"
    });
}

//查询案件后更新选中按钮
function updateCaseChose() {
    for(var i = 0,len = caseList.length;i<len ;i++ ) {
        $(".caseCheckbox").each(function () {
            var caseNum = $(this).parents("tr").attr("caseNum");
            if (caseList[i] == caseNum) {
                $(this).prop("checked",true);
            }
        })
    }
}

function exportCase() {
    var allocationStartDate = $("input[name='allocationStartDate']").val();//分配时间
    var allocationEndDate = $("input[name='allocationEndDate']").val();//分配时间
    var applyStartDate = $("input[name='applyStartDate']").val();//进件时间
    var applyEndDate = $("input[name='applyEndDate']").val();//进件时间
    window.location.href = context_path + "/caseAllocation/exportCase?allocationType=" + $("#allocationType").val() + "&cityCode=" + $("#cityCode").val()+ "&areaCode=" + $("#areaCode").val()
        + "&compCode=" + $("#compCode").val()+ "&compName=" + $("#compName").val()+ "&allocationStartDate=" + allocationStartDate+ "&allocationEndDate=" + allocationEndDate
        + "&applyStartDate=" + applyStartDate+ "&applyEndDate=" + applyEndDate;
}