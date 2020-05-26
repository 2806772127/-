$(function(){
	//initDate();
	
	 $("#startImg").click(function() {
	        $("input[id='startDate']").focus();
	    });
	    $("#startImgs").click(function() {
	        $("input[id='startDates']").focus();
	    });


	    $("#endImg").click(function() {
	        $("input[id='endDate']").focus();
	    });
	    $("#endImgs").click(function() {
	        $("input[id='endDates']").focus();
	    });
	    $("input[id='startDate']").datetimepicker({
	        onClose: function(selectedDate) {
	            $("input[id='endDate']").datepicker("option", "minDate", selectedDate);
	        }
	    });
	    $("input[id='endDate']").datetimepicker({
	        onClose: function(selectedDate) {
	            $("input[id='startDate']").datepicker("option", "maxDate", selectedDate);
	        }
	    });
	    $("input[id='startDates']").datetimepicker({
	        onClose: function(selectedDate) {
	            $("input[id='endDates']").datepicker("option", "minDate", selectedDate);
	        }
	    });
	    $("input[id='endDates']").datetimepicker({
	        onClose: function(selectedDate) {
	            $("input[id='startDates']").datepicker("option", "maxDate", selectedDate);
	        }
	    });
	
	    findSystemLogs(1,"");
	    
});



function  findSystemLogs(curPage,queryFlag) {

    if(curPage!="" && curPage!='undefined'){
        curPage = curPage;
    }else{
        curPage = 1;
    }
    var operationType  = $("#operationType option:selected").val();
    var createStartDate = $("#startDate").val();
    var createEndDate = $("#endDate").val();
    var updateStartDate = $("#startDates").val();
    var updateEndDate = $("#endDates").val();

    $.ajax({
        url: context_path + "/systemLogs/querySystemLogs",
        type: "post",
        data: {
            "operationType": operationType, "createStartDate": createStartDate,
            "createEndDate": createEndDate, "updateStartDate": updateStartDate,
            "updateEndDate": updateEndDate, "curPage": curPage,
            "queryFlag": queryFlag
        },
        datatype: "html",
        success: function(result) {
            $("#systemLogs_list").empty();
            $("#systemLogs_list").html(result);
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            alert(textStatus + " " + XMLHttpRequest.readyState + " "
                + XMLHttpRequest.status + " " + errorThrown);
        }
    })
}

function logDownload(obj) {
    //日志名称
    var logName = encodeURI(encodeURI($(obj).attr("logName")));
    //日志类型
    var logType = $(obj).attr("logType");
    //日志log文件路径 
    var logFileDir = encodeURI(encodeURI($(obj).attr("logFileDir")));  
    window.location.href = context_path + "/systemLogs/logDownload?logName=" + logName+"&logType="+logType+"&logFileDir="+logFileDir;
}


function  findPage(curPage) {
	findSystemLogs(curPage,"");
}




