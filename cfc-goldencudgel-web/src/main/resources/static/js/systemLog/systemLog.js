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
    findLog(1);
});


function  findLog(curPage) {

    if(curPage!="" && curPage!='undefined'){
        curPage = curPage;
    }else{
        curPage = 1;
    }
    var operationType  = $("#operationType option:selected").val();
    var operationUser = $("#operationUser").val();
    var startDate = $("#startDate").val();
    var endDate = $("#endDate").val();

    $.ajax({
        url: context_path + "/systemLog/querySystemLog",
        type: "post",
        data: {
            "operationType": operationType, "operationUser": operationUser,
            "startDate": startDate, "endDate": endDate, "curPage": curPage
        },
        datatype: "html",
        success: function(result) {
            $("#dataListDiv").empty();
            $("#dataListDiv").html(result);
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            alert(textStatus + " " + XMLHttpRequest.readyState + " "
                + XMLHttpRequest.status + " " + errorThrown);
        }
    })
}

function  findPage(curPage) {
    findLog(curPage);
}