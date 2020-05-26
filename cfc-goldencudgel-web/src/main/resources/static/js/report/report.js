$(function() {
    queryReportConfigList();
    getLoginPage();
})

function queryReportConfigList(curPage) {
    var reportName = $("#queryReportName").val();
    if (curPage == "" || curPage == undefined || curPage == "undefined") {
        curPage = 1;
    }
    bodymask();
    $.ajax({
        url: context_path + "/report/queryReportConfigList",
        type: "post",
        dataType: "html",
        data: {"reportName": reportName, "curPage": curPage},
        success: function(result) {
            $("#report_list").empty();
            $("#report_list").html(result);
            bodyunmask();
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            bodyunmask();
            alert(textStatus + " " + XMLHttpRequest.readyState + " "
                    + XMLHttpRequest.status + " " + errorThrown);
        }
    });
}

function showAddReportConfigDialog(type, obj){
    if (type === "add") {
        openDialog("addReportConfig", "新增報表配置", 800, 360);
        $("#reportName").val("");
        $("#reportPath").val("");
        $("#reportType").val("");
        $("#queryDateLabel").val("");
        $("#queryDateFormat").val("");
        $("#reportQueryRegion").val("");
        $("#reportDownload").val("");
        $("input[name='reportAuthority']").each(function() {
           this.checked = true;
        });
        $("#operateType").val("add");
    } else {
        openDialog("addReportConfig", "更新報表配置", 800, 360);
        $("#reportId").val($(obj).attr("reportId"));
        $("#reportName").val($(obj).attr("reportCode"));
        $("#reportPath").val($(obj).attr("reportPath"));
        $("#reportType").val($(obj).attr("reportType").substring(0, 1));
        $("#queryDateLabel").val($(obj).attr("queryDateLabel"));
        $("#queryDateFormat").val($(obj).attr("queryDateFormat"));
        $("#reportQueryRegion").val($(obj).attr("reportQueryRegion"));
        $("#reportDownload").val($(obj).attr("reportDownload"));
        var roleStr = $(obj).attr("roleStr");
        $("input[name='reportAuthority']").each(function() {
            if (roleStr.indexOf($(this).val()) != -1) {
                this.checked = true;
            } else {
                this.checked = false;
            }
            
        });
        $("#operateType").val("update");
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

function saveReportConfig() {
    var operateType = $("#operateType").val();
    var reportId = $("#reportId").val();
    var reportCode = $("#reportName option:selected").val();
    var reportName = $("#reportName option:selected").text();
    var reportPath = $("#reportPath").val();
    var reportType = $("#reportType").val();
    var queryDateLabel = $("#queryDateLabel").val();
    var queryDateFormat = $("#queryDateFormat").val();
    var reportQueryRegion = $("#reportQueryRegion").val();
    var reportDownload = $("#reportDownload").val();
    var roleStr = "";
    $("input[name='reportAuthority']:checked").each(function() {
        roleStr += $(this).val() + "|";
    });
    if (reportName == "undefined" ||reportName == null || reportName == "" || reportName.length == 0) {
        alertInfo("提示","報表名稱不能為空!");
        return false;
    }
    
    if (reportPath == "undefined" || reportPath == null || reportPath == "" || reportPath.length == 0) {
        alertInfo("提示","報表路徑不能為空!");
        return false;
    }
    
    if (reportType == "undefined" || reportType == null || reportType == "" || reportType.length == 0) {
        alertInfo("提示","報表類型不能為空!");
        return false;
    }
    if (queryDateLabel == "undefined" || queryDateLabel == null || queryDateLabel == "" || queryDateLabel.length == 0) {
        alertInfo("提示","日期查詢標籤不能為空!");
        return false;
    }
    if (queryDateFormat == "undefined" || queryDateFormat == null || queryDateFormat == "" || queryDateFormat.length == 0) {
        alertInfo("提示","日期查詢格式不能為空!");
        return false;
    }
    
    if (reportQueryRegion == "undefined" || reportQueryRegion == null || reportQueryRegion == "" || reportQueryRegion.length == 0) {
        alertInfo("提示","是否需要查詢區間不能為空!");
        return false;
    }
    if (reportDownload == "undefined" || reportDownload == null || reportDownload == "" || reportDownload.length == 0) {
        alertInfo("提示","是否需要下載不能為空!");
        return false;
    }
    if (roleStr == "undefined" || roleStr == null || roleStr == "" || roleStr.length == 0) {
        alertInfo("提示","是否報表權限不能為空!");
        return false;
    }
    
    if (operateType === "add") {
        $.ajax({
            url: context_path + "/report/saveReportConfig",
            type: "post",
            dataType: "json",
            data: {"reportCode": reportCode, "reportName": reportName, "reportPath": reportPath, "reportType": reportType, "queryDateLabel": queryDateLabel, "queryDateFormat": queryDateFormat, 
                "reportQueryRegion": reportQueryRegion, "reportDownload": reportDownload, "roleStr": roleStr},
            success: function(result) {
                if (result.flag) {
                    alertInfo("提示", result.msg, function () {;
                        $("#addReportConfig").dialog('close');
                    });
                    queryReportConfigList();
                } else {
                    alertInfo("提示", result.msg);
                }
            },
            
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                alertInfo("提示", textStatus);
            }
        });
    } else {
        $.ajax({
            url: context_path + "/report/updateReportConfig",
            type: "post",
            dataType: "json",
            data: {"reportId": reportId, "reportCode": reportCode, "reportName": reportName, "reportPath": reportPath, "reportType": reportType, "queryDateLabel": queryDateLabel, 
                "queryDateFormat": queryDateFormat, "reportQueryRegion": reportQueryRegion, "reportDownload": reportDownload, "roleStr": roleStr},
            success: function(result) {
                if (result.flag) {
                    alertInfo("提示", result.msg, function () {;
                        $("#addReportConfig").dialog('close');
                    });
                    queryReportConfigList();
                } else {
                    alertInfo("提示", result.msg);
                }
            },
            
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                alertInfo("提示", textStatus);
            }
        });
    }
    
}

function showSpotfire(obj) {
    var reportPath = $(obj).attr("reportPath");
    var reportCode = $(obj).attr("reportCode");
    var reportName = $(obj).attr("reportName");
    var queryDateLabel = $(obj).attr("queryDateLabel");
    var queryDateFormat = $(obj).attr("queryDateFormat");
    var reportQueryRegion = $(obj).attr("reportQueryRegion");
    var reportDownload = $(obj).attr("reportDownload");
    window.location.href = context_path + "/report/showSpotfire?reportPath=" + encodeURI(reportPath) + "&reportCode=" + reportCode + "&reportName=" + encodeURI(reportName)
        + "&queryDateLabel=" + queryDateLabel + "&queryDateFormat=" + queryDateFormat + "&reportQueryRegion=" + reportQueryRegion + "&reportDownload=" + reportDownload;
}

function delReportConfig(obj) {
    var reportId = $(obj).attr("reportId");
    alertInfoCOPY("提示","請確認是否刪除？",function deleteSelectRole() {
        $(obj).removeAttr("onclick");
    $.ajax({
        url: context_path + "/report/delReportConfig",
        type: "post",
        dataType: "json",
        data: {"reportId": reportId},
        success: function(result) {
            if (result.flag) {
                alertInfo("提示", result.msg);
                queryReportConfigList();
            } else {
                alertInfo("提示", result.msg);
            }
        },
        
        error: function(XMLHttpRequest, textStatus, errorThrown) {
//            bodyunmask();
            alertInfo("提示", textStatus);
        }
    })
})
}

function findPage(curpage){
    queryReportConfigList(curpage);
}