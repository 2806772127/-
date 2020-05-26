$(function () {
	var ratings=  $("#ratings").val();
	$("#ABILITY_DESCRIBE").val(ratings);
});

function updataById(){
    var configId  = $("#configId").val();
    var abiDescribe  = $("#ABILITY_DESCRIBE").val();
    
    var type = $("#typeSeePage").val();
    var business = $("#businessSeePage").val();
    var debit = $("#debitSeePage").val();
    var financial = $("#financialSeePage").val();
    var conpanyYear = $("#conpanyYearSeePage").val();
    var startDate = $("#startDateSeePage").val();
    var endDate = $("#endDateSeePage").val();
    var startDates = $("#endDatesSeePage").val();
    var endDates = $("#endDatesSeePage").val();
    var curPage = $("#page_selectSeePage").val();
    
    
    sessionStorage.clear();
    sessionStorage.type = type;
    sessionStorage.financial = financial;
    sessionStorage.debit = debit;
    sessionStorage.business = business;
    sessionStorage.conpanyYear = conpanyYear;
    sessionStorage.startDate = startDate;
    sessionStorage.endDate = endDate;
    sessionStorage.startDates = startDates;
    sessionStorage.endDates = endDates;
    sessionStorage.page_select = curPage;

    bodymask("儲存中，請稍等...");
    $.ajax({
        url:context_path +"/abilityConfig/updataAbilityConfig",
        type:'post',
        data: {"configId":configId,"abiDescribe":abiDescribe},
        success:function (data) {
            bodyunmask();
            if (data) {
                alertInfo("提示","儲存成功",function(){
                    window.location.href = context_path + "/abilityConfig/viewAbilityConfig";
           //window.location.href = context_path + "/abilityConfig/viewAbilityConfig?type="+type+"&business="+business+"&debit="+debit+"&financial="+financial+"&conpanyYear="+conpanyYear+"&startDate="+startDate+"&endDate="+endDate+"&startDates="+startDates+"&endDates="+endDates+"&curPage="+curPage;
                 });
            }else{
                alertInfo("提示","儲存失败",function(){
                    window.location.href = context_path + "/abilityConfig/lookAbilityConfig?configId=" + configId + "&operate=2";
                });
            }
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            bodyunmask();
            alert(textStatus + " " + XMLHttpRequest.readyState + " "
                + XMLHttpRequest.status + " " + errorThrown);
        }
    });
}

//返回方法
function backFunc() {
    //window.location.href = context_path + "/abilityConfig/viewAbilityConfig?type="+type+"&business="+business+"&debit="+debit+"&financial="+financial+"&conpanyYear="+conpanyYear+"&startDate="+startDate+"&endDate="+endDate+"&startDates="+startDates+"&endDates="+endDates+"&curPage="+curPage;
    window.location.href = context_path + "/abilityConfig/viewAbilityConfig";
	
}

