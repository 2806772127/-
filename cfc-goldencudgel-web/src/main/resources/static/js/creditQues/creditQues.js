$(function () {
    $("#startImg").click(function () {
        $("input[name='startDate']").focus();
    });

    $("#endImg").click(function () {
        $("input[name='endDate']").focus();
    });

    $("input[name='startDate']").datetimepicker({
        onClose: function (selectedDate) {
            $("input[name='endDate']").datepicker("option", "minDate", selectedDate);
        }
    });
    $("input[name='endDate']").datetimepicker({
        onClose: function (selectedDate) {
            $("input[name='startDate']").datepicker("option", "maxDate", selectedDate);
        }
    });
    //修改问卷页面
    $("input[name='selectDate']").datetimepicker({
        dateFormat:'yy-mm-dd'
    });
    
    var backFlag = $("#backFlag").val();
    var curPage = 1;
    if ("-1" == backFlag) {
        $("#compilationNo").val(localStorage.getItem("creditQuesCompilationNo"));
        $("#companyName").val(localStorage.getItem("creditQuesCompanyName"));
        $("#questionnaireName").val(localStorage.getItem("creditQuesQuestionnaireName"));
        $("#startDate").val(localStorage.getItem("creditQuesStartDate"));
        $("#endDate").val(localStorage.getItem("creditQuesEndDate"));
        $("#creditName").val(localStorage.getItem("creditQuesCreditName"));
        
        curPage = localStorage.getItem("creditQuesCurPage");
        // 清除所有缓存
        localStorage.clear();
    }
    
    if($("#applyIncom_list").html() != undefined){
        queryNcoms(curPage);
    }
    removeNoneOption();
    setSaveFlag();
    var alertMsg = $("#alertMsg").val();
    if(alertMsg != undefined && alertMsg != null && alertMsg != ""){
        alertInfo("提示",alertMsg);
    }
});

function queryNcoms(curPage) {
    if (curPage != "" && curPage != 'undefined') {
        curPage = curPage;
    } else {
        curPage = 1;
    }
    var compilationNo = $("#compilationNo").val();
    var companyName = $("#companyName").val();
    var questionnaireName = $("#questionnaireName").val();
    var startDate = $("#startDate").val();
    var endDate = $("#endDate").val();
    var creditName = $("#creditName").val();
    bodymask();
    $.ajax({
        url: context_path + "/CreditQues/queryCreditQues",
        type: "post",
        data: {
            "compilationNo": compilationNo, "companyName": companyName, "questionnaireName": questionnaireName,
            "startDate": startDate, "endDate": endDate, "curPage": curPage, "creditName": creditName
        },
        datatype: "html",
        success: function (result) {
            bodyunmask();
            $("#applyIncom_list").empty();
            $("#applyIncom_list").html(result);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            bodyunmask();
            alert(textStatus + " " + XMLHttpRequest.readyState + " "
                + XMLHttpRequest.status + " " + errorThrown);
        }
    })
}

function viewReport(obj) {
    var measureId = $(obj).attr("measureId");
    var compilationNanme = $(obj).attr("compilationNanme");
    var compilationNanmes= encodeURI(encodeURI(compilationNanme));
    var curPage = $("#page_select").val();
    
    // 缓存查询条件
    localStorage.setItem("creditQuesCompilationNo", $("#compilationNo").val());
    localStorage.setItem("creditQuesCompanyName", $("#companyName").val());
    localStorage.setItem("creditQuesQuestionnaireName", $("#questionnaireName").val());
    localStorage.setItem("creditQuesStartDate", $("#startDate").val());
    localStorage.setItem("creditQuesEndDate", $("#endDate").val());
    localStorage.setItem("creditQuesCreditName", $("#creditName").val());
    localStorage.setItem("creditQuesCurPage", $("#page_select").val());
    
    window.location.href = context_path + "/CreditQues/viewReport?measureId=" + measureId+"&compilationNanme="+compilationNanmes;

}
/**
 * 返回首页
 * @returns
 */
function gotoPages(){
    if(saveFlag){
        gotoPage();
    }else{
        confirmInfo_1("確認", "問卷未儲存，是否返回查詢頁？", function() {
            // window.location.href = context_path + "/CreditQues/viewCreditQues";
            gotoPage();
        });
    }
}

function gotoPage(){
    window.location.href = context_path + "/CreditQues/viewCreditQues?backFlag=-1";
}

function findPage(curPage) {
    queryNcoms(curPage);
}

function editReport(obj) {
    var measureId = $(obj).attr("measureId");
    var compilationNanme = $(obj).attr("compilationNanme");
    compilationNanme = encodeURIComponent(compilationNanme);
    var curPage = $("#page_select").val();
    // 缓存查询条件
    localStorage.setItem("creditQuesCompilationNo", $("#compilationNo").val());
    localStorage.setItem("creditQuesCompanyName", $("#companyName").val());
    localStorage.setItem("creditQuesQuestionnaireName", $("#questionnaireName").val());
    localStorage.setItem("creditQuesStartDate", $("#startDate").val());
    localStorage.setItem("creditQuesEndDate", $("#endDate").val());
    localStorage.setItem("creditQuesCreditName", $("#creditName").val());
    localStorage.setItem("creditQuesCurPage", $("#page_select").val());

    var compilationNo = $(obj).attr("compilationNo");
    var questionnaireName = $(obj).attr("questionnaireName");
    //var questionnaireName= encodeURI(encodeURI(questionnaireNames));
    questionnaireName = encodeURIComponent(questionnaireName);
    var creditName = $(obj).attr("creditName");
    creditName = encodeURIComponent(creditName);
    var startDate = $(obj).attr("startDate");
    var endDate = $(obj).attr("endDate");

    var questionType = $(obj).attr("questionType");
    var description = $(obj).attr("description");
    description = encodeURIComponent(description);
    //window.location.href = context_path + "/CreditQues/editReport?measureId=" + measureId+"&compilationNanme="+compilationNanmes;
    window.location.href = context_path + "/CreditQues/editReport?measureId=" + measureId+"&compilationNanme="+compilationNanme+"&questionnaireName="+questionnaireName+"&creditName="+creditName+"&startDate="+startDate+"&endDate="+endDate+"&compilationNo="+compilationNo+"&questionType="+questionType+"&description="+description;
}

function clickImgFocus(obj) {
    $(obj).prev("input").focus(); //获取上一个元素的焦点
}

//儲存
function updateQuestionRecord() {
    var alertMsg = "";
    var hasNullAnswer = false;
    $(".tr_answer").each(function(){
        if(!$(this).hasClass("remove")) {
            $(this).find(".answerInput").each(function () {
                if (!hasNullAnswer) {
                    var answerValue = $(this).val();
                    if (isNull(answerValue)) {
                        hasNullAnswer = true;
                    }
                }
            });
            if (hasNullAnswer) {
                var qName = $(this).children("td").first().text();
                alertMsg = qName + "未填寫！";
                return false;
            }
        }
    });
    if(hasNullAnswer){
        alertInfo("提示",alertMsg);
        return;
    }
    confirmInfo_1("確認", "是否提交修改？", function() {
        //清空隐藏的移除答案
        $(".remove .answerInput").val("");
        var data = $("#questionForm").serialize();
        data += listSerialize("questionRecord");
        bodymask();
        $.ajax({
            url:context_path + "/CreditQues/updateQuestionRecord",
            type:"post",
            data:data,
            datatype: "json",
            success: function(result) {
                bodyunmask();
                if(result.success){
                    saveFlag = true;
                    alertInfo("提示","儲存成功",function(){
                        gotoPage();
                    });
                }else{
                    alertInfo("提示",result.message);
                }
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                alert(textStatus + " " + XMLHttpRequest.readyState + " "
                    + XMLHttpRequest.status + " " + errorThrown);
                bodyunmask();
            }
        });
    });
}

//暂存
function temporaryStorage() {
    confirmInfo_1("確認", "是否提交修改？", function() {
        //清空隐藏的移除答案
        $(".remove .answerInput").val("");
        var data = $("#questionForm").serialize();
        data += listSerialize("questionRecord");
        bodymask();
        $.ajax({
            url:context_path + "/CreditQues/temporaryStorage",
            type:"post",
            data:data,
            datatype: "json",
            success: function(result) {
                bodyunmask();
                if(result.success) {
                    saveFlag = true;
                }
                alertInfo("提示",result.message);
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                alert(textStatus + " " + XMLHttpRequest.readyState + " "
                    + XMLHttpRequest.status + " " + errorThrown);
                bodyunmask();
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
                                + index + "]." + listName + "=" + encodeURIComponent($(this).val());
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
                                        + listName + "=" + encodeURIComponent($(this).val());
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
    return listParame;
}

/********************表单序列化***************************************/
function isNull(data) {
    if (data == undefined || data == null || data.trim() == "") { return true; }
    return false;
}

/**
 * 修改问卷页面重新选择答案
 */
function chengeAnswer(obj) {
    //请求后端接口需要的参数
    var questionId = $(obj).attr("questionId");
    var questionnaireId = $(obj).attr("questionnaireId");
    var answer = obj.value;
    var oldAnswer = $(obj).attr("oldAnswer");
    oldAnswer = (oldAnswer == undefined || oldAnswer == null)?"":oldAnswer;
    $(obj).attr("oldAnswer",answer); // 本次答案作为下次更改答案的old answer
    //新增题目需要的参数
    var cusCode = $(obj).attr("cusCode"); //客户id
    cusCode = (cusCode == undefined || cusCode == null)?"":cusCode;
    var measureId = $(obj).attr("measureId"); // 测字id

    $.ajax({
        url:context_path + "/CreditQues/chengeAnswer",
        type:"post",
        data:{
            "questionId": questionId,
            "questionnaireId":questionnaireId,
            "answer":answer,
            "oldAnswer":oldAnswer,
            "measureId":measureId
        },
        success: function(result) {
            // alertInfo("提示",result.message);
            if(result.isChange){
                //删除不需要的id
                var idObject;
                $.each(result.noNeedQuesId,function (n,value) {
                    // 删除问题
                    idObject = document.getElementById(value);
                    if (idObject != null)
                        idObject.parentNode.removeChild(idObject);
                });
                //在当前问题下插入问题
                var tr1 = "";
                $.each(result.needQuestion,function (n, value) {
                    tr1 += "<tr id='" + value.id + "' style='margin-left:15px; height: 40px' listnamehead='records' >";
                    tr1 += "<td  style=\"text-align: right;\">" + value.name + "</td>";
                    tr1 += "<td style='text-align:left;width:12.5%;padding-left:10px;'>";
                    tr1 += "<input type='hidden' listName='questionName' value='" + value.name + "'/>";
                    // <!-- type 01-问答题  02-单选题 03-日期（年月日） 04-多选题  05-日期（年月） -->
                    var answer ="";
                    if (value.answer!=null) {
                        answer = value.answer;
                    }

                    if (value.questionType == "01") {
                        tr1 += "<input name='questionType01' type='text' style='width: 90%;height: 30px;padding: 0 10px'  listName='questuibAnswer' value='"+answer+"' />";
                    }
                    if (value.questionType == "02") {
                        tr1 += "<select name='questionType02' listName='questuibAnswer' onchange='chengeAnswer(this);'\n" +
                                "     questionId='" + value.id + "' questionnaireId='" + questionnaireId + "'\n" +
                                "     cusCode='" + cusCode + "' measureId='" + measureId + "'\n" +
                                "     oldAnswer='" + answer + "'\n" +
                                " >";
                        tr1 += isNull(answer)?"<option style='display: none'></option>":"";
                        $.each(value.answers,function (index,answerValue) {
                            tr1 += "<option ";
                            if (answer == answerValue.answer) {
                                tr1 += " selected='true' ";
                            }
                            tr1 += "  value='" + answerValue.answer + "' >" + answerValue.answer + "</option> ";
                        })
                        tr1 += " </select> ";
                    }
                    if (value.questionType == "03") {
                        tr1 += "<input name='questionType03' class='input' type='text' style='padding: 0 10px' name ='selectDate' listName='questuibAnswer' readonly />&nbsp;\n" +
                            "<img  name='selectDateImg' src='../images/measureWord/u22.png' onclick='clickImgFocus(this); value='"+answer+"'' />";
                    }
                    if (value.questionType == "04") {
                        $.each(value.answers,function (index,answerValue) {
                            var index=0;
                            var sum=1;
                                for (var i = 0; i <answer.length ; i++) {
                                if (answerValue==answer[i]&&sum!=0) {
                                    tr1 += "<input name='questionType04' type ='checkbox' name='selectAnswers' listName='questuibAnswer'\n" +
                                        " value='" + answerValue.answer + "'    checked='checked'  /> " + answerValue.answer;
                                    sum=0;
                                }else{
                                    index++;
                                }
                                if (index==i&&sum!=0) {
                                    tr1 += "<input type ='checkbox' name='selectAnswers' listName='questuibAnswer'\n" +
                                        " value='" + answerValue.answer + "'   /> " + answerValue.answer;
                                }
                            }
                        })
                    }
                  
                    if (value.questionType == "05") {
                    	var date=new Date;
                    	var year=date.getFullYear(); 
                    	var y = year-1911, m = 12;
                    	var yearAnswer = "";
                    	var monthAnswer = "";
                    	if(!isNull(answer)){
                            var arr = answer.split('-');
                            if(arr.length>0 && !isNull(arr[0])){
                                yearAnswer = arr[0];
                            }
                            if(arr.length>1 && !isNull(arr[0])){
                                monthAnswer = arr[1];
                            }
                        }
                        var year = isNull(yearAnswer)?"<option style='display: none'></option>":"";
                        var month = isNull(monthAnswer)?"<option style='display: none'></option>":"";
                    	for(var i = y; i >= 1; i--) {
                    	    year += '<option  value="'+ i +'"'+(yearAnswer==i?"selected":"")+'>'+ i +'</option>';
                    	}
                    	for(var j = 1; j <= m; j++) {
                    		if (j <= m) {
                    			if(j<10){
                    				month += '<option  value="'+ +"0"+j +'"'+(monthAnswer==("0"+j)?"selected":"")+'>'+ +"0"+j +'</option>'
                    			}else{
                    				month += '<option  value="'+ j +'"'+(monthAnswer==j?"selected":"")+'>'+ j +'</option>'
                    			}
                    		}
                    	}
                        tr1 += "<span>民國&nbsp;";
                        tr1 += "<select name ='questionType051' listName='republicYear'>" + year + "</select>&nbsp;年";
                        tr1 += "<select name ='questionType052' listName='republicMonth'>" + month + "</select>&nbsp;月";
                        tr1 += "</span>";
                    }
                    
                    if (value.questionType == "07") {
                    	var date=new Date;
                    	var year=date.getFullYear(); 
                    	var y = year-1919, m = 11;
                    	var yearAnswer = "";
                    	var monthAnswer = "";
                    	if(!isNull(answer)){
                            var arr = answer.split('-');
                            if(arr.length>0 && !isNull(arr[0])){
                                yearAnswer = arr[0];
                            }
                            if(arr.length>1 && !isNull(arr[0])){
                                monthAnswer = arr[1];
                            }
                        }
                        var year = isNull(yearAnswer)?"<option style='display: none'></option>":"";
                        var month = isNull(monthAnswer)?"<option style='display: none'></option>":"";
                    	for(var i = 0; i <= y; i++) {
                    	    year += '<option  value="'+ i +'"'+(yearAnswer==i?"selected":"")+'>'+ i +'</option>';
                    	}
                    	for(var j = 0; j <= m; j++) {
                    		if (j <= m) {
                    			if(j<10){
                    				if(j==0) {
                    					month += '<option  value="'+ j +'"'+(monthAnswer==j?"selected":"")+'>'+ j +'</option>'	
                    				}else{
                    					month += '<option  value="'+ +"0"+j +'"'+(monthAnswer==("0"+j)?"selected":"")+'>'+ +"0"+j +'</option>'	
                    				}
                    			}else{
                    				month += '<option  value="'+ j +'"'+(monthAnswer==j?"selected":"")+'>'+ j +'</option>'
                    			}
                    		}
                    	}
                    	tr1 += "<span>";
                        tr1 += "<select name ='questionType071' listName='republicYear'>" + year + "</select>&nbsp;年";
                        tr1 += "<select name ='questionType072' listName='republicMonth'>" + month + "</select>&nbsp;月";
                        tr1 += "</span>";
                    }
                    
                    if (value.questionType == "06") {
                    	var date=new Date;
                    	var year=date.getFullYear(); 
                    	var y = year-1911;
                    	var yearAnswer = "";
                    	if(!isNull(answer)){
                            var arr = answer.split('-');
                            if(arr.length>0 && !isNull(arr[0])){
                                yearAnswer = arr[0];
                            }
                        }
                        var year = isNull(yearAnswer)?"<option style='display: none'></option>":"";
                    	for(var i = y; i >= 1; i--) {
                    	    year += '<option  value="'+ i +'"'+(yearAnswer==i?"selected":"")+'>'+ i +'</option>';
                    	}
                    	
                        tr1 += "<span>民國&nbsp;";
                        tr1 += "<select name ='questionType061' listName='questuibAnswer'>" + year + "</select>&nbsp;年";
                        tr1 += "</span>";
                    }
                    
                    /*if (value.questionType == "05") {
                        var arr = answer.split('-');

                        tr1 += "<span>民國&nbsp;";
                        tr1 += "<input type='text' name ='republicYear'  value='"+arr[0]+"'  listName='republicYear' style='text-align:center;width: 30px;padding: 0 10px'maxlength='3'/>&nbsp;年";
                        tr1 += "<input type='text' name ='republicMonth' value='"+arr[0]+"'  listName='republicMonth' style='text-align:center;width: 30px;padding: 0 10px'  maxlength='2'/>&nbsp;月";
                        tr1 += "</span>";
                    }*/

                    //<!-- 其他答题记录表字段 -->
                    tr1 += "<input type='hidden' value='" + value.questionType + "' listName='type'/>";
                    tr1 += "<input type='hidden' value='" + cusCode + "' listName='cusCode'/>";
                    tr1 += "<input type='hidden' value='" + result.questionnaireType + "' listName='questionType'/>";
                    tr1 += "<input type='hidden' value='" + value.id + "' listName='questionId'/>";
                    tr1 += "<input type='hidden' value='" + measureId + "' listName='measureId'/>";
                    tr1 += "<input type='hidden' value='" + value.ruleItem + "' listName='ruleItem'/>";
                    tr1 += "<input type='hidden' value='" + value.applyItem + "' listName='applyItem'/>";
                    tr1 += "<input type='hidden' value='" + value.questionLevel + "' listName='questionLevel'/>";
                    tr1 += "<input type='hidden' value='" + value.fatherQuestion + "' listName='fatherQuertion'/>";
                    //这个是答题记录表id 新增的问题需要重新生成
                    // tr1 += "<input type='hidden' value='" + value.id + "' listName='id'/>";
                    //时间会重新取当前时间，所以不需要
                    // tr1 += "<!--<input type='hidden' value='${currRecord.createTime}' listName='createTime' listType='date'/>-->";
                    tr1 += "</td>";
                    tr1 += "</tr>";
                });
                if (tr1 != "") {
                    $("#"+ questionId).after(tr1);
                    $("input[name='selectDate']").datetimepicker({
                        dateFormat:'yy-mm-dd'
                    });
                }
            }
            removeNoneOption();
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            alert(textStatus + " " + XMLHttpRequest.readyState + " "
                + XMLHttpRequest.status + " " + errorThrown);
        }
    });
}

/**
 * 查询页面下载pdf
 */
function downloadPdf(obj) {
    //测字id
    var measureId = $(obj).attr("measureId");
    window.location.href = context_path + "/CreditQues/downloadPdf?measureId=" + measureId;
}

/**
 * 查看页面下载pdf
 */
function pdfDown(obj) {
    //测字id
    var measureId = $("#measureId").val();
    window.location.href = context_path + "/CreditQues/downloadPdf?measureId=" + measureId;
}

function backPage(isEdit) {
    window.location.href = context_path + "/CreditQues/viewCreditQues?backFlag=-1";
}

/**
 * 显示或隐藏
 */
function showOrHide(em){
    var contentEm = $(em).parents("table").find(".content");
    if(contentEm.css("display") != "none"){
        contentEm.css("display","none");
    }else{
        contentEm.css("display","");
    }
}

/**
 * 删除隐藏的下拉框选项
 */
function removeNoneOption(){
    $("option").each(function(){
        if($(this).css("display")=="none"){
            var parentEm = $(this).parent();
            $(this).remove();
            parentEm.val("");
        }
    });
}

function changeAnswer(em){
    var trEm = $(em).parents("tr");
    var fatherQuestion = trEm.attr("fatherQuestion");
    fatherQuestion = fatherQuestion!=null?fatherQuestion:trEm.attr("sortNo");
    var tableEm = $(em).parents("table");
    var nextIndexIdArr = new Array();
    var fatherQAEm = tableEm.find("tr[sortNo='"+fatherQuestion+"']").find(".answer");
    var fatherQAName = fatherQAEm.attr("name");
    var nextIndexId = "1";
    nextIndexIdArr.push(nextIndexId);
    for(var i=0;i<tableEm.find(".tr_answer").length;i++) {
        var flag = false;
        tableEm.find(".tr_answer").each(function () {
            if(!flag){
                var sortNo = $(this).attr("sortNo");
                if(nextIndexId == sortNo){
                    var qAEm = $(this).find(".answer");
                    var name = qAEm.attr("name");
                    if (!isNull(name) && name == "questionType02") {
                        nextIndexId = qAEm.find("option:selected").attr("nextQuestion");
                        if (isNull(nextIndexId)) {
                            qAEm.find("option").each(function () {
                                if(isNull(nextIndexId)){
                                    nextIndexId = $(this).attr("nextQuestion");
                                }
                            });
                        }
                    } else {
                        nextIndexId = qAEm.attr("nextQuestion");
                    }
                    if (!isNull(nextIndexId)) {
                        nextIndexIdArr.push(nextIndexId);
                    }
                    flag =true;
                }
            }
        });
    }
    tableEm.find(".tr_answer").each(function(){
        var sortNo = $(this).attr("sortNo");
        var isShow = false;
        for(var i=0;i<nextIndexIdArr.length;i++){
            var indexId  = nextIndexIdArr[i];
            if(indexId == sortNo){
                isShow = true;
                break;
            }
        }
        if(isShow){
            $(this).show();
            $(this).removeClass("remove");
        }else{
            $(this).hide();
            $(this).addClass("remove");
        }
    });
}
