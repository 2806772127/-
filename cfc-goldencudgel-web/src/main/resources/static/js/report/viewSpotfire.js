var app;
var doc;
var isSelectChangeEvent = false;
$(function() {
    bodymask();
    var queryDateFormat = $("#queryDateFormat").val();
    if ("1" == queryDateFormat) {
        var today = new Date();
        var curYear = today.getFullYear();
        
        if ($("#reportCode").val() != "SCPBYF") {
            dynamisYearSelect("startDate", curYear, "start", curYear);
            dynamisYearSelect("endDate", curYear, "end", curYear);
        }
        $("#startDate").on("change", function() {
            var selectYear = $(this).val();
            var endSelectYear = $("#endDate").val();
            dynamisYearSelect("startDate", endSelectYear, "start", selectYear);
            dynamisYearSelect("endDate", selectYear, "end", endSelectYear);
            if ($("#reportCode").val() == "SCPBYF") {
                changeBatchNo();
            } else if ($("#reportCode").val() == "HZL") {
                changeHzhQueryParams();
            } else if ($("#reportCode").val() == "PJGSTJB") {
                changePJGSTJBQueryParams();
            } else {
                changeQueryParams();
            }
            
            $("#startDate").blur();
        });
        $("#endDate").on("change", function() {
            var startSelectYear = $("#startDate").val();
            var selectYear = $(this).val();
            dynamisYearSelect("startDate", selectYear, "start", startSelectYear);
            dynamisYearSelect("endDate", startSelectYear, "end", selectYear);
            if ($("#reportCode").val() == "SCPBYF") {
                changeBatchNo();
            } else if ($("#reportCode").val() == "HZL") {
                changeHzhQueryParams();
            } else if ($("#reportCode").val() == "PJGSTJB") {
                changePJGSTJBQueryParams();
            } else {
                changeQueryParams();
            }
            $("#endDate").blur();
        });
        
    } else if ("2" == queryDateFormat) {
        $("#startDate, #startImg").click(function() {
            $("#startDateBox").css("display", "block");
        });
        
        $("#endDate, #endImg").click(function() {
            var endDateInput = document.getElementById("endDate");
            var dateTd = document.getElementById("dateTd");
            var leftOffset = dateTd.offsetLeft + endDateInput.offsetLeft + 50;
            $("#endDateBox").css("left", leftOffset + "px");
            $("#endDateBox").css("display", "block");
        });
        
        $(".date-close-btn").click(function() {
            if ($("#reportCode").val() == "SCPBYF") {
                changeBatchNo();
            } else if ($("#reportCode").val() == "HZL") {
                changeHzhQueryParams();
            } else if ($("#reportCode").val() == "PJGSTJB") {
                changePJGSTJBQueryParams();
            } else {
                changeQueryParams();
            }
            $(".date-box").css("display", "none");
        });
        
        // 当鼠标在日历框外点击使，关闭日历框
        $("body").click(function(event) {
            var x = event.clientX;
            var y = event.clientY;
            var targetId = $(event.target).attr("id");
            
            if ($("#startDateBox").css("display") != "none" && targetId != "startDate" && targetId != "startImg" && !isSelectChangeEvent) {
                var startDiv = document.getElementById("startDateBox");
                var startDivx1 = startDiv.offsetLeft;
                var startDivy1 = startDiv.offsetTop;
                var startDivx2 = startDiv.offsetLeft + startDiv.offsetWidth;
                var startDivy2 = startDiv.offsetTop + startDiv.offsetHeight;
                
                if( x < startDivx1 || x > startDivx2 || y < startDivy1 || y > startDivy2){
                    if ($("#reportCode").val() == "SCPBYF") {
                        changeBatchNo();
                    } else if ($("#reportCode").val() == "HZL") {
                        changeHzhQueryParams();
                    } else if ($("#reportCode").val() == "PJGSTJB") {
                        changePJGSTJBQueryParams();
                    } else {
                        changeQueryParams();
                    }
                    $("#startDateBox").css("display", "none");
                }
            }
            
            if ($("#endDateBox").css("display") != "none" && targetId != "endDate" && targetId != "endImg" && !isSelectChangeEvent) {
                var endDiv = document.getElementById("endDateBox");
                var endDivx1 = endDiv.offsetLeft;
                var endDivy1 = endDiv.offsetTop;
                var endDivx2 = endDiv.offsetLeft + endDiv.offsetWidth;
                var endDivy2 = endDiv.offsetTop + endDiv.offsetHeight;
                if( x < endDivx1 || x > endDivx2 || y < endDivy1 || y > endDivy2){
                    if ($("#reportCode").val() == "SCPBYF") {
                        changeBatchNo();
                    } else if ($("#reportCode").val() == "HZL") {
                        changeHzhQueryParams();
                    } else if ($("#reportCode").val() == "PJGSTJB") {
                        changePJGSTJBQueryParams();
                    } else {
                        changeQueryParams();
                    }
                    $("#endDateBox").css("display", "none");
                }
            }
            
            if (isSelectChangeEvent) {
                isSelectChangeEvent = false;
            }
        });
        
        $(".select-box select").click(function() {
            isSelectChangeEvent = true;
        });
        
        var today = new Date();
        var curYear = today.getFullYear();
        var curMonth = today.getMonth() + 1;
        
        // 初始化年份下拉框
        dynamisYearSelect("startDateYear", curYear, "start", curYear);
        dynamisYearSelect("endDateYear", curYear, "end", curYear);
        // 初始化月份下拉框
        dynamisMonthSelect("startDateMonth", curMonth, "start", curMonth);
        dynamisMonthSelect("endDateMonth", curMonth, "end", curMonth);
        
        $("#startDateYear").on("change", function() {
            var selectYear = $(this).val();
            var endSelectYear = $("#endDateYear").val();
            var endSelectMonth = $("#endDateMonth").val();
            dynamisYearSelect("startDateYear", endSelectYear, "start", selectYear);
            dynamisYearSelect("endDateYear", selectYear, "end", endSelectYear);
            
            // 年份变化时，将月份重置为1月
            if (selectYear == endSelectYear) {
                dynamisMonthSelect("startDateMonth", endSelectMonth, "start", 1);
                dynamisMonthSelect("endDateMonth", endSelectMonth, "end", endSelectMonth);
            } else {
                dynamisMonthSelect("startDateMonth", 12, "start", 1);
                dynamisMonthSelect("endDateMonth", 1, "end", endSelectMonth);
            }
            
            var startSelectMonth = $("#startDateMonth").val();
            $("#startDate").val(selectYear + "/" + startSelectMonth);
            $("#startDateYear").blur();
        });
        
        $("#endDateYear").on("change", function() {
            var startSelectYear = $("#startDateYear").val();
            var selectYear = $(this).val();
            var startSelectMonth = $("#startDateMonth").val();
            dynamisYearSelect("startDateYear", selectYear, "start", startSelectYear);
            dynamisYearSelect("endDateYear", startSelectYear, "end", selectYear);
            
            // 年份变化时，将月份重置为1月
            if (startSelectYear == selectYear) {
                dynamisMonthSelect("startDateMonth", startSelectMonth, "start", startSelectMonth);
                dynamisMonthSelect("endDateMonth", startSelectMonth, "end", 1);
            } else {
                dynamisMonthSelect("startDateMonth", 12, "start", startSelectMonth);
                dynamisMonthSelect("endDateMonth", 1, "end", 1);
            }
            
            var endSelectMonth = $("#endDateMonth").val();
            $("#endDate").val(selectYear + "/" + endSelectMonth);
            $("#endDateYear").blur();
        });
        
        $("#startDateMonth").on("change", function() {
            var startSelectYear = $("#startDateYear").val();
            var endDateYear = $("#endDateYear").val();
            
            var startSelectMonth = $(this).val();
            var endSelectMonth = $("#endDateMonth").val();
            
            if (startSelectYear == endDateYear) {
                dynamisMonthSelect("startDateMonth", endSelectMonth, "start", startSelectMonth);
                dynamisMonthSelect("endDateMonth", startSelectMonth, "end", endSelectMonth);
            }
            
            $("#startDate").val(startSelectYear + "/" + startSelectMonth);
            $("#startDateMonth").blur();
        });
        
        $("#endDateMonth").on("change", function() {
            var startSelectYear = $("#startDateYear").val();
            var endDateYear = $("#endDateYear").val();
            
            var startSelectMonth = $("#startDateMonth").val();
            var endSelectMonth = $(this).val();
            
            if (startSelectYear == endDateYear) {
                dynamisMonthSelect("startDateMonth", endSelectMonth, "start", startSelectMonth);
                dynamisMonthSelect("endDateMonth", startSelectMonth, "end", endSelectMonth);
            }
            $("#endDate").val(endDateYear + "/" + endSelectMonth);
            $("#endDateMonth").blur();
        });
    } else if ("3" == queryDateFormat) {
        $("#startImg").click(function() {
            $("input[name='startDate']").focus();
        });
        
        $("#endImg").click(function() {
            $("input[name='endDate']").focus();
        });
        
        $("input[name='startDate']").datetimepicker({
            format: "Y/m/d",
            onClose: function(selectedDate) {
                $("input[name='endDate']").datepicker("option", "minDate", selectedDate);
                if ($("#reportCode").val() == "SCPBYF") {
                    changeBatchNo();
                } else if ($("#reportCode").val() == "HZL") {
                    changeHzhQueryParams();
                } else if ($("#reportCode").val() == "PJGSTJB") {
                    changePJGSTJBQueryParams();
                } else {
                    changeQueryParams();
                }
            }
        });
        $("input[name='endDate']").datetimepicker({
            format: "Y/m/d",
            onClose: function(selectedDate) {
                $("input[name='startDate']").datepicker("option", "maxDate", selectedDate);
                if ($("#reportCode").val() == "SCPBYF") {
                    changeBatchNo();
                } else if ($("#reportCode").val() == "HZL") {
                    changeHzhQueryParams();
                } else if ($("#reportCode").val() == "PJGSTJB") {
                    changePJGSTJBQueryParams();
                } else {
                    changeQueryParams();
                }
            }
        });
    }
    
    $("input:checkbox").click(function () { 
        changeHzhQueryParams();
    }); 
    
    
    showSpotfire();
    bodyunmask();
})

function dynamisYearSelect(id, year, type, defaultYear) {
    var dateSelect = document.getElementById(id);
    dateSelect.options.length = 0;
    if (type == "start") {
        for (var i = parseInt(year) - 10; i <= parseInt(year); i++) {
            var option = new Option(i, i);
            dateSelect.appendChild(option);
            if (i == defaultYear) {
                option.setAttribute("selected", "selected");
            }
        }
    } 
    
    if (type == "end") {
        for (var i = parseInt(year); i <= parseInt(year) + 10; i++) {
            var option = new Option(i, i);
            dateSelect.appendChild(option);
            if (i == defaultYear) {
                option.setAttribute("selected", "selected");
            }
        }
    }
}

function dynamisMonthSelect(id, month, type, defaultMonth) {
    var dateSelect = document.getElementById(id);
    dateSelect.options.length = 0;
    if (type == "start") {
        for (var i = 1; i <= parseInt(month); i++) {
            var monthTemp;
            if (i < 10) {
                var monthTemp = "0"+ i;
            } else {
                var monthTemp  = i;
            }
            var option = new Option(monthTemp, monthTemp);
            dateSelect.appendChild(option);
            if (i == defaultMonth) {
                option.setAttribute("selected", "selected");
            }
        }
    } 
    
    if (type == "end") {
        for (var i = parseInt(month); i <= 12; i++) {
            var monthTemp;
            if (i < 10) {
                var monthTemp = "0"+ i;
            } else {
                var monthTemp  = i;
            }
            var option = new Option(monthTemp, monthTemp);
            dateSelect.appendChild(option);
            if (i == defaultMonth) {
                option.setAttribute("selected", "selected");
            }
        }
    }
}

/**
 * @description 打印
 * @returns
 */
function reportPrint() {
    doc.print();
}

function showSpotfire() {
    var webPlayerServerRootUrl = $("#spotfireServerRootUrl").val();
    var customizationInfo = {
            showAbout : false,
            showAnalysisInformationTool : false,
            showAuthor : false,
            showClose : false,
            showCustomizableHeader : false,
            showDodPanel : false,
            showExportFile : false,
            showExportVisualization : false,
            showFilterPanel : false,
            showHelp : false,
            showLogout : false,
            showPageNavigation : true,
            showReloadAnalysis : false,
            showStatusBar : false,
            showToolBar : false,
            showUndoRedo : false,
            draggable: false
    };
    var analysisPath = $("#reportPath").val();
    var searchStartDate = "searchStartDate=\"" + $("#startDate").val() + "\";";
    var parameters = searchStartDate;
    
    // 審查批保逾放率報表屬性
    if ($("#reportCode").val() == "SCPBYF") {
        var batchNo = "batchNo=\"" + $("#batchNoList option:selected").val() + "\";";
        var daiChanRate = $("#daiChanRate").val();
        daiChanRate = parseFloat(daiChanRate) * 0.01;
        var daiChanRateStr = "daiChanRate=\"" + daiChanRate + "\";";
        
        parameters = parameters + batchNo + daiChanRateStr;
    }
    
    // 工時警示表
    if ($("#reportCode").val() == "GSJSB") {
        var workstep = "workstep=\"" + $("#stepId option:selected").val() + "\";";
        parameters = parameters + workstep;
    }
    
    // 平均工時統計表
    if ($("#reportCode").val() == "PJGSTJB") {
        var workstep = "workstep=\"" + $("#stepId option:selected").val() + "\";";
        parameters = parameters + workstep;
    }
    
    if ($("#reportCode").val() == "HZL") {
        // 新舊戶
        var isNewCust = "newCust=\"" + $("#isNewCust option:selected").val() + "\";";
        // 人員類型
        var recordType = "recordType=\"" + $("#recordType option:selected").val() + "\";";
        // 專案別
        var prgCodeStr = "";
        $("input[name='prgCode']:checked").each(function() {
            prgCodeStr = prgCodeStr + "," + $(this).val();
        });
        prgCodeStr = prgCodeStr.substring(1, prgCodeStr.length);
        var prgCodeList = "prodcode=\"" + prgCodeStr + "\";";
        // 報表類型
        var tableType = "tableType=\"" + $("#tableType option:selected").val() + "\";";
         
        parameters = parameters + isNewCust + recordType + prgCodeList + tableType;
    }
    
    if ($("#reportCode").val() == "JDFXD") {
        var prodType = "chanpin=\"" + $("#chanpin option:selected").val() + "\";";
        parameters = parameters + prodType;
    }
    
    if ($("#reportCode").val() == "ZBDCL") {
        var tableType = "tableType=\"" + $("#tableType option:selected").val() + "\";";
        parameters = parameters + tableType;
    }
    
    // 當為企貸件數和企貸進度時，傳產品別屬性
    if ($("#reportCode").val() == "JDQD" || $("#reportCode").val() == "JSQDY" || $("#reportCode").val() == "JSQDN") {
        var prodCode = "prodCode=\"" + $("#prodCode option:selected").val() + "\";";
        parameters = parameters + prodCode;
    }
    
    var applyBookmark = "ApplyBookmark(bookmarkName='bookmark1');";
    parameters = parameters + applyBookmark;
    console.log(parameters);
    
    var reloadInstances = false;
    var apiVersion = $("#spotfireApiVersion").val();
    
    spotfire.webPlayer.createApplication(
            webPlayerServerRootUrl,
            customizationInfo,
            analysisPath,
            parameters,
            reloadInstances,
            apiVersion,
            onReadyCallback,
            onCreateLoginElement 
    );
    
}

function onReadyCallback(response, newApp)
{
    app = newApp;
    app.onOpened(onOpenedCallback);
    app.onError(onErrorCallback);
    
    if(response.status === "OK") {
        doc = app.openDocument("webPlayer", 0);
    } else {
        bodyunmask();
        console.log("Status not OK. " + response.status + ": " + response.messageo );
        window.location.href = context_path + "/report/reportIndex";
    }
    
}

function onOpenedCallback(doc) {

    doc.setDocumentProperty("userCode", $("#userCodeParam").val());
    doc.setDocumentProperty("whereSql", $("#whereSql").val());
    if ($("#endDate").val() != "undefined" && $("#endDate").val() != undefined && $("#endDate").val() != null && $("#endDate").val() != "null" && $("#endDate").val() != "") {
        doc.setDocumentProperty("searchEndDate", $("#endDate").val().trim());
    }
    doc.setDocumentProperty("reportPdfName", $("#reportPdfName").val());
    doc.setDocumentProperty("reportPdfTimestamp", new Date().getTime() + "");
    
    bodyunmask();
}



function onErrorCallback(errorCode, description) 
{   
    console.log(errorCode + ": " + description);
    bodyunmask();
}

function onCreateLoginElement()
{
    console.log("Creating the login element");
    return null;
}

function getGroupList() {
    var reportCode = $("#reportCode").val();
    if (reportCode == "ZBDCL") {
        $.ajax({
            url: context_path + "/report/getGroupList",
            data: {"areaCode" : $("#areaCode").val()},
            type : "POST",
            dataType : "json",
            success : function (result) {
                var groupSelect = document.getElementById("groupCode");
                groupSelect.options.length=0;
                groupSelect.add(new Option("全部",""));
                var groupMap = result[0];
                for(var code in groupMap) {
                    var option = document.createElement("option");
                    option.setAttribute("value", code);
                    option.text= groupMap[code];
                    groupSelect.appendChild(option);
                }

                var userSelect = document.getElementById("userCode");
                userSelect.options.length=0;
                userSelect.add(new Option("全部",""));
                var userMap = result[1];
                for (var code in userMap) {
                    var option = document.createElement("option");
                    option.setAttribute("value", code);
                    option.text = userMap[code];
                    userSelect.appendChild(option);
                }
                if ($("#reportCode").val() == "SCPBYF") {
                    changeBatchNo();
                } else if ($("#reportCode").val() == "HZL") {
                    changeHzhQueryParams();
                } else {
                    changeQueryParams();
                }
            },
            fail : function () {
            }
        });
    } else {
        $.ajax({
            url: context_path + "/missionStroke/getGroupList",
            data: {"areaCode" : $("#areaCode").val()},
            type : "POST",
            dataType : "json",
            success : function (result) {
                var groupSelect = document.getElementById("groupCode");
                groupSelect.options.length=0;
                groupSelect.add(new Option("全部",""));
                var groupMap = result[0];
                for(var code in groupMap) {
                    var option = document.createElement("option");
                    option.setAttribute("value", code);
                    option.text= groupMap[code];
                    groupSelect.appendChild(option);
                }

                var userSelect = document.getElementById("userCode");
                userSelect.options.length=0;
                userSelect.add(new Option("全部",""));
                var userMap = result[1];
                for (var code in userMap) {
                    var option = document.createElement("option");
                    option.setAttribute("value", code);
                    option.text = userMap[code];
                    userSelect.appendChild(option);
                }
                if ($("#reportCode").val() == "SCPBYF") {
                    changeBatchNo();
                } else if ($("#reportCode").val() == "HZL") {
                    changeHzhQueryParams();
                } else {
                    changeQueryParams();
                }
            },
            fail : function () {
            }
        });
    }
    
}

function getUserList() {
    var reportCode = $("#reportCode").val();
    if (reportCode == "ZBDCL") {
        $.ajax({
            url: context_path + "/report/getUserList",
            data: {
                "areaCode" : $("#areaCode").val(),
                "groupCode" : $("#groupCode").val()
            },
            type : "POST",
            dataType : "json",
            success : function (result) {
                var userSelect = document.getElementById("userCode");
                userSelect.options.length=0;
                userSelect.add(new Option("全部",""));
                for(var code in result) {
                    var option = document.createElement("option");
                    option.setAttribute("value",code);
                    option.text= result[code];
                    userSelect.appendChild(option);
                };
                if ($("#reportCode").val() == "SCPBYF") {
                    changeBatchNo();
                } else if ($("#reportCode").val() == "HZL") {
                    changeHzhQueryParams();
                } else {
                    changeQueryParams();
                }
            },
            fail : function () {
    
            }
        });
    } else {
        $.ajax({
            url: context_path + "/missionStroke/getUserList",
            data: {
                "areaCode" : $("#areaCode").val(),
                "groupCode" : $("#groupCode").val()
            },
            type : "POST",
            dataType : "json",
            success : function (result) {
                var userSelect = document.getElementById("userCode");
                userSelect.options.length=0;
                userSelect.add(new Option("全部",""));
                for(var code in result) {
                    var option = document.createElement("option");
                    option.setAttribute("value",code);
                    option.text= result[code];
                    userSelect.appendChild(option);
                };
                if ($("#reportCode").val() == "SCPBYF") {
                    changeBatchNo();
                } else if ($("#reportCode").val() == "HZL") {
                    changeHzhQueryParams();
                } else {
                    changeQueryParams();
                }
            },
            fail : function () {
    
            }
        });
    }
}

function changeQueryParams() {
    doc.setDocumentProperty("searchArea", $("#areaCode option:selected").val());
    doc.setDocumentProperty("searchGroup", $("#groupCode option:selected").val());
    doc.setDocumentProperty("searchUser", $("#userCode option:selected").val());
    doc.setDocumentProperty("searchStartDate", $("#startDate").val().trim());
    if ($("#endDate").val() != "undefined" && $("#endDate").val() != undefined && $("#endDate").val() != null && $("#endDate").val() != "null" && $("#endDate").val() != "") {
        doc.setDocumentProperty("searchEndDate", $("#endDate").val().trim());
    }
    doc.setDocumentProperty("reportPdfName", $("#reportPdfName").val());
    doc.setDocumentProperty("reportPdfTimestamp", new Date().getTime() + "");
    if ($("#reportCode").val() == "JDFXD") {
        doc.setDocumentProperty("chanpin", $("#chanpin option:selected").val());
    }
    
    if ($("#reportCode").val() == "ZBDCL") {
        doc.setDocumentProperty("tableType", $("#tableType option:selected").val());
    }
    
    if ($("#reportCode").val() == "JDQD" || $("#reportCode").val() == "JSQDY" || $("#reportCode").val() == "JSQDN") {
        doc.setDocumentProperty("prodCode", $("#prodCode option:selected").val());
    }
}

function changeBatchNo() {
    doc.setDocumentProperty("batchNo", $("#batchNoList option:selected").val());
    doc.setDocumentProperty("searchStartDate", $("#startDate").val().trim());
    if ($("#endDate").val() != "undefined" && $("#endDate").val() != undefined && $("#endDate").val() != null && $("#endDate").val() != "null" && $("#endDate").val() != "") {
        doc.setDocumentProperty("searchEndDate", $("#endDate").val().trim());
    }
    var daiChanRate = $("#daiChanRate").val();
    daiChanRate = parseFloat(daiChanRate) * 0.01;
    doc.setDocumentProperty("daiChanRate", daiChanRate + "");
    doc.setDocumentProperty("reportPdfName", $("#reportPdfName").val());
    doc.setDocumentProperty("reportPdfTimestamp", new Date().getTime() + "");
    
}

function changeHzhQueryParams() {
    doc.setDocumentProperty("searchArea", $("#areaCode option:selected").val());
    doc.setDocumentProperty("searchGroup", $("#groupCode option:selected").val());
    doc.setDocumentProperty("searchUser", $("#userCode option:selected").val());
    doc.setDocumentProperty("searchStartDate", $("#startDate").val().trim());
    doc.setDocumentProperty("reportPdfName", $("#reportPdfName").val());
    doc.setDocumentProperty("reportPdfTimestamp", new Date().getTime() + "");
    // 新舊戶
    doc.setDocumentProperty("newCust", $("#isNewCust option:selected").val());
    // 人員類型
    doc.setDocumentProperty("recordType", $("#recordType option:selected").val());
    // 專案別
    var prgCodeStr = "";
    $("input[name='prgCode']:checked").each(function() {
        prgCodeStr = prgCodeStr + "," + $(this).val();
    });
    prgCodeStr = prgCodeStr.substring(1, prgCodeStr.length);
    doc.setDocumentProperty("prodcode", prgCodeStr);
    // 報表類型
    doc.setDocumentProperty("tableType", $("#tableType option:selected").val());
}

function changeStepId() {
    doc.setDocumentProperty("workstep", $("#stepId option:selected").val());
    doc.setDocumentProperty("reportPdfName", $("#reportPdfName").val());
    doc.setDocumentProperty("reportPdfTimestamp", new Date().getTime() + "");
}

function changePJGSTJBQueryParams() {
    doc.setDocumentProperty("searchStartDate", $("#startDate").val().trim());
    doc.setDocumentProperty("workstep", $("#stepId option:selected").val());
    doc.setDocumentProperty("reportPdfName", $("#reportPdfName").val());
    doc.setDocumentProperty("reportPdfTimestamp", new Date().getTime() + "");
}

