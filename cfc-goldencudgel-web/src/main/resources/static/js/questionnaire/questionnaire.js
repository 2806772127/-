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
		if("01"==checkState){
			checkState="已審核";	
		}
		if("02"==checkState){
			checkState="待審核";	
		}
		if(checkState=="已審核"){
		 $("#toBeAudited").removeClass('gpic').addClass('pic');
		 $("#beAudited").removeClass('pic').addClass('gpic');
		}
		if(checkState=="待審核"){
		 $("#toBeAudited").removeClass('pic').addClass('gpic');
		}
	   
    saveFlag = false;
	var flag = $("#flag").val();
	if(flag == "view"){
	  queryQuestionaires(1);
	}else if(flag == "add"){
	  var _clone = $("#template_02").clone();
	  _clone.css('display','block');
	  _clone.attr("id","question_1");
	  $(".question").append(_clone);
	}else if(flag =="edit"){
	  //設置編輯頁面信息
	  // setEditDefault();
	  saveFlag = true;
	  var prodType = $("#prod").val();
	  $("#question .question_div").each(function(){
		var _value = $(this).find(".questionTdClass select[name='level']").val();
		//审核页面使用
		setSelectLevelChange($(this),_value);
	  });
	  showOrHideDefualtInput(prodType);
	}
	setSaveFlag();
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
    var description = $("#descriptionId").val();
    bodymask();
	$.ajax({
	    url:context_path + "/questionnaire/queryQuestionaires",
	    type:"post",
	    data:{"questionName":questionName,"startDate":startDate,"endDate":endDate,"curPage":curPage,"introduceType":introduceType,"checkState":checkState,"createUser":createUser,"description":description},
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
					$(".coSpanClass").html("審核時間");
					$(".statusClass").hide();
				}
				if("待審核"==$(this).val()){
					$(".comDateAudit").hide();
					$(".comDateAuditTd").hide();
					$(".coSpanClass").html("提交時間");
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
function viewAddQuestionnaire(){
    var introduceType = $("#introduceType").val();
    var functionCode = $("#functionCode").val();
    window.location.href = context_path + "/questionnaire/viewAddQuestionnaire?introduceType="+introduceType+"&functionCode="+functionCode;
}

/**多选题添加行**/
function addRows(obj){
	var p_table = $(obj).parents(".tb01");//获取父元素table
	var p_tr = $(obj).parents("._add_clone");//获取父元素tr
//	var _clone = $("#row_template").clone();//复制模板行
    var _first_td = p_table.find('._add_clone').eq(0).find("td").eq(0);//获取首行td
	var _clone = p_tr.clone();//复制模板行
	_clone.find(":input").val("");//清空数据
	var _rows = Number(_first_td.attr("rowspan"));//获取首行占用行数
	var _row_n = _rows+1;
	var _attr = _clone.children('td').eq(0).attr("rowspan");
	if(_attr){//复制首行的话需要删除 第一列
		_clone.children('td').eq(0).remove();
	}
	_first_td.attr("rowspan",_row_n);//修改占用首行属性
	_clone.insertAfter(p_table.find("._add_clone").last());//插入行
	// setToQuestion(obj);
}

/***多选题删除行**/
function delRows(obj){
    var p_table = $(obj).parents(".tb01");//获取父元素table
    var p_tr = $(obj).parents("._add_clone");//获取父元素tr
    var p_first_tr = p_table.find("._add_clone").eq(0);//获取首行tr
    var trSize = p_table.find("._add_clone").size();
    if(trSize < 3){
        alertInfo("提示","選擇題至少保留兩行");
        return;
    }
    var tdSize = p_tr.find("td").size();
    if(tdSize == 4){
        var _next_tr = p_tr.next("tr");
        var _next_tr_ftd = _next_tr.find("td").eq(0);
        var _clone_td = p_tr.find("td").eq(0).clone();
        var _rows = Number($(_clone_td).attr("rowspan"));
        _clone_td.attr("rowspan",_rows-1);
        _clone_td.insertBefore(_next_tr_ftd);
    }else{
        var first_td = p_first_tr.find('td').eq(0);
        var _rows = first_td.attr("rowspan");//获取首行td rowspan
        first_td.attr("rowspan",_rows-1);
    }
    p_tr.remove();
}

/***题目类型不同切换*/
function selectChange(obj){
	var value = $(obj).val();
	var _pdiv = $(obj).parents(".question_div");
	var optionVal = _pdiv.find(":input[name='nextQuestion']").html();
	var questionBodyEm = $(_pdiv).find(".question_body");
	var questionName = questionBodyEm.find("textarea[name='questionName']").val();
	var _clone;
	if(value=="02" || value=="04"){
		_clone = $("#template_01 .question_body").clone();
	}else{
		_clone =$("#template_02 .question_body").clone();
		var answerEm = questionBodyEm.find("input[name='answer']");
		if(answerEm.length == 1){
			_clone.find("input[name='answer']").val(answerEm.val());
		}
	}
	questionBodyEm.remove();
    _clone.find("textarea[name='questionName']").val(questionName);
    $(_pdiv).append(_clone);
	_pdiv.find(":input[name='nextQuestion']").html(optionVal);
}

/***問題級別的選擇的處理*/
function selectLevel(obj){
	var value = $(obj).val();
	var _pdiv = $(obj).parent().parent().parent().parent().parent();
	setSelectLevelChange(_pdiv,value);
	if (value == '1') {
		$(_pdiv).find(".selectLevel").each(function () {
			this.style.display = "none";
		});
	}else{
		$(_pdiv).find(".selectLevel").each(function () {
			this.style.display = "inline";
		});
		$(obj).val(value);
		// setFatherQuestion();
	}
    changeSelectCommon(obj);
}

/**默認答案與子問題位置互換**/
function setSelectLevelChange(obj,value){
	var curTd = $(obj).find(".questionTdClass");//當前下拉框所在td
	var curTr = $(curTd).parent("tr");//當前下拉所在tr
	var fatherQHead = $(curTr).find("#fatherQHd");
	var fatherQBody = $(curTr).find("#fatherQBd");
	var defAnsHd = $(curTr).find("#defAnsHd");
	var defAnsBd = $(curTr).find("#defAnsBd");
	if (value == '1') {
		$(defAnsBd).insertAfter($(curTd));
		$(defAnsHd).insertAfter($(curTd));
		
	}else{
		$(fatherQBody).insertAfter($(curTd));
		$(fatherQHead).insertAfter($(curTd));
		
	}
}

/**审核问卷页面  默認答案與子問題位置互換**/
function setSelectLevelChangeAudit(obj,value){
	var curTd = $(obj).find(".questionTdClass");//當前下拉框所在td
	var curTr = $(curTd).parent("tr");//當前下拉所在tr
	var fatherQHead = $(curTr).find("#fatherQHd");
	var fatherQBody = $(curTr).find("#fatherQBd");
	var defAnsHd = $(curTr).find("#defAnsHd");
	var defAnsBd = $(curTr).find("#defAnsBd");
	if (value == '1') {
		$(defAnsBd).insertAfter($(curTd));
		$(defAnsHd).insertAfter($(curTd));
		
	}else{
		$(fatherQBody).insertAfter($(curTd));
		$(fatherQHead).insertAfter($(curTd));
		
	}
}

/**添加题目**/
function addTemplate(obj){
	var curDiv = $(obj).parents(".question_div");
	var id= $(curDiv).attr("id");
	var cur_index = id.substring(id.indexOf("_")+1,id.length);
	var _clone = $(curDiv).clone();
    _clone.find(":input").val("");
	var selectOption = $(curDiv).find("select[name='type']").val();
	var selEm = _clone.find("select[name='type']");
    selEm.val(selectOption);//初始化下拉框选项
	_clone.find("select[name='level']").val("1");
	_clone.find(".selectLevel").css("display","none");
    selectChange(selEm);//初始化选项文字

	//初賽問卷3默認值顯示設置
	setSelectLevelChange(_clone,"1");
	
	_clone.insertAfter($(curDiv));
    setToQuestion(obj,"01");
}

/**設置上級問題的下拉框選項**/
function setFatherQuestion(obj,type){
    var cur_index;
    if(obj != null && obj != undefined){
        var questionEm = $(obj).parents(".question_div");
        var id = questionEm.attr("id");
        cur_index = id.substring(id.indexOf("_")+1,id.length);
    }
	var ques_size = $(".question .question_div").size();
	$(".question .question_div").each(function(index){
		var question = $(this);
		var options = "<option value=''>--請選擇--</option>";
		for(var i=1 ;i<=ques_size;i++){
			options += "<option value='"+ i+"'>第"+i+"题</option>";
		}
		question.find(".selectLevel").each(function(){
			var select = $(this);
			select.find("option").remove();
			select.append(options);

            if (obj != null && obj != undefined) {
                var indexEm = select.next("input");
                var oldIndex = indexEm.val();
                if(type == "01" || type == "02"){//01:添加;02:删除
                    if (oldIndex != null && oldIndex != "") {
                        if (Number(oldIndex) > (Number(cur_index)+1)) {
                            var newIndex = type=="01"?(Number(oldIndex) + 1):(Number(oldIndex) - 1);
                            indexEm.val(newIndex + "");
                            select.val(newIndex + "");
                        }else if(Number(oldIndex) == (Number(cur_index)+1)){
                            indexEm.val("");
                            select.val("");
                        } else {
                            indexEm.val(oldIndex + "");
                            select.val(oldIndex + "");
                        }
                    }
                }else if(type == "03" || type == "04"){//03:上移;04:下移
                    var switchIndex = type=="03"?(Number(cur_index) + 1):(Number(cur_index) - 1);
                    var theIndex = index+1;
                    if(Number(switchIndex) == theIndex || Number(cur_index) == theIndex || Number(oldIndex) == Number(cur_index) || Number(oldIndex) == Number(switchIndex)){
                        indexEm.val("");
                        select.val("");
                    } else {
                        indexEm.val(oldIndex + "");
                        select.val(oldIndex + "");
                    }
                }
            }
		});
	});
}

/**設置跳轉題目下拉框**/
function setToQuestion(obj,type){
    // bodymask("加載中，請稍後...");
    saveFlag = false;
    var cur_index;
	if(obj != null && obj != undefined){
        var questionEm = $(obj).parents(".question_div");
        var id = questionEm.attr("id");
        cur_index = id.substring(id.indexOf("_")+1,id.length);
	}
	cur_index = Number(cur_index);
	var ques_size = $(".question .question_div").size();
	$(".question .question_div").each(function(index){
	    var questionDivEm = $(this);
	    //設置題號
        var _index = index+1;
        var _title = "第" + _index + "題";
        questionDivEm.find(".question_no").text(_title);
        questionDivEm.attr("id","question_"+ _index);

        //跳轉題目
        questionDivEm.find(".nextQuestion").each(function(){
            var selectEm = $(this);
            //跳轉題目-下拉框選項調整
            if(type == "01"){//新增
                if(_index > cur_index && _index < ques_size){
                    selectEm.children("option:nth-child(2)").remove();
                }
                if(_index < ques_size){
                    selectEm.children("option:last-child").before("<option value='"+ques_size+"'>第"+ques_size+"题</option>");
                }
            }else if(type == "02"){//刪除
                if((_index < ques_size) || (_index == (cur_index-1) && (cur_index-1)==ques_size)){
                    selectEm.children("option:nth-last-child(2)").remove();
                }
                if(_index >= cur_index && _index < ques_size){
                    selectEm.children("option:first-child").after("<option value='"+(_index+1)+"'>第"+(_index+1)+"题</option>");
                }
            }else if(type == "03" || type == "04"){//03:上移;04:下移
                var smallIndex = type=="03"?cur_index:(cur_index-1);
                var bigIndex = type=="03"?(cur_index+1):cur_index;
                if(_index == smallIndex){
                    selectEm.children("option:first-child").after("<option value='"+(_index+1)+"'>第"+(_index+1)+"题</option>");
                }else if(_index == bigIndex){
                    selectEm.children("option:nth-child(2)").remove();
                }
            }

            //跳轉題目-選中值更新
            if (obj != null && obj != undefined) {
                var indexEm = selectEm.next("input");
                var oldIndex = indexEm.val();
                if(type == "01" || type == "02"){//01:添加;02:删除
                    if (ques_size == _index) {
                        indexEm.val("end");
                        selectEm.val("end");
                    }else if(oldIndex == "end" || (type == "01" && cur_index == index)){
                        indexEm.val("");
                        selectEm.val("");
                    }else if (oldIndex != null && oldIndex != "") {
                    	oldIndex = Number(oldIndex);
                        if (oldIndex > cur_index+1) {
                            var newIndex = type=="01"?(oldIndex + 1):(oldIndex - 1);
                            indexEm.val(newIndex + "");
                            selectEm.val(newIndex + "");
                        }else if(oldIndex == (cur_index+1)){
                            indexEm.val("");
                            selectEm.val("");
                        } else {
                            indexEm.val(oldIndex + "");
                            selectEm.val(oldIndex + "");
                        }
                    }
                }else if(type == "03" || type == "04"){//03:上移;04:下移
                    var switchIndex = type=="03"?(cur_index + 1):(cur_index - 1);
                    if (ques_size == _index) {
                        indexEm.val("end");
                        selectEm.val("end");
                    }else if(oldIndex == "end" || switchIndex == _index || cur_index == _index || Number(oldIndex) == cur_index || Number(oldIndex) == switchIndex){
                        indexEm.val("");
                        selectEm.val("");
                    } else {
                        indexEm.val(oldIndex + "");
                        selectEm.val(oldIndex + "");
                    }
                }
            }
        });

        //所屬主問題
        questionDivEm.find(".selectLevel").each(function(){
            var selectEm = $(this);
            //所屬主問題-下拉框選項調整
            if(type == "01"){//新增
                if(_index > cur_index){
                    selectEm.children("option:last-child").after("<option value='"+(_index-1)+"'>第"+(_index-1)+"题</option>");
                }
            }else if(type == "02"){//刪除
                if(_index > cur_index){
                    selectEm.children("option:last-child").remove();
                }
            }else if(type == "03" || type == "04"){//03:上移;04:下移
                var smallIndex = type=="03"?cur_index:(cur_index-1);
                var bigIndex = type=="03"?(cur_index+1):cur_index;
                if(_index == smallIndex){
                    selectEm.children("option:last-child").remove();
                }else if(_index == bigIndex){
                    selectEm.children("option:last-child").after("<option value='"+(_index-1)+"'>第"+(_index-1)+"题</option>");
                }
            }

            //所屬主問題-選中值更新
            if (obj != null && obj != undefined) {
                var indexEm = selectEm.next("input");
                var oldIndex = indexEm.val();
                if(type == "01" || type == "02"){//01:添加;02:删除
                    if (oldIndex != null && oldIndex != "") {
                        oldIndex = Number(oldIndex);
                        if (oldIndex > (cur_index+1)) {
                            var newIndex = type=="01"?(oldIndex + 1):(oldIndex - 1);
                            indexEm.val(newIndex + "");
                            selectEm.val(newIndex + "");
                        }else if(oldIndex == (cur_index+1)){
                            indexEm.val("");
                            selectEm.val("");
                        } else {
                            indexEm.val(oldIndex + "");
                            selectEm.val(oldIndex + "");
                        }
                    }
                }else if(type == "03" || type == "04"){//03:上移;04:下移
                    var switchIndex = type=="03"?(cur_index + 1):(cur_index - 1);
                    if(switchIndex == _index || cur_index == _index || Number(oldIndex) == cur_index || Number(oldIndex) == switchIndex){
                        indexEm.val("");
                        selectEm.val("");
                    } else {
                        indexEm.val(oldIndex + "");
                        selectEm.val(oldIndex + "");
                    }
                }
            }
        });
    });
    // setFatherQuestion(obj,type);
}
/***删除题目***/
function delTemplate(obj){
	var questionSize = $(".question .question_div").size();
	if(questionSize <= 1){
		alertInfo("提示","至少要保留一題題目");
		return;
	}
    var curDiv = $(obj).parents(".question_div");//当前操作div
	$(curDiv).remove();//删除
	//题目重新设定序号
	// $("#question ._question").each(function(index){
	// 	var _index = index + 1;
	// 	$(this).attr("id","question_" + _index);
	// 	$(this).find("#title").text("第" + _index + "题");
	// });
	setToQuestion(obj,"02");
}
/**
 * 上移
 * */
function templateUp(obj){
    var curDiv = $(obj).parents(".question_div");//当前div
	var curid= curDiv.attr("id");
	var curTitle = $(curDiv).find(".question_no").text();

    var cur_index = curid.substring(curid.indexOf("_")+1,curid.length);
    if(Number(cur_index)==1){
        alertInfo("提示","第一題無法上移！");
        return;
	}
	
	var preDiv = curDiv.prev();//前一个div
	var preTitle = preDiv.find(".question_no").text();
	var preid = preDiv.attr("id");
	
	//变换题目和id属性
	if(preTitle!="" && preid!=undefined){
		preDiv.attr("id",curid);
		preDiv.find(".question_no").text(curTitle);
		
		curDiv.attr("id",preid);
		curDiv.find(".question_no").text(preTitle);
		preDiv.before(curDiv);
	}
	setToQuestion(obj,"03");
}
/**下移**/
function templateDown(obj){
    var curDiv = $(obj).parents(".question_div");//当前div
	var curid= curDiv.attr("id");
	var curTitle = curDiv.find(".question_no").text();

    var ques_size = $(".question .question_div").size();
    var cur_index = curid.substring(curid.indexOf("_")+1,curid.length);
    if(Number(cur_index)==ques_size){
        alertInfo("提示","最後一題無法下移！");
        return;
    }
	
	var nextDiv = curDiv.next();//下一个div
	var nextTitle = nextDiv.find(".question_no").text();
	var nexteid = nextDiv.attr("id");
	//变换题目和id属性
	if(nextTitle!="" && nexteid!=undefined){
		nextDiv.attr("id",curid);
		nextDiv.find(".question_no").text(curTitle);
		
		curDiv.attr("id",nexteid);
		curDiv.find(".question_no").text(nextTitle);
		nextDiv.after(curDiv);
	}
	setToQuestion(obj,"04");
}

/**添加属性  list数据封装
function addPro(obj,flag){
	if(flag){
		$(obj).attr("listNameHead","details");
	}
	$(obj).find("select[name='type']").attr("listName","type");
	$(obj).find("input[name='isRequired']").attr("listName","isRequired");
	//$(obj).find("._add_clone").attr("listNameHead2","answers");
	$("#question").find("._add_clone").each(function(){
		$(this).attr("listNameHead2","answers");
	});
    $(obj).find("textarea[name='questionName']").attr("listName","name");
    $(obj).find("input[name='answer']").attr("listName2","answer");
    $(obj).find("select[name='nextQuestion']").attr("listName2","nextQuestion");
}**/

/***
 * 儲存
 * @returns
 */
function addQuestion(){
	var questionName = $("#questionnaireName").val();
	if(questionName==""){
		alertInfo("提示","請填寫問卷名稱！");
		return ;
	}
	var description = $("textarea[name='description']").val();
	if(description==""){
		alertInfo("提示","請填寫問卷說明！");
		return ;
	}
	var questionType = $("#questionType").val();
	var prodCode = $("select[name='prodCode']").val();
	if(questionType!="02"){
		if(prodCode==""){
			alertInfo("提示","請選擇問卷類型");
			return;
		}
	}
	var array=new Array();
	$(".question .question_div").each(function(index){
		var question = $(this);
		 question.find("._add_clone").find(".nextQuestion").each(function(){
			 var option= $(this).val();
			 array.push(option); 
		 }); 
	});	
	
	for(var i=0;i<array.length;i++){
		if(array[i] == "" || array[i] == "undefined"){
			alertInfo("提示","跳轉題目未設定");
			return; 
		}
	}
	
	var fatherQuestionArray=new Array();
	$(".question .question_div").each(function(index){
		var question = $(this);
		 question.find(".fatherQuestionClass").find(".selectLevel").each(function(){
			 var option= $(this).val();
			 fatherQuestionArray.push(option); 
		 }); 
	});	
	
	var questionLevelArray=new Array();
	$(".question .question_div").each(function(index){
		var question = $(this);
		 question.find(".questionTdClass").find(".questionLevel").each(function(){
			 var option= $(this).val();
			 questionLevelArray.push(option); 
		 }); 
	});	
	
	for(var i=0;i<fatherQuestionArray.length;i++){
		if(questionLevelArray[i]!="1"){
		if(fatherQuestionArray[i] == "" || fatherQuestionArray[i] == "undefined"){
			alertInfo("提示","所屬主問題未設定");
			return; 
		}
		}
	}
	
	var titleArray=new Array();
	$(".question .question_div").each(function(index){
		var question = $(this);
		 question.find("._add_clone").find(".textarea").each(function(){
			 var option= $(this).val();
			 titleArray.push(option); 
		 }); 
	});	
	
	for(var i=0;i<titleArray.length;i++){
		if(titleArray[i] == "" || titleArray[i] == "undefined"){
			alertInfo("提示","請填寫題目");
			return; 
		}
	}
	  
    var functionCode = $("#functionCode").val();
	var data = $("#questionnarie").serialize();
	$("body textarea").each(function(index, element) {
		str = element.value;
		str= str.replace(/\+/g, '%2B');
		$(element).val(str);
	});
    data += listSerialize("question")+"&"+$.param({"functionCode":functionCode});
	$("body textarea").each(function(index, element) {
		str = element.value;
		str= str.replace(/%2B/g,'+');
		$(element).val(str);
	});
    confirmInfo_1("確認", "是否儲存信息？", function() {
    	bodymask();
	    $.ajax({
		    url:context_path + "/questionnaire/addQuestionnaire",
		    type:"post",
		    data:data,
		    datatype: "json",
		    success: function(result) {
		    	bodyunmask();
				if(result.success){
				    saveFlag = true;
                    // alertInfo("提示",result.message);
                    alertInfo("提示",result.message,function(){
                        gotoPage();
                    });
				}else{
				    alertInfo("提示",result.message);
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

/**類型變化選擇**/
function changeType(obj,value,id){
	$(obj).parent().find("input").each(function(){
		$(this).attr("class","pic");
	});
	$(obj).attr("class","gpic");
	if("_type"==id){
		$("#questionType").val(value);
		if(value=="02"){
			$("#prodLabel").hide();
			$("#prodSelect").hide();
		}else if(value == "01"){
			$("#prodLabel").show();
			$("#prodSelect").show();
			var isNotSelect = true;
			$("#prodCode option").each(function(){
				if (this.value == '01' || this.value == '03'|| this.value == '14') {
					this.style='display:list-item';
					if (isNotSelect) {
						this.selected=true;
						isNotSelect = false;
					}
				}else{
					this.style='display:none';
				}
			});
			$("#prodCode").trigger("chosen:updated");
		}else if(value == "04"){
			$("#prodLabel").show();
			$("#prodSelect").show();
			var isNotSelect = true;
			$("#prodCode option").each(function(){
				if (this.value == '04' || this.value == '05' || this.value == '06'
					|| this.value == '07' || this.value == '08' || this.value == '09'
					|| this.value == '10' || this.value == '11' || this.value == '12'|| this.value == '13') {
					this.style='display:list-item';
					if (isNotSelect) {
						this.selected=true;
						isNotSelect = false;
					}
				}else{
					this.style='display:none';
				}
			});
			$("#prodCode").trigger("chosen:updated");
		}
	}else if("_isEnable"==id){
		$("#isEnable").val(value);
	}else {
		$(obj).parent().find("input[name='isRequired']").val(value);
	}
}
function qSelectChange(obj){
	$("#questionType").val($(obj).find("option:selected").val());
	var questionType = $("#questionType").val();//問卷你類型
	//初賽問卷3 顯示默認值
	showOrHideDefualtInput(questionType);
}
/***顯示隱藏默認值輸入框**/
function showOrHideDefualtInput(questionType){
	if(questionType=="14"){
		  $("#question .defualtAnswer").each(function(){
			 $(this).css('display','block');
		  });
		}else{
			$("#question .defualtAnswer").each(function(){
				$(this).css('display','none');
			  });
		}
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
    //var introduceType = $("#introduceType").val();
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
 * 更新
 * @returns
 */
function updateQuestion(){
	
	$("select").removeAttr("disabled");
	$("input").removeAttr("disabled");
	var questionName = $("#questionnaireName").val();
	var QuestionExplanation=$("#QuestionExplanation").val();
	
	var regu = "^[ ]+$";
	var re = new RegExp(regu);
	if(re.test(QuestionExplanation)){
		alertInfo("提示","問卷說明不能为空！");
		return ;
	}
	if(QuestionExplanation==""){
		alertInfo("提示","問卷說明不能为空！");
		return ;
	}
	if(questionName==""){
		alertInfo("提示","請填寫問卷名稱！");
		return ;
	}
	var description = $("input[name='description']").val();
	if(description==""){
		alertInfo("提示","請填寫問卷說明！");
		return ;
	}
	var questionType = $("#questionType").val();
	var prodCode = $("select[name='prodCode']").val();
	if(questionType!="02"){
		if(prodCode==""){
			alertInfo("提示","請選擇問卷類型");
			return;
		}
	}
	var array=new Array();
	$(".question .question_div").each(function(index){
		var question = $(this);
		 question.find("._add_clone").find(".nextQuestion").each(function(){
			 var option= $(this).val();
			 array.push(option); 
		 }); 
	});
	for(var i=0;i<array.length;i++){
		if(array[i] == "" || array[i] == "undefined"){
			alertInfo("提示","跳轉題目未設定");
			return; 
		}
	}
	
	var fatherQuestionArray=new Array();
	$(".question .question_div").each(function(index){
		var question = $(this);
		 question.find(".fatherQuestionClass").find(".selectLevel").each(function(){
			 var option= $(this).val();
			 fatherQuestionArray.push(option); 
		 }); 
	});	
	
	var questionLevelArray=new Array();
	$(".question .question_div").each(function(index){
		var question = $(this);
		 question.find(".questionTdClass").find(".questionLevel").each(function(){
			 var option= $(this).val();
			 questionLevelArray.push(option); 
		 }); 
	});	
	
	for(var i=0;i<fatherQuestionArray.length;i++){
		if(questionLevelArray[i]!="1"){
		if(fatherQuestionArray[i] == "" || fatherQuestionArray[i] == "undefined"){
			alertInfo("提示","所屬主問題未設定");
			return; 
		}
		}
	}
	
	var titleArray=new Array();
	$(".question .question_div").each(function(index){
		var question = $(this);
		 question.find("._add_clone").find(".textarea").each(function(){
			 var option= $(this).val();
			 titleArray.push(option); 
		 }); 
	});	
	
	for(var i=0;i<titleArray.length;i++){
		if(titleArray[i] == "" || titleArray[i] == "undefined"){
			alertInfo("提示","請填寫題目");
			return; 
		}
	}
	
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
	
	var introduceType = $("#introduceType").val();//根据类型修改后返回的是测字馆还是征信实访页面
	
    confirmInfo_1("確認", "是否提交審核？", function() {
        var data = $("#questionnarie").serialize();
        $("body textarea").each(function(index, element) {
            str = element.value;
            str= str.replace(/\+/g, '%2B');
            $(element).val(str);
        });
        data += listSerialize("question");
        $("body textarea").each(function(index, element) {
            str = element.value;
            str= str.replace(/%2B/g,'+');
            $(element).val(str);
        });
        var functionCode = $("#functionCode").val();
        var updateRemark = $("#updateRemarkId").val();//修改说明
    	
    	var auditRemark = $("#auditRemarkId").val();
    	var createUserCode = $("#createUserCode").val();//提交人
    	var questionProdType = $("#prod").val();//问卷类型
    	var auditStatus = $("#auditStatusId").val();//审核状态
    	var addDelUpdateFlag = $("#addDelUpdateFlag").val();
        data +="&"+$.param({"forAuditFlag":forAuditFlag,"updateRemark":updateRemark,"checkResult":checkResult,"auditRemark":auditRemark,"createUserCode":createUserCode,"questionProdType":questionProdType,"auditStatus":auditStatus,"addDelUpdateFlag":addDelUpdateFlag,"functionCode":functionCode});
        bodymask();
        $.ajax({
		    url:context_path + "/questionnaire/updateQuestionnaire",
		    type:"post",
		    data:data,
		    datatype: "json",
		    success: function(result) {
		    	bodyunmask();
		    	alertInfo("提示",result.message);
		    	//setTimeout(3000);
		    	if(introduceType=='02'){
		    	setTimeout(function(){window.location.href = context_path + "/questionnaire/viewIntroduces";},2000);	
		    	}else{
		    	setTimeout(function(){window.location.href = context_path + "/questionnaire/viewQuestionnaires";},2000);	
		    	}

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
        var data ={"checkResult":checkResult,"auditRemark":auditRemark,"questionnaireId":questionnaireId,"updateRemark":updateRemark,"addDelUpdateFlag":addDelUpdateFlag};
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
 * 测试馆刪除
 * @param obj
 * @returns
 */
function deleteQueston(obj){
	var questionnaireId = $(obj).attr("questionId");
	var functionCode = $("#functionCode").val();
	if(questionnaireId==""||questionnaireId=="undefiend"){
		alertInfo("提示","問卷ID為空");
		return;
	}
	confirmInfo_1("確認", "是否刪除該問卷？", function() {
		bodymask();
	 	$.ajax({
		    url:context_path + "/questionnaire/deleteQuestionnaire",
		    type:"post",
		    data:{"questionnaireId":questionnaireId,"functionCode":functionCode},
		    datatype: "json",
		    success: function(result) {
		    	bodyunmask();
		    	if(result.success){
			    	  alertInfo("提示",result.message,function(){
						  saveFlag = true;
			    		  gotoPage();
			    	  });
			      }else{
			    	  alertInfo("提示",result.message);
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
        window.location.href = context_path + "/questionnaire/viewAuditQuestionaires?backFlag="+backFlag;
	}else{
        confirmInfo_1("確認", "問卷未審核，是否返回查詢頁", function() {
            window.location.href = context_path + "/questionnaire/viewAuditQuestionaires";
        });
	}
}

/***
 * 進入編輯頁面設置
 * @returns
 */
function setEditDefault(){
	var questionType = $("#questionType").val();
	var isEnable = $("#isEnable").val();
	if(questionType=="02"){
		$("#prodLabel").hide();
		$("#prodSelect").hide();
	}else if(questionType == "00" || questionType == "01"  || questionType == "03"){
		$("#prodLabel").show();
		$("#prodSelect").show();
		$("#prodCode option").each(function(){
			if (this.value == '01' || this.value == '03') {
				this.style='display:list-item';
				if (isNotSelect) {
					this.selected=true;
					isNotSelect = false;
				}
			}else{
				this.style='display:none';
			}
		});
		$("#prodCode").trigger("chosen:updated");
	}else if(questionType == "04" || questionType == "05"  || questionType == "06"
			|| questionType == "07" || questionType == "08"  || questionType == "09"
			|| questionType == "10" || questionType == "11"  || questionType == "12"|| questionType == "13"){
		$("#prodLabel").show();
		$("#prodSelect").show();
		var isNotSelect = true;
		$("#prodCode option").each(function(){
			if (this.value == '04' || this.value == '05' || this.value == '06'
				|| this.value == '07' || this.value == '08' || this.value == '09'
				|| this.value == '10' || this.value == '11' || this.value == '12'|| questionType == "13") {
				this.style='display:list-item';
				if (isNotSelect) {
					this.selected=true;
					isNotSelect = false;
				}
			}else{
				this.style='display:none';
			}
		});
		$("#prodCode").trigger("chosen:updated");
	}

	//题目重新设定序号
	$(".question .question_div").each(function(index){
		var _index = index + 1;
		$(this).attr("id","question_" + _index);
		$(this).find("#title").text("第" + _index + "题");
	});
	
	setToQuestion();
	// setFatherQuestion();
	$(".next").each(function(){
		var value = $(this).val();
		var _select = $(this).prev("select");
	    _select.val(value);
	});

	//设置映射栏位
	setEditDefualtMapping($("select[name='ruleName']"),'rule');
	setEditDefualtMapping($("select[name='applyName']"),'apply');
}
function setEditDefualtMapping(obj,type,value){
	var _value = '';
	var value_1 = '';
	var value_2 = '';
	$("input[listName='"+type+"Item']").each(function(){
		_value = $(this).val();
		if(_value){
			value_1 = _value.substring(0,_value.indexOf(","));
			value_2 = _value.substring(_value.indexOf(",")+1,_value.length);
			var _parent = $(this).parent("td");
			$(_parent).find("select[name='"+type+"Name']").val(value_1);
			mappingChange($(_parent).find("select[name='"+type+"Name']"),type);
			$(_parent).find("select[name='"+type+"Item']").val(value_2);
		}
	});
//	if(value){
//		value = value.substring(0,value.indexOf(","));
//		$("select[name='"+type+"Name']").val(value);
//	}
//	mappingChange(obj,type);
}
/**
 * 映射栏位选择变化时设置之后的选项
 * @param obj
 * @param type
 * @returns
 */
function mappingChange(obj,type){
	var itemName = $(obj).find("option:selected").val();
	if(itemName){
		$.ajax({
		    url:context_path + "/questionnaire/getMappingItem",
		    type:"post",
		    data:{"itemName":itemName,"type":type},
		    datatype: "json",
		    async: false,
		    success: function(result) {
		    	if(result.success){
			    	 var selectItem = $(obj).parent("td").find("select[name='"+type+"Item']");
			    	 selectItem.empty();
			    	 var info = "<option value=''>--請選擇--</option>";
			    	 var data = result.data;
			    	 if(data!="" && data!=null){
			    		 for(var i=0;i<data.length;i++){
			    			 info+="<option value='"+data[i]+"'>"+data[i]+"</option>";
			    		 }
			    	 }
			    	 selectItem.append(info);
			      }else{
			    	  alertInfo("提示",result.message);
			      }
		    },
		    error: function(XMLHttpRequest, textStatus, errorThrown) {
		      alert(textStatus + " " + XMLHttpRequest.readyState + " "
		              + XMLHttpRequest.status + " " + errorThrown);
		    }
		  });
	}else{
		 $(obj).next().empty().append("<option value=''>--請選擇--</option>");
	}
}
function setMappingItem(obj,inputId){
	var value = $(obj).find("option:selected").val();
	var prevValue = $(obj).parent("td").find("select").eq(0).find("option:selected").val();
	if(value){
		$(obj).parent("td").find("#"+inputId).val(prevValue+","+value);
	}else{
		$("#"+inputId).val("");
	}
}


/********************表单序列化***************************************/
function isNull(data) {
	  if (data == undefined || data == null || data.trim() == "") { return true; }
	  return false;
}

/**
 * 获取List的参数
 * 
 * @returns {String}
 */
function listSerialize(ids, num, headEm, preListNameHead, decodeDataFlag) {
  var idArr = isNull(ids) ? null : ids.split(",");
  var listNameHeadArr = new Array();
  num = isNull(num) ? "" : num;
  if (isNull(ids)) {
    var listNameHeadEm = $("[listNameHead" + num + "]");
    if (headEm != undefined && headEm != null) {
      listNameHeadEm = $(headEm).find("[listNameHead" + num + "]");
    }
    listNameHeadEm.each(function() {
      var listNameHead = $(this).attr("listNameHead" + num);
      var haveListNameHead = false;
      for (var i = 0; i < listNameHeadArr.length; i++) {
        var listNameHeadTemp = listNameHeadArr[i];
        if (listNameHead == listNameHeadTemp) {
          haveListNameHead = true;
          break;
        }
      }
      if (!haveListNameHead) {
        listNameHeadArr.push(listNameHead);
      }
    });
  } else {
    for (var i = 0; i < idArr.length; i++) {
      var id = idArr[i];
      $("#" + id + " [listNameHead]").each(function() {
        var listNameHead = $(this).attr("listNameHead");
        var haveListNameHead = false;
        for (var i = 0; i < listNameHeadArr.length; i++) {
          var listNameHeadTemp = listNameHeadArr[i];
          if (listNameHead == listNameHeadTemp) {
            haveListNameHead = true;
            break;
          }
        }
        if (!haveListNameHead) {
          listNameHeadArr.push(listNameHead);
        }
      });
    }
  }
  var listParame = "";
  for (var i = 0; i < listNameHeadArr.length; i++) {
    var listNameHead = listNameHeadArr[i];
    var index = 0;
    if (isNull(ids)) {
      var listNameHeadEm2 = $("[listNameHead" + num + "='" + listNameHead
              + "']");
      if (headEm != undefined && headEm != null) {
        listNameHeadEm2 = $(headEm).find(
                "[listNameHead" + num + "='" + listNameHead + "']");
      }
      listNameHeadEm2.each(function() {
        var isAllDataNull = true;
        $(this).find("[listName" + num + "]").each(
                function() {
                  var listName = $(this).attr("listName" + num);
                  var listType = $(this).attr("listType" + num);
                  preListNameHead = isNull(preListNameHead) ? ""
                          : (preListNameHead);
                  if (isNull(listType) || listType != "date"
                          || !isNull($(this).val())) {
                    listParame += "&" + preListNameHead + listNameHead + "["
                            + index + "]." + listName + "=" + $(this).val();
                    if (isAllDataNull) {
                      isAllDataNull = false;
                    }
                  }
                });
        var nextNum = isNull(num) ? "2" : (Number(num) + 1) + "";
        var nextListNameHeadEm = $(this).find("[listNameHead" + nextNum + "]");
        if (nextListNameHeadEm != undefined && nextListNameHeadEm != null
                && nextListNameHeadEm.length > 0) {
          listParame += listSerialize(null, nextNum, this, listNameHead + "["
                  + index + "].", "0");
          if (isAllDataNull) {
            isAllDataNull = false;
          }
        }
        if (!isAllDataNull) {
          index++;
        }
      });
    } else {
      for (var j = 0; j < idArr.length; j++) {
        var id = idArr[j];
        $("#" + id + " [listNameHead" + num + "='" + listNameHead + "']").each(
                function() {
                  var isAllDataNull = true;
                  $(this).find("[listName" + num + "]").each(
                          function() {
                            var listName = $(this).attr("listName" + num);
                            var listType = $(this).attr("listType" + num);
                            preListNameHead = isNull(preListNameHead) ? ""
                                    : (preListNameHead);
                            if (isNull(listType) || listType != "date"
                                    || !isNull($(this).val())) {
                              listParame += "&" + preListNameHead
                                      + listNameHead + "[" + index + "]."
                                      + listName + "=" + $(this).val();
                              if (isAllDataNull) {
                                isAllDataNull = false;
                              }
                            }
                          });
                  var nextNum = isNull(num) ? "2" : (Number(num) + 1) + "";
                  var nextListNameHeadEm = $(this).find(
                          "[listNameHead" + nextNum + "]");
                  if (nextListNameHeadEm != undefined
                          && nextListNameHeadEm != null
                          && nextListNameHeadEm.length > 0) {
                    listParame += listSerialize(null, nextNum, this,
                            listNameHead + "[" + index + "].", "0");
                    if (isAllDataNull) {
                      isAllDataNull = false;
                    }
                  }
                  if (!isAllDataNull) {
                    index++;
                  }
                });
      }
    }
  }
  return decodeDataFlag == "0" ? listParame : encodeURI(listParame);
}

/**
 * 跳轉題目onchange事件
 * @param em
 */
function changeNextQuestion(em){
    changeSelectCommon(em);
}

/**
 * 所屬主問題onchange事件
 * @param em
 */
function changeFatherQuestion(em){
    changeSelectCommon(em);
}

function changeSelectCommon(em){
    var inputEm = $(em).next(".next");
    inputEm.val($(em).val());
}
