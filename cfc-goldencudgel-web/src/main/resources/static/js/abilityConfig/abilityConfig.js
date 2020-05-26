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
    //ss
    
    var  type = sessionStorage.type;
	if (type==undefined||type==null){
		type="";
	}
	$("#type").val(type);
	
	var  financial = sessionStorage.financial;
	if (financial==undefined||financial==null){
		financial="";
	}
	$("#financial").val(financial);
	
	var  debit = sessionStorage.debit;
	if (debit==undefined||debit==null){
		debit="";
	}
	$("#debit").val(debit);
	
	var  business = sessionStorage.business;
	if (business==undefined||business==null){
		business="";
	}
	$("#business").val(business);
	
	var  conpanyYear = sessionStorage.conpanyYear;
	if (conpanyYear==undefined||conpanyYear==null){
		conpanyYear="";
	}
	$("#conpanyYear").val(conpanyYear);
	
	
	var  startDate = sessionStorage.startDate;
	if (startDate==undefined||startDate==null){
		startDate="";
	}
	$("#startDate").val(startDate);
	
	
	var  endDate = sessionStorage.endDate;
	if (endDate==undefined||endDate==null){
		endDate="";
	}
	$("#endDate").val(endDate);
	
	
	var  startDates = sessionStorage.startDates;
	if (startDates==undefined||startDates==null){
		startDates="";
	}
	$("#startDates").val(startDates);
	
	
	var  endDates = sessionStorage.endDates;
	if (endDates==undefined||endDates==null){
		endDates="";
	}
	$("#endDates").val(endDates);
	
	
	
	var curPage=sessionStorage.page_select;
	if (curPage==undefined||curPage==null){
	    curPage="1";
	}

    sessionStorage.clear();
    //var curPage = $("#page_selectBack").val();
    findByPage(curPage);

});

function  findByPage(curPage){

    if(curPage!="" && curPage!='undefined'){
        curPage = curPage;
    }else{
        curPage = 1;
    }

    var type  = $("#type").val();
    var financial  = $("#financial").val();
    var debit  = $("#debit").val();
    var business  = $("#business").val();
    var conpanyYear  = $("#conpanyYear").val();
    var startDate = $("#startDate").val();
    var endDate = $("#endDate").val();
    var startDates = $("#startDates").val();
    var endDates = $("#endDates").val();

    bodymask("查詢中，請稍等...");
    $.ajax({
        url:context_path +"/abilityConfig/abilityConfigList",
        type:'post',
        data: {"type":type,"financial":financial,"debit":debit,"business":business,"conpanyYear":conpanyYear,"curPage":curPage,"startDate":startDate,"endDate":endDate,"startDates":startDates,"endDates":endDates},
        datatype: 'html',
        success:function (result) {
            bodyunmask();
            $("#abilityConfigList").empty();
            $("#abilityConfigList").html(result);
        },
        error:function (XMLHttpRequest, textStatus, errorThrown) {
            bodyunmask();
            alert(textStatus + " " + XMLHttpRequest.readyState + " "
                + XMLHttpRequest.status + " " + errorThrown);
        }
    })

}

//返回方法
function backFunc() {

    //window.location.href = context_path + "/abilityConfig/viewAbilityConfig?type="+type+"&business="+business+"&debit="+debit+"&financial="+financial+"&conpanyYear="+conpanyYear+"&startDate="+startDate+"&endDate="+endDate+"&startDates="+startDates+"&endDates="+endDates+"&curPage="+curPage;
    window.location.href = context_path + "/abilityConfig/viewAbilityConfig";
	
}




function  findPage(curPage) {
    findByPage(curPage);
}

function  showAbilityConfig(obj){
	
	 //点击查看把查询条件带过去
    var type = $("#type").val();
    var business = $("#business").val();
    var debit = $("#debit").val();
    var financial = $("#financial").val();
    var conpanyYear = $("#conpanyYear").val();
    var startDate = $("#startDate").val();
    var endDate = $("#endDate").val();
    var startDates = $("#startDates").val();
    var endDates = $("#endDates").val();
    var page_select = $("#page_select").val();
    
    
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
    sessionStorage.page_select = page_select;
  
	
	
    var configId = $(obj).attr("configId");
    var operate = $(obj).attr("operate");
    window.location.href = context_path + "/abilityConfig/lookAbilityConfig?configId=" + configId + "&operate="+operate+"&type="+type+"&business="+business+"&debit="
    +debit+"&financial="+financial+"&conpanyYear="+conpanyYear+"&startDate="+startDate+"&endDate="+endDate+"&startDates="+startDates+"&endDates="+endDates+"&page_select="+page_select;
}


function  editAbilityConfig(obj){
	
	 //点击查看把查询条件带过去
   var type = $("#type").val();
   var business = $("#business").val();
   var debit = $("#debit").val();
   var financial = $("#financial").val();
   var conpanyYear = $("#conpanyYear").val();
   var startDate = $("#startDate").val();
   var endDate = $("#endDate").val();
   var startDates = $("#startDates").val();
   var endDates = $("#endDates").val();
   var page_select = $("#page_select").val();
   
   
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
   sessionStorage.page_select = page_select;
 
	
	
   var configId = $(obj).attr("configId");
   var operate = $(obj).attr("operate");
   window.location.href = context_path + "/abilityConfig/lookAbilityConfig?configId=" + configId + "&operate="+operate+"&type="+type+"&business="+business+"&debit="
   +debit+"&financial="+financial+"&conpanyYear="+conpanyYear+"&startDate="+startDate+"&endDate="+endDate+"&startDates="+startDates+"&endDates="+endDates+"&page_select="+page_select;
}



