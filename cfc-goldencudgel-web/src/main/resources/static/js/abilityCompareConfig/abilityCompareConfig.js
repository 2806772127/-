$(function(){
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
/*
      $("#TYPE").val($("#types").val());

    $("#ABILITY_TYPE").val($("#abilityTypes").val());
    $("#startDate").val($("#sDate").val());
    $("#startDates").val($("#sDates").val());
    $("#endDate").val($("#EDate").val());
    $("#endDates").val($("#EDates").val());*/

    var  types = sessionStorage.type;

    if (types==undefined||types==null){
        types="";
    }
    $("#TYPE").val(types);
    var  abilityType = sessionStorage.abilityType;

    if (abilityType==undefined||abilityType==null){
        abilityType="";
    }
    $("#ABILITY_TYPE").val(abilityType);
    var  startDate = sessionStorage.startDate;

    if (startDate==undefined||startDate==null){
        startDate="";
    }
    $("#startDate").val(startDate);
    var  startDates = sessionStorage.startDates;

    if (startDates==undefined||startDates==null){
        startDates="";
    }
    $("#startDates").val(startDates);

    var  endDate = sessionStorage.endDate;

    if (endDate==undefined||endDate==null){
        endDate="";
    }
    $("#endDate").val(endDate);


    var  endDates = sessionStorage.endDates;

    if (endDates==undefined||endDates==null){
        endDates="";
    }
    $("#endDates").val(endDates);
    var curPage=sessionStorage.curPage;
    if (curPage==undefined||curPage==null){
        curPage="1";
    }


    sessionStorage.clear();

    queryAbilityCompagerconfig(curPage);
});
//显示图片
var len = $("#imgCon").length;
var arrPic = new Array(); //定义一个数组
for (var i = 0; i < len; i++) {
    arrPic[i] = $("img[modal='zoomImg']").eq(i).prop("src"); //将所有img路径存储到数组中
}
var page=$("#curPage").val();

function  queryAbilityCompagerconfig(curPage) {

    if (page=="" && page=='undefined') {
        if(curPage!="" && curPage!='undefined'){
            curPage = curPage;
        }else{
            curPage = 1;
        }
    }else  {
        if(curPage!="" && curPage!='undefined'){
            curPage = curPage;
        }else{
            curPage = page;
            page="";
        }
    }

    var TYPE = $("#TYPE").val();
    var ABILITY_TYPE = $("#ABILITY_TYPE").val();
    var startDate = $("#startDate").val();
    var endDate = $("#endDate").val();
    var startDates = $("#startDates").val();
    var endDates = $("#endDates").val();

    bodymask("查詢中，請稍等...");
    $.ajax({
        url: context_path + "/abilityCompareConfig/queryabilityCompareConfig",
        type: "post",
        data: {
            "type": TYPE, "abilityType": ABILITY_TYPE,
            "startDate": startDate, "endDate": endDate, "curPage": curPage,
            "startDates": startDates, "endDates": endDates
        },
        datatype: "html",
        success: function(result) {
            bodyunmask();
            $("#abilibty_list").empty();
            $("#abilibty_list").html(result);
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            bodyunmask();
            alert(textStatus + " " + XMLHttpRequest.readyState + " "
                + XMLHttpRequest.status + " " + errorThrown);
        }
    })
}

function  viewIncomDetail(obj) {


    var configid = $(obj).attr("compilationNo");
   var curPage=$(obj).attr("curPage");
    var type = $("#TYPE").val();
    var abilityType = $("#ABILITY_TYPE").val();
    var startDate = $("#startDate").val();
    var endDate = $("#endDate").val();
    var startDates = $("#startDates").val();
    var endDates = $("#endDates").val();

    sessionStorage.clear();


//存入
    sessionStorage.type = type;
    sessionStorage.curPage = curPage;
    sessionStorage.abilityType = abilityType;
    sessionStorage.startDate = startDate;
    sessionStorage.endDate = endDate;
    sessionStorage.startDates = startDates;
    sessionStorage.endDates = endDates;


    /*//读取
        str = sessionStorage.obj;
    //重新转换为对象
        obj = JSON.parse(str);*/
    window.location.href = context_path + "/abilityCompareConfig/seeAbility?configid="+configid+"&curPage="+curPage+"&startDate="+startDate+"&startDates="+startDates+"&endDate="+endDate+"&endDates="+endDates+"&type="+type+"&abilityType="+abilityType


}


function editIncom(obj) {

    var configid = $(obj).attr("compilationNo");
    var curPage=$(obj).attr("curPage");
    var type = $("#TYPE").val();
    var abilityType = $("#ABILITY_TYPE").val();
    var startDate = $("#startDate").val();
    var endDate = $("#endDate").val();
    var startDates = $("#startDates").val();
    var endDates = $("#endDates").val();
    sessionStorage.clear();
    sessionStorage.type = type;
    sessionStorage.curPage = curPage;
    sessionStorage.abilityType = abilityType;
    sessionStorage.startDate = startDate;
    sessionStorage.endDate = endDate;
    sessionStorage.startDates = startDates;
    sessionStorage.endDates = endDates;
    window.location.href = context_path + "/abilityCompareConfig/editAbility?configid="+configid+"&curPage="+curPage+"&startDate="+startDate+"&startDates="+startDates+"&endDate="+endDate+"&endDates="+endDates+"&type="+type+"&abilityType="+abilityType

}


function  changClass(flag) {

    if(flag =='1'){
        $("#new").attr("class","gpic");
        $("#old").attr("class","pic");
    }else{
        $("#old").attr("class","gpic");
        $("#new").attr("class","pic");
    }

}



function  findPage(curPage) {
    queryAbilityCompagerconfig(curPage);
}

function retrunPage(){
/*    var curPage=$("#curPage").val();
    var type=$("#typess").val();
    var abilityType=$("#abilityTypes").val();
    var startDate=$("#sDate").val();
    var startDates=$("#sDates").val();
    var endDate=$("#EDate").val();
    var endDates=$("#EDates").val();*/
    window.location.href = context_path + "/abilityCompareConfig/viewAbilityCompareConfig";
   // window.location.href = context_path + "/abilityCompareConfig/viewAbilityCompareConfig?&curPage="+curPage+"&startDate="+startDate+"&startDates="+startDates+"&endDate="+endDate+"&endDates="+endDates+"&type="+type+"&abilityType="+abilityType
}

function  saveCookic() {


}

/**
 * Created by TF on 2018/1/15.
 */

