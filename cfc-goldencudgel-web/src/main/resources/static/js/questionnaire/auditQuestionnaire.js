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
	var backFlag = $("#backFlagView").val();
	if(backFlag=="undefined"||backFlag==""){
	 sessionStorage.clear();
	}

	//返回带条件
	var  qName = sessionStorage.qName;
	if (qName==undefined||qName==null){
		qName="";
	}
	$("#qName").val(qName);

	var  createUserId = sessionStorage.commitUserName;
	if (createUserId==undefined||createUserId==null){
		createUserId="";
	}
	$("#createUserId").val(createUserId);

	var  startDateId = sessionStorage.startDate;
	if (startDateId==undefined||startDateId==null){
		startDateId="";
	}
	$("#startDateId").val(startDateId);

	var  endDateId = sessionStorage.endDate;
	if (endDateId==undefined||endDateId==null){
		endDateId="";
	}
	$("#endDateId").val(endDateId);

	var  checkState = sessionStorage.checkState;
	if (checkState==undefined||checkState==null){
		checkState="";
	}
	$("#toBeAudited").attr("class","01"==checkState ? "pic":"gpic")
	$("#beAudited").attr("class","01"==checkState ? "gpic":"pic")
	queryQuestionaires(1);
});


/***查询***/
function queryQuestionaires(curPage){
	if(curPage!="" && curPage!='undefined'){
		curPage = curPage;
	}else{
		curPage = 1;
	}
	var questionName = $("#qName").val();
	var startDate =$("input[name='startDate']").val();
	var endDate =$("input[name='endDate']").val();
    var introduceType = $("#introduceType").val();
    var checkState;
    $('input[name="questionnaireConfig"]').each(function() {
    	if($(this).hasClass('gpic')){
    		var ss = $(this).val();
    		if (ss == '已審核') {
    			checkState = '01'
    		}
    		if (ss == '待審核') {
    			checkState = '02'
    		}	
    	}
	});
    var createUser = $("#createUserId").val();
    bodymask();
	$.ajax({
	    url:context_path + "/questionnaire/querAuditQuestionaires",
	    type:"post",
	    data:{"questionName":questionName,"startDate":startDate,"endDate":endDate,"curPage":curPage,"introduceType":introduceType,"checkState":checkState,"createUser":createUser},
	    datatype: "html",
	    success: function(result) {
	    	bodyunmask();
			$("#questionaires_list").empty();
			$("#questionaires_list").html(result);
			
			$('input[name="questionnaireConfig"]').each(function() {
				if($(this).hasClass('gpic')){
				if("已審核"==$(this).val()){
					$(".comDate").hide();
					$(".comDateTd").hide();
					$(".coSpanClass").html("審核時間：");
					$(".statusClass").hide();
				}
				if("待審核"==$(this).val()){
					$(".comDateAudit").hide();
					$(".comDateAuditTd").hide();
					$(".coSpanClass").html("提交時間：");
					$(".resultClass").hide();
				}
				}
			});
	    },
	    error: function(XMLHttpRequest, textStatus, errorThrown) {
	    	bodyunmask();
	      	alert(textStatus + " " + XMLHttpRequest.readyState + " "
	              + XMLHttpRequest.status + " " + errorThrown);
	    }
	  });
}
function findPage(curPage){
	queryQuestionaires(curPage);
}

/**
 * 测试馆進入編輯頁面
 * @param obj
 * @returns
 */
function viewEditQueston(obj){
	var functionCode = $("#functionCode").val();
	var questionanaireId = $(obj).attr("questionId");
	if(questionanaireId==""||questionanaireId=="undefined"){
		alertInfo("提示","問卷ID為空!");
		return;
	}
	var introduceType = $(obj).attr("questionType");//問卷所屬 01：測字館 02：徵信實訪
	var forAuditFlag = $(obj).attr("forAuditFlag");
	var viewForAudit = $(obj).attr("viewForAudit");//审核查看，隐藏提交按钮
	var createUserCode = $(obj).attr("createUserCode");//提交人
	var addDelUpdateFlag = $(obj).attr("addDelUpdateFlag");//新增删除修改标识
	var auditStatus = $(obj).attr("auditStatus");//维护人进入时带入审核状态，和问卷类型一起判断是否修改再新增一份问卷

	//点击查看把查询条件带过去
	var qName = $("#qName").val();
	var commitUserName = $("#createUserId").val();
	var startDate = $("#startDateId").val();
	var endDate = $("#endDateId").val();
	var checkState;
	$('input[name="questionnaireConfig"]').each(function() {
		if($(this).hasClass('gpic')){
			var ss = $(this).val();
			if (ss == '已審核') {
				checkState = '01'
			}
			if (ss == '待審核') {
				checkState = '02'
			}
		}
	});
	sessionStorage.clear();
	sessionStorage.qName = qName;
	sessionStorage.commitUserName = commitUserName;
	sessionStorage.startDate = startDate;
	sessionStorage.endDate = endDate;
	sessionStorage.checkState = checkState;

	window.location.href = context_path + "/questionnaire/viewEditQuestionnaire?introduceType="+introduceType+"&questionnaireId="+questionanaireId+"&forAuditFlag="+forAuditFlag
		+"&viewForAudit="+viewForAudit+"&createUserCode="+createUserCode+"&auditStatus="+auditStatus+"&addDelUpdateFlag="+addDelUpdateFlag
		+"&qName="+qName+"&commitUserName="+commitUserName+"&startDate="+startDate+"&endDate="+endDate+"&checkState="+checkState+"&functionCode="+functionCode;


}

//修改前问卷
//查詢數據
function queryBeforeQuestione() {
    var beforeQuestionId = $("#beforeQuestionId").val();
    var introduceType = $("#introduceTypeBefore").val();
    $.ajax({
        url : context_path + "/questionnaire/queryBeforeQuestione",
        data : {
            "beforeQuestionId" : beforeQuestionId,
            "introduceType" : introduceType
        },
        type : "POST",
        dataType : "html",
        success : function (result) {
                $("#questionBefore").append(result);
        }
    });
}

/**
 * 审核
 * @returns
 */
function updateQuestionAudit(){
	
	var forAuditFlag = $("#forAuditFlag").val();//判断是否有人工审核栏位页面
	var checkResult;
	$("#questionNames").find("input[name='auditStateName']").each(function(){
		var $that = $(this);
		if($that.is(":checked")){
		   checkResult = $that.val();
		}
	});	
	
	if(checkResult==undefined&&forAuditFlag=="forAuditFlag"){
		alertInfo("提示","請選擇審核結果");
		return; 
	}
	var auditRemark = $("#auditRemarkId").val();
	var questionnaireId = $("#questionnaireId").val();
	var updateRemark = $("#updateRemarkId").text();//修改说明
	var addDelUpdateFlag = $("#addDelUpdateFlag").val();
	
    confirmInfo_1("確認", "是否提交審核？", function() {
        data ={"checkResult":checkResult,"auditRemark":auditRemark,"questionnaireId":questionnaireId,"updateRemark":updateRemark,"addDelUpdateFlag":addDelUpdateFlag};
        bodymask();
        $.ajax({
		    url:context_path + "/questionnaire/updateQuestionnaireAudit",
		    type:"post",
		    data:data,
		    datatype: "json",
		    success: function(result) {
		    	bodyunmask();
		    	alertInfo("提示",result.message);
		    	setTimeout(function(){window.location.href = context_path + "/questionnaire/viewAuditQuestionaires";},2000)
		    	if(result.message=="更新成功"){
					saveFlag = true;
                }
		    },
		    error: function(XMLHttpRequest, textStatus, errorThrown) {
                bodyunmask();
		    	alert(textStatus + " " + XMLHttpRequest.readyState + " "
		              + XMLHttpRequest.status + " " + errorThrown);
		    }
		  });
    });
}

/**
 * 返回首页
 * @returns
 */
function gotoPage(){
	if(saveFlag){
        var introduceType = $("#introduceType").val();
        var backFlag = $("#backFlag").val();
        if(introduceType=='02'){
            window.location.href = context_path + "/questionnaire/viewIntroduces";
        }else{
            window.location.href = context_path + "/questionnaire/viewQuestionnaires?backFlag="+backFlag;
        }
	}else{
        confirmInfo_1("確認", "問卷未儲存，是否返回查詢頁", function() {
            var introduceType = $("#introduceType").val();
            if(introduceType=='02'){
                window.location.href = context_path + "/questionnaire/viewIntroduces";
            }else{
                window.location.href = context_path + "/questionnaire/viewQuestionnaires";
            }
        });
	}
}

function gotoPageForAudit(){
	if(saveFlag){
        var backFlag = $("#backFlag").val();
            window.location.href = context_path + "/questionnaire/viewQuestionnaires?backFlag="+backFlag;
	}else{
        confirmInfo_1("確認", "問卷未審核，是否返回查詢頁", function() {
            window.location.href = context_path + "/questionnaire/viewQuestionnaires";
        });
	}
}

/**
 * 显示或隐藏
 */
function showOrHide(em) {
	var em = $(em);
	if (em.hasClass('gpic')) {
		$('input[name="questionnaireConfig"]').each(function() {
			$(this).removeClass('gpic').addClass('pic');
		});
		em.addClass('gpic');
	} else {
		$('input[name="questionnaireConfig"]').each(function() {
			$(this).removeClass('gpic').addClass('pic');
		});
		em.addClass('gpic');
	}
	$("input[name='startDate']").val("");
	$("input[name='endDate']").val("");
	this.queryQuestionaires(1);
}

