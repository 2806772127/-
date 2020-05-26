$(function(){
    var backFlag = $("#backFlag").val();
    var curPage = 1;
    if ("-1" == backFlag) {
        $("#userCode").val(localStorage.getItem("measureUserCode"));
        $("#groupCode").val(localStorage.getItem("measureGroupCode"));
        curPage = localStorage.getItem("measureCurPage");
        // 清除所有缓存
        localStorage.clear();
    }
    
    querySystemDataList(curPage);
});

function findPage(curpage) {
    querySystemDataList(curpage);
}

/**
 * @description 查詢列表數據
 * @param curPage
 * @returns
 */
function querySystemDataList(curPage) {
    if (curPage!="" && curPage !='undefined') {
        curPage = curPage;
    } else {
        curPage = 1;
    }
    var schemaName=  $("#schemaName").val();
    var tableName=  $("#tableName").val();
    $.ajax({
        url: context_path + "/systemData/querySystemDataList",
        type : "post",
        data : {"schemaName" : schemaName, "tableName" : tableName, "curPage" : curPage},
        datatype: "html",
        success: function(result) {
            $("#systemData_list").empty();
            $("#systemData_list").html(result);
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
          alert(textStatus + " " + XMLHttpRequest.readyState + " "
                  + XMLHttpRequest.status + " " + errorThrown);
        }
    });
}

/**
 * @description 產制系統數據
 * @param obj
 * @returns
 */
function generateData(obj) {
    var schemaName = $(obj).attr("schemaName");
    var tableName = $(obj).attr("tableName");
    // 獲取每行數據
    var curRowObj = $(obj).parent().parent();
    // 每行的第3列
    $(curRowObj).find("td").eq(2).text("請求中");
    // 每行第5列里的span標籤
    var curRowSpanObj = $(curRowObj).find("td").eq(4).find("span");
    // 每行第5列里的a元素
    var curRowAObj = $(curRowObj).find("td").eq(4).find("a.export");
    if (curRowSpanObj.length == 1 && curRowAObj.length == 1) {
        $(curRowObj).find("td").eq(4).find("a").eq(2).css("display", "none");
    }
    
    // 產制之前先更新產制狀態
    $.ajax({
        url: context_path + "/systemData/updateGenerate",
        type: "post",
        data: {"schemaName" : schemaName, "tableName" : tableName, "status" : "4"},
        dataType: "json",
        success: function(result) {
            // 產制狀態更新成功后，再調用接口進行產制
            if (result.code) {
                $.ajax({
                    url: context_path + "/systemData/generateData",
                    type: "post",
                    data: {"schemaName" : schemaName, "tableName" : tableName},
                    dataType: "json",
                    async: true,
                    success: function(result) {
                        if (result.returnFlag) {
                            $(curRowObj).find("td").eq(2).text("產制成功");
                            $(curRowObj).find("td").eq(3).text(result.updateTime);
                            if (curRowSpanObj.length == 1 && curRowAObj.length == 1) {
                                $(curRowObj).find("td").eq(4).find("a").eq(2).css("display", "");
                            } else if (curRowSpanObj.length == 1 && curRowAObj.length == 0) {
                                $(curRowObj).find("td").eq(4).append("<a style=\"cursor: pointer;display:inline;\" class=\"export\" schemaName=\"" + schemaName + "\" tableName=\"" + tableName + "\" onclick=\"javascript:downloadData(this);\">匯出</a>");
                            }
                        } else {
                            if (result.returnCode == "4") {
                                $(curRowObj).find("td").eq(2).text("產制失敗");
                                $(curRowObj).find("td").eq(3).text(result.updateTime);
                            }
                        }
                    }
                });
            } else {
                // 產制狀態更新失敗后，當做產制失敗處理
                $.ajax({
                    url: context_path + "/systemData/updateGenerate",
                    type: "post",
                    data: {"schemaName" : schemaName, "tableName" : tableName, "status" : "2"},
                    dataType: "json",
                    async: true,
                    success: function(result) {
                        if (result.code) {
                            $(curRowObj).find("td").eq(2).text("產制失敗");
                        }
                    },
                    error: function(XMLHttpRequest, textStatus, errorThrown) {
                        alert(textStatus + " " + XMLHttpRequest.readyState + " "
                                + XMLHttpRequest.status + " " + errorThrown);
                    }
                });
            }
        }
    });
    
    
}

/**
 * @description 匯出系統數據
 * @param obj
 * @returns
 */
function downloadData(obj) {
    var schemaName = $(obj).attr("schemaName");
    var tableName = $(obj).attr("tableName");
    window.location.href = context_path + "/systemData/downloadData?schemaName=" + schemaName + "&tableName=" + tableName;
}

function addSystemData() {
    openDialog("addSystemData", "新增資料表", 800, 360);
}

function saveSystemData() {
    var data = serializeFormJSON("systemDataInfo");
    $.ajax({
        url: context_path + "/systemData/updateSystemData",
        type: "post",
        data: JSON.stringify(data),
        dataType: "json",
        contentType: "application/json",
        success: function(result) {
            alertInfo("提示", result.returnMsg, function () {
                $("#addSystemData").dialog('close');
            });
            querySystemDataList();
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            alert(textStatus + " " + XMLHttpRequest.readyState + " "
                    + XMLHttpRequest.status + " " + errorThrown);
        }
    });
}

function closeWindow(id) {
    $("#" + id).dialog('close');
}

function deleteSystemData(obj) {
    var systemDataId = $(obj).attr("systemDataId");
    $.ajax({
        url: context_path + "/systemData/deleteSystemData",
        type: "post",
        data: {"systemDataId" : systemDataId},
        dataType: "json",
        success: function(result) {
            alert(result.returnMsg);
            if (result.returnFlag) {
                querySystemDataList();
            }
            
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            alert(textStatus + " " + XMLHttpRequest.readyState + " "
                    + XMLHttpRequest.status + " " + errorThrown);
        }
    });
}
