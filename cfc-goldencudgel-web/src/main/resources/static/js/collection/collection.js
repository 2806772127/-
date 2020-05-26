$(function(){
    $("#startImg").click(function() {
        $("input[name='startDate']").focus();
    });
    
    $("#endImg").click(function() {
        $("input[name='endDate']").focus();
    });
    
    $("input[name='startDate']").datetimepicker({
        onClose: function(selectedDate) {
            $("input[name='endDate']").datepicker("option", "minDate", selectedDate);
        }
    });
    $("input[name='endDate']").datetimepicker({
        onClose: function(selectedDate) {
            $("input[name='startDate']").datepicker("option", "maxDate", selectedDate);
        }
    });

    $(".money").text(function(i,v){
        return v.replace(/(?!^)(?=(\d{3})+($|\.))/g,",");
    });
    
    var backFlag = $("#backFlag").val();
    var curPage = 1;
    if ("-1" == backFlag) {
        $("#userCode").val(localStorage.getItem("collectionUserCode"));
        $("#groupCode").val(localStorage.getItem("collectionGroupCode"));
        $("#areaCode").val(localStorage.getItem("collectionAreaCode"));
        $("#compilationNo").val(localStorage.getItem("collectionCompilationNo"));
        $("#visitName").val(localStorage.getItem("collectionVisitName"));
        $("#startDate").val(localStorage.getItem("collectionStartDate"));
        $("#endDate").val(localStorage.getItem("collectionEndDate"));
        curPage = localStorage.getItem("collectionCurPage");
        
        // 清除所有缓存
        localStorage.clear();
    }
    
    findPage(curPage);
 });

function findPage(curpage) {
    viewQueryVisit(curpage);
}


//查询拜访笔记纪录
function viewQueryVisit(curpage) {
    var createUsers=$("#userCode").val();
    var userGroups=$("#groupCode").val();
    var userAreas=$("#areaCode").val();
    var curPage = curpage;
    var compilationNo = $("#compilationNo").val();
    var visitName = $("#visitName").val();
    var startDate =$("#startDate").val();
    var endDate =$("#endDate").val();
    $.ajax({
        url:context_path + "/collectionQuery/viewQueryVisit",
        type:"post",
        data:{"compilationNo":compilationNo,"visitName":visitName,
            "startDate":startDate,"endDate":endDate,"curPage":curPage,
        "createUsers":createUsers,"userGroups":userGroups,"userAreas":userAreas
        },
        datatype: "html",
        success: function(result) {
            $("#collectionQuery_list").empty();           
        	$("#collectionQuery_list").html(result);
        	
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            alert(textStatus + " " + XMLHttpRequest.readyState + " "
                + XMLHttpRequest.status + " " + errorThrown);
        }
    });
}




//查看拜访详情页面
function viewVisitDetail(obj) {
    var compilationNo =  $(obj).attr("compilationNo");
    var trandId = typeof $(obj).attr("trandId") === 'undefined' ? '' : $(obj).attr("trandId");
    var appointmentDate = typeof $(obj).attr("appointmentDate") === 'undefined' ? '' : $(obj).attr("appointmentDate");
    
    // 缓存查询条件
    localStorage.setItem("collectionUserCode", $("#userCode").val());
    localStorage.setItem("collectionGroupCode", $("#groupCode").val());
    localStorage.setItem("collectionAreaCode", $("#areaCode").val());
    localStorage.setItem("collectionCompilationNo", $("#compilationNo").val());
    localStorage.setItem("collectionVisitName", $("#visitName").val());
    localStorage.setItem("collectionStartDate", $("#startDate").val());
    localStorage.setItem("collectionEndDate", $("#endDate").val());
    localStorage.setItem("collectionCurPage", $("#page_select").val());
    localStorage.setItem("collectionSelfPage", "Y");
    
    window.location.href =
        context_path + "/collectionQuery/visitDetail?compilationNo="+compilationNo+"&trandId="+trandId + "&appointmentDate=" + appointmentDate;
}

//修改拜访详情页面
function editVisitDetail(obj) {
    var compilationNo =  $(obj).attr("compilationNo");
    var trandId = typeof $(obj).attr("trandId") === 'undefined' ? '' : $(obj).attr("trandId");
    var appointmentDate = typeof $(obj).attr("appointmentDate") === 'undefined' ? '' : $(obj).attr("appointmentDate");
    var type = '2';
    //var applyProduct =  $(obj).attr("applyProduct");
    
    
    // 缓存查询条件
    localStorage.setItem("collectionUserCode", $("#userCode").val());
    localStorage.setItem("collectionGroupCode", $("#groupCode").val());
    localStorage.setItem("collectionAreaCode", $("#areaCode").val());
    localStorage.setItem("collectionCompilationNo", $("#compilationNo").val());
    localStorage.setItem("collectionVisitName", $("#visitName").val());
    localStorage.setItem("collectionStartDate", $("#startDate").val());
    localStorage.setItem("collectionEndDate", $("#endDate").val());
    localStorage.setItem("collectionCurPage", $("#page_select").val());
    localStorage.setItem("collectionSelfPage", "Y");
    
    window.location.href =
        context_path + "/collectionQuery/visitDetail?compilationNo="+compilationNo+"&trandId="+trandId+"&type="+type + "&appointmentDate=" + appointmentDate;
}

function closeImg(id){
    $("#"+id).dialog('close');
        $(".audit").pause();
}
function getGroupList() {
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
           // viewQueryVisit(1);
        },
        fail : function () {
        }
    });
}




function getUserList() {
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
           // viewQueryVisit(1);
        },
        fail : function () {

        }
    });
}