$(function(){
	var contentEm = $("#productManagementcom_list");
	contentEm.css("display","none");
	var editFlag = $("#editFlag").val();
	var CheckEditFlag = $("#CheckEditFlag").val();
	//需求修改，修改页面也不可以修改问卷
	$("input").attr("disabled",true);
	 $("td").not('.not').attr("disabled",true);
	 $("select").attr("disabled",true);
	 $("textarea").attr("disabled",true);
	 $("#questionnaireConfig").attr("disabled",false);
	 $("#backQueryPage").attr("disabled",false);
	 $("#commitId").attr("disabled",false);
	 $("#turnBackPage").attr("disabled",false);
	 $("#questionNames").attr("disabled",false);
	 //$('input[name="questionNameCheckBox"]').attr("disabled",false);
	 $('input[name="questionnaireConfig"]').attr("disabled",false);
	 
	 $('input[name="questionNamess"]').attr("disabled",false);
	 $('input[name="auditState"]').attr("disabled",false);
	 $('textarea[name="auditRemark"]').attr("disabled",false);
	 
	if(editFlag=="undefined"||editFlag==undefined){
		 $("input").attr("disabled",true);
		 $("td").not('.not').attr("disabled",true);
		 $("select").attr("disabled",true);
		 $("textarea").attr("disabled",true);
		 $("#questionnaireConfig").attr("disabled",false);
		 $("#backQueryPage").attr("disabled",false);
		 $("#questionNames").attr("disabled",false);
		 //$('input[name="questionNameCheckBox"]').attr("disabled",false);
		 $('input[name="questionnaireConfig"]').attr("disabled",false);
	}
	if(CheckEditFlag=="undefined"||CheckEditFlag==""){
		 $("input").attr("disabled",true);
		 $("td").not('.not').attr("disabled",true);
		 $("select").attr("disabled",true);
		 $("textarea").attr("disabled",true);
		 $("#questionnaireConfig").attr("disabled",false);
		 $("#backQueryPage").attr("disabled",false);
		 $("#questionNames").attr("disabled",false);
		 $('input[name="auditState"]').attr("disabled",true);
		 $('textarea[name="auditRemark"]').attr("disabled",true);
		 $('input[name="questionnaireConfig"]').attr("disabled",false);
	}
	   $("input[name='startDate']").datetimepicker({
	        timeFormat: "HH:mm",
	        dateFormat: "yy/mm/dd"
	    });
	   
	    var questionnaireType = $("#questionnaireType").val();
	    var questionnaireTypeArray = new Array();
		   questionnaireTypeArray = questionnaireType.split(",")
		$('input[name="questionNameCheckBox"]').each(function(n){
			var $that = $(this);
			var s = $that.attr("questionname");
			if(questionnaireTypeArray.indexOf(s)>-1){
				$that.attr("checked","checked");
				checkChange(this);
			}
		});
		   $('input[name="questionNamess"]').each(function(n){
				var $that = $(this);
				var s = $that.attr("questionName");
				if(questionnaireTypeArray.indexOf(s)>-1){
					$that.attr("checked","checked");
					checkChange(this);
				}
			});
});





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
    // selectChange(selEm);//初始化选项文字
	_clone.insertAfter($(curDiv));
    setToQuestion(obj,"01");
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
	if (value == '1') {
		var _pdiv = $(obj).parent().parent().parent().parent().parent();
		$(_pdiv).find(".selectLevel").each(function () {
			this.style.display = "none";
		});
	}else{
		var _pdiv = $(obj).parent().parent().parent().parent().parent();
		$(_pdiv).find(".selectLevel").each(function () {
			this.style.display = "inline";
		});
		$(obj).val(value);
		// setFatherQuestion();
	}
    changeSelectCommon(obj);
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

function qSelectChange(obj){
	$("#questionType").val($(obj).find("option:selected").val());
}


/**
 * 显示或隐藏
 */
//問卷設置显示隐藏
function  showOrHide(em) {
	var parent = $(em).parent();
	var questionId = parent.attr('value');
	var contentEm = $("#productManagementcom_list");
	var editFlag = $("#editFlag").val();
	var CheckEditFlag = $("#CheckEditFlag").val();
	var pDiv = $(em).parent().parent();
	var em = $(em);
	//权限标识
	var authorityFlag = $("#authorityFlag").val();
	if(em.hasClass('gpic')){
		$('input[name="questionnaireConfig"]').each(function(){
			$(this).removeClass('gpic').addClass('pic');
		});
	}else{
		$('input[name="questionnaireConfig"]').each(function(){
			$(this).removeClass('gpic').addClass('pic');
		});
		em.addClass('gpic');
	}
	if(em.hasClass('gpic')){
	   contentEm.css("display","none");
	}
	//判断是否进入有无权限的页面路径
	var url = '';
	var checkFlag;
	if(authorityFlag=="undefined"||authorityFlag==undefined){
		url= context_path + "/product/loadFbQuestionnaire";
	}else{
		url= context_path + "/product/loadFbQuestionnaireForCheck";
		checkFlag = 'checkFlag';
	}
    $.ajax({
        url:url,
        type: "post",
        data: {
            "questionId": questionId
        },
        datatype: "html",
        success: function(result) {
            $("#productManagementcom_list").empty();
            $("#productManagementcom_list").html(result);
            if(contentEm.css("display") != "none"){
             	contentEm.css("display","none");
             }else{
             	contentEm.css("display","");
             }
            $(".notType").attr("disabled",true);
            $("#chushai").attr("disabled",true);
            $("#chanpin").attr("disabled",true);
            //需求修改，把修改页面也不能修改问卷
             $("input").attr("disabled",true);
    		 $("td").not('.not').attr("disabled",true);
    		 $("select").attr("disabled",true);
    		 $("textarea").attr("disabled",true);
    		 $("#questionnaireConfig").attr("disabled",false);
    		 $("#backQueryPage").attr("disabled",false);
    		 $("#commitId").attr("disabled",false);
    		 $("#turnBackPage").attr("disabled",false);
    		 $("#questionNames").attr("disabled",false);
    		 $('input[name="questionnaireConfig"]').attr("disabled",false);
    		 //$('input[name="questionNameCheckBox"]').attr("disabled",false);
    		 $("#viewOrEditFlag").css("display","none");
    		 
    		 
            
            
            
         	if(editFlag=="undefined"||editFlag==undefined){
         		 $("input").attr("disabled",true);
         		 $("td").not('.not').attr("disabled",true);
         		 $("select").attr("disabled",true);
         		 $("textarea").attr("disabled",true);
         		 $("#questionnaireConfig").attr("disabled",false);
         		 $("#backQueryPage").attr("disabled",false);
         		 $("#questionNames").attr("disabled",false);
         		 $('input[name="questionnaireConfig"]').attr("disabled",false);
         		 //$('input[name="questionNameCheckBox"]').attr("disabled",false);
         		 $("#viewOrEditFlag").css("display","none");
         		   
         	}
         	if(typeof(authorityFlag)!="undefined"){
         		if(CheckEditFlag!=""){
             		$("select").attr("disabled",true);
             		$("td").not('.not').attr("disabled",true);
             		
             		$('input[name="questionNamess"]').attr("disabled",false);
             		$('input[name="auditState"]').attr("disabled",false);
             		$('textarea[name="auditRemark"]').attr("disabled",false);
         		}else{
             		$("select").attr("disabled",true);
             		$("td").attr("disabled",true);
             		$('input[name="auditState"]').attr("disabled",true);
             		$('textarea[name="auditRemark"]').attr("disabled",true);
             		
         		}
         	}
         	
         //加载问卷3默认答案
          var prodType = $("#prod").val();
      	  $("#question .question_div").each(function(){
      		var _value = $(this).find(".questionTdClass input[name='level']").val();
      		setSelectLevelChange($(this),_value);
      		showOrHideDefualtInput(prodType);
      	  });
         	
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            alert(textStatus + " " + XMLHttpRequest.readyState + " "
                + XMLHttpRequest.status + " " + errorThrown);
        }
    })
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

//選擇問卷js
function checkChange(obj){
	var questionId = $(obj).attr("questionId");
	var questionName = $(obj).attr("questionName");
	var questionnaireConfigSpan="";
	var pDiv = $(obj).parent().parent();
	var questionnaireConfigDiv = $("#questionnaireConfigDiv");
	var contentEm = $("#productManagementcom_list");
		if($(obj).is(":checked")){
			questionnaireConfigSpan +=	"<span name='questionIdSpan' type='hidden' value='"+questionId+"'><input type='button' class='pic'  name='questionnaireConfig' value='"+questionName+"'  onclick='showOrHide(this)' ></input>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
	       + "</span>";
			questionnaireConfigDiv.append(questionnaireConfigSpan);
		}else{
			$('input[name="questionnaireConfig"]').each(function(n){
				var $that = $(this);
				if(questionName==$that.val()){
					$that.parent().remove();
				}
			});
		    contentEm.css("display","none");
		}
}


//
/***
 * 修改配置提交
 * @returns
 */
function updateQuestion(){
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
	
	var estimatedLaunchTime = $('input[name="startDate"]').val();
	if(estimatedLaunchTime==""){
		alertInfo("提示","請填寫預計上線時間");
		return; 	
	}
	

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
	var prodName = $("#prodNameHidden").val();
	var estimatedLaunchTime = $('input[name="startDate"]').val();
	var prodId = $("#prodId").val();
	var productVersion = $("#productVersion").val();
	var queId = queIds.join(',');//储存问卷id
	var LaunchTime = estimatedLaunchTimes.join(',');//预计上线时间
	var prodType = prodTypes.join(',');//储存问卷类别
	var commitIds = [];
	            $('input[name="questionNameCheckBox"]').each(function(n){
				var $that = $(this);
				if($that.is(":checked")){
					commitIds.push($that.val());
				}
			});
			var commitId = commitIds.join(",");//提交问卷Id
	data +="&"+$.param({"prodName":prodName, "estimatedLaunchTime" :estimatedLaunchTime,"prodId" :prodId,"productVersion":productVersion,"queId":queId,"LaunchTime":LaunchTime,"commitId":commitId,"prodType":prodType});
    confirmInfo_1("確認", "是否提交信息？", function() {
    	//bodymask();
	    $.ajax({
		    url:context_path + "/product/updateProduct",
		    type:"post",
		    data:data,
		    datatype: "json",
		    success: function(result) {
		    	/*bodyunmask();
				if(result.success){
				    saveFlag = true;
                    alertInfo("提示",result.message,function(){
                        gotoPage();
                    });
				}else{
				    alertInfo("提示",result.message);
				}*/
		    	openNewWinForCOmmit();
		    },
		    error: function(XMLHttpRequest, textStatus, errorThrown) {
		    	bodyunmask();
		      	alert(textStatus + " " + XMLHttpRequest.readyState + " "
		              + XMLHttpRequest.status + " " + errorThrown);
		    }
		  });
    });
}

//
/***
 * 修改配置储存
 * @returns
 */
function saveQuestion(){
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
	var estimatedLaunchTime = $('input[name="startDate"]').val();
	var questionnaireConfig = $('input[name="questionnaireConfig"]').val();
	if(questionnaireConfig=="undefined"||questionnaireConfig==undefined){
		alertInfo("提示","請選擇問卷設置");
		return;
	}
	if($('input[name="questionnaireConfig"]').hasClass('gpic')==false){
		alertInfo("提示","請選擇問卷設置");
		return;
	    }
	var zanFlag="";
	var zanTemporaryStorage = temporaryStorage.join(",");//暂存储存过的问卷，用来判断，没有提交前，储存同一个类型的问卷，更新
	if(prodTypes.indexOf(prodCode)>-1){
		var zanFlag="zan";
	}
	
	var productVersion = $("#productVersion").val();
	var prodId = $("#prodId").val();
	data +="&"+$.param({"estimatedLaunchTime":estimatedLaunchTime,"productVersion":productVersion,"prodId":prodId,"estimatedLaunchTime":estimatedLaunchTime,"zanFlag":zanFlag,"zanTemporaryStorage":zanTemporaryStorage});
    confirmInfo_1("確認", "是否儲存信息？", function() {
    	//bodymask();
	    $.ajax({
		    url:context_path + "/product/saveProduct",
		    type:"post",
		    data:data,
		    datatype: "json",
		    success: function(result) {
		    	if(result.success&&result.firstInsert){
		    		queIds.push(result.questionId);
		    		prodTypes.push(result.prodCode);
		    		temporaryStorage.push(result.questionId+"-"+result.prodCode);
		    		estimatedLaunchTimes.push(result.estimatedLaunchTime);
		    	}
		    	openNewWin();
		    },
		    error: function(XMLHttpRequest, textStatus, errorThrown) {
		    	bodyunmask();
		      	alert(textStatus + " " + XMLHttpRequest.readyState + " "
		              + XMLHttpRequest.status + " " + errorThrown);
		    }
		  });
    });
}

function openNewWin(){
	   openDialog("newWindow", "提示", 350, 200);
}

function hideTurnBack(){
	$("#newWindow").dialog('close');
}

function openNewWinForCOmmit(){
	   openDialog("commitWindow", "提示", 350, 200);
}

/********************表单序列化***************************************/
function isNull(data) {
	  if (data == undefined || data == null || data.trim() == "") { return true; }
	  return false;
}

/**
 * 返回首页
 * @returns
 */
function gotoPage(){
         window.location.href = context_path + "/product/productManagement";
            }


//审核页面的提交js

function commitForCheckPage(){
	//var checkResult = $('input[name="auditState"]').val();
	var checkResult;
	$("#auditStateFlag").find("input[name='auditState']").each(function(){
		var $that = $(this);
		if($that.is(":checked")){
		   checkResult = $that.val();
		}
	});	
	var remarkEncodeUrl = $('textarea[name="auditRemark"]').val();
	var remark= encodeURI(encodeURI(remarkEncodeUrl));
	var prodId = $("#prodId").val();
	var commitName = $("#commitName").val();
	//window.location.href = context_path + "/product/commitForCheckPage?checkResult="+checkResult+"&remark="+remark+"&prodId="+prodId+"&commitName="+commitName;
	confirmInfo_1("確認", "是否提交審核？", function() {
	    $.ajax({
		    url:context_path + "/product/commitForCheckPage",
		    type:"post",
		    data:{"checkResult":checkResult,"remark":remark,"prodId":prodId,"commitName":commitName},
		    datatype: "json",
		    success: function(result) {
		    	openNewWinForCOmmit();
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
