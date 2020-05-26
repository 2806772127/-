$(function(){
    var start=$("#start").val();
    var end=$("#end").val();
    $("#starts").val(start);
    $("#ends").val(end);

    var ratings=  $("#ratings").val();
    $("#ABILITY_DESCRIBE").val(ratings);

});
function saves() {
  var startRange=  $("#START_RANGE").val();
    var endRange =$("#END_RANGE").val();
    var ability=  $("#ABILITY_DESCRIBE").val();
    var starts=  $("#starts").val();
    var ends=  $("#ends").val();

    //var re = /^\d+(?=\.{0,1}\d+$|$)/;
      var re = /^(\-|\+)?\d+(\.\d+)?$/;
    if (startRange != "") { 
        if (!re.test(startRange)) { 
         alertInfo("提示","請輸入正確的開始範圍格式!");  
         return false;

        } 
    }
    
    if (endRange != "") { 
        if (!re.test(endRange)) { 
         alertInfo("提示","請輸入正確的結束範圍格式!");  
         return false;

        } 
    }

    if(parseFloat(startRange)>parseFloat(endRange)&&startRange!=""&&endRange!=""){
        alertInfo("提示","開始範圍需小於結束範圍");
        return false;
    }
    if(startRange==""&&endRange==""){
        alertInfo("提示","開始與結束範圍不能同時為空");
        return false;
    }
/*    if (starts!=1||starts!=2||ends!=1||ends!=2){
        alertInfo("提示","輸入格式錯誤");
    }*/
    var id=  $("#id").val();
    var abilityType=  $("#abilityType").val();
    var types=  $("#types").val();
    var creatTime=$("#creatTime").val();
    if(types=="經營能力"){
        types="01"
    }
    if(types=="償債能力"){
        types="02"
    }
    if(types=="財務能力"){
        types="03"
    }
    if (abilityType=="優於同業"){
        abilityType="01"
    }
    if (abilityType=="與同業相當"){
        abilityType="02"
    }
    if (abilityType=="劣於同業"){
        abilityType="03"
    }
    bodymask("儲存中，請稍等...");
    $.ajax({
        url:"/abilityCompareConfig/saveAbility ",
        datatype:"json",
        type:"post",
        data:{
            "startRange":startRange,
            "endRange":endRange,
            "ability":ability,
            "starts":starts,
            "ends":ends,
            "id":id,
            "abilityType":abilityType,
            "type":types,
            "creatTime":creatTime
        },
        success:function (data) {
            bodyunmask();
            // alertInfo("提示",data);
            // jQuery(document).ready(function(){
            //     setTimeout(delayer, 3000);
            //     //这里实现延迟5秒跳转
            // });
            alertInfo("提示",data,function () {
                retrunPage();
            });
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            bodyunmask();
            alert(textStatus + " " + XMLHttpRequest.readyState + " "
                + XMLHttpRequest.status + " " + errorThrown);
        }
    });
}
function retrunPage(){
    var curPage=$("#curPage").val();
    var type=$("#typess").val();
    var abilityType=$("#abilityTypes").val();
    var startDate=$("#sDate").val();
    var startDates=$("#sDates").val();
    var endDate=$("#EDate").val();
    var endDates=$("#EDates").val();
/*
    $.ajax({
        url:"/abilityCompareConfig/viewAbilityCompareConfig",
        type:"post",
        datetype:"JSON",
        data:{
            "curPage":curPage,
            "startDate":startDate,
            "startDates":startDates,
            "endDate":endDate,
            "endDates":endDates,
            "type":type,
            "abilityType":abilityType
        },
        success: function(result) {

          ///  window.location.href = context_path + "/abilityCompareConfig/viewAbilityCompareConfig";
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            alert(textStatus + " " + XMLHttpRequest.readyState + " "
                + XMLHttpRequest.status + " " + errorThrown);
        }
    });
*/

    window.location.href = context_path + "/abilityCompareConfig/viewAbilityCompareConfig";





  //window.location.href = context_path + "/abilityCompareConfig/viewAbilityCompareConfig?&curPage="+curPage+"&startDate="+startDate+"&startDates="+startDates+"&endDate="+endDate+"&endDates="+endDates+"&type="+type+"&abilityType="+abilityType
}
