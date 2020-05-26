$(function(){
    $("#startImg").click(function() {
        $("input[name='startDate']").focus();
    });

    $("#endImg").click(function() {
        $("input[name='endDate']").focus();
    });

    $("input[name='startDate']").datetimepicker({
        dateFormat:"yy/mm/dd",
        onClose: function(selectedDate) {
            $("input[name='endDate']").datepicker("option", "minDate", selectedDate);
        }
    });
    $("input[name='endDate']").datetimepicker({
        dateFormat:"yy/mm/dd",
        onClose: function(selectedDate) {
            $("input[name='startDate']").datepicker("option", "maxDate", selectedDate);
        }
    });
    var flag = $("#flag").val();
    if(flag == "view"){
        queryQuestionaires(1);
    }else if(flag == "add"){
        var _clone = $("#template_02").clone();
        _clone.css('display','block');
        _clone.attr("id","question_1");
        $("#question").append(_clone);
    }else if(flag =="edit"){
        //設置編輯頁面信息
        setEditDefault();
    }
});

/***查询***/
function queryIntroduceBank(curPage){
    if(curPage!="" && curPage!='undefined'){
        curPage = curPage;
    }else{
        curPage = 1;
    }
    var questionName = $("#qName").val();
    var startDate =$("input[name='startDate']").val();
    var endDate =$("input[name='endDate']").val();
    $.ajax({
        url:context_path + "/questionBank/queryQuestionaires",
        type:"post",
        data:{"questionName":questionName,"startDate":startDate,"endDate":endDate,"curPage":curPage},
        datatype: "html",
        success: function(result) {
            $("#questionaires_list").empty();
            $("#questionaires_list").html(result);
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            alert(textStatus + " " + XMLHttpRequest.readyState + " "
                + XMLHttpRequest.status + " " + errorThrown);
        }
    });
}
function findPage(curPage){
    queryQuestionaires(curPage);
}
function viewAddQuestionnaire(){
    window.location.href = context_path + "/questionnaire/viewAddQuestionnaire";
}

/**多选题添加行**/
function addRows(obj){
    var p_table = $(obj).parent("td").parent().parent();//获取父元素table
    var p_tr = $(obj).parent().parent();
//	var _clone = $("#row_template").clone();//复制模板行
    var _clone = $(p_tr).clone();//复制模板行
    var _first_td = $(p_table).find('td').eq(0);//获取首行td
    var _rows = Number(_first_td.attr("rowspan"));//获取首行占用行数
    var _row_n = _rows+1;
    var _attr = _clone.children('td').eq(0).attr("rowspan");
    if(_attr){//复制首行的话需要删除 第一列
        $(_clone).children('td').eq(0).remove();
    }
    $(_first_td).attr("rowspan",_row_n);//修改占用首行属性
    _clone.insertAfter($(p_table).find("._add_clone").last());//插入行
    setToQuestion();
}

/***多选题删除行**/
function delRows(obj){
    var _ptr = $(obj).parent().parent();//获取父tr
    var _ptable = $(obj).parent().parent().parent().find("tr");//父table
    if($(_ptable).size()<4){
        alertInfo("提示","選擇題至少保留兩行");
        return;
    }
    var _size = $(_ptr).find("td").size();//要删除tr的td列数
    var _rows = 0;
    if(_size==4){//首行的话  需复制首行的首列td(题目输入框)到下一行
        var _next_tr = $(_ptr).next("tr");
        var _next_tr_ftd = $(_next_tr).find("td").eq(0);
        var _clone_td = $(_ptr).find("td").eq(0).clone();
        _rows = Number($(_clone_td).attr("rowspan"));
        _clone_td.attr("rowspan",_rows-1);
        _clone_td.insertBefore($(_next_tr_ftd));
    }else{
        _rows = $(_ptable).find('td').eq(0).attr("rowspan");//获取首行td rowspan
        $(_ptable).find('td').eq(0).attr("rowspan",_rows-1);
    }
    $(_ptr).remove();
}

/***题目类型不同切换*/
function selectChange(obj){
    var value = $(obj).val();
    var _pdiv = $(obj).parent().parent().parent().parent().parent();
    $(_pdiv).find("#question_body").remove();
    var _clone;
    if(value=="02"){
        _clone = $("#template_01 #question_body").clone();
        $(_pdiv).append(_clone);
    }else{
        _clone =$("#template_02 #question_body").clone();
        $(_pdiv).append(_clone);
    }
    setToQuestion();
//  addPro(_clone);
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
        setFatherQuestion();
    }
}

/**添加题目**/
function addTemplate(obj){

    var curDiv = $(obj).parent().parent().parent().parent().parent().parent();
    var id= $(curDiv).attr("id");
    var cur_index = id.substring(id.indexOf("_")+1,id.length);
//	var id= $(cloneDiv).attr("id");
//	var index = id.substring(id.indexOf("_")+1,id.length);
//	var title ="题目" + (Number(index)+1);

    var _clone = $(curDiv).clone();
    var selectOption = $(curDiv).find("select[name='type']").val();
    _clone.find("select[name='type']").val(selectOption);
    _clone.insertAfter($(curDiv));
//	_clone.find("#title").text(title);
//	_clone.css('display','block');
//	_clone.attr("id","question_"+(Number(index)+1));
//	_clone.insertAfter($(cloneDiv));
    $("#question ._question").each(function(index){
//		var _id= $(this).attr("id");
//		var _index = id.substring(id.indexOf("_")+1,_id.length);
        var _index = index+1;
//		if(_index < cur_index){
//			$(this).find("#to_question").append("<option value='"+ (_index+1)+"'>第" + (_index+1) + "题</option>");
//			return;
//		}else{
        var _title = "题目" + _index;
        $(this).find("#title").text(_title);
        $(this).attr("id","question_"+ _index);

    });

    setToQuestion();
//    $("#question #to_question").each(function(){
//		 $(this).append("<option value='"+ order+"'>第"+order+"题</option>");
//	 });

//	$("#question").append(_clone);
}

/**設置上級問題的下拉框選項**/
function setFatherQuestion(){
    var ques_size = $("#question ._question").size();
    $("#question ._question").each(function(){
        var question = $(this);
        var options = "<option value=''>--請選擇--</option>";
        for(var i=1 ;i<=ques_size;i++){
            options += "<option value='"+ i+"'>第"+i+"题</option>";
        }
        question.find("#fatherQuestion").each(function(){
            var select = $(this);
            select.find("option").remove();
            select.append(options);
        });
    });
}

/**設置跳轉題目下拉框**/
function setToQuestion(){
    var ques_size = $("#question ._question").size();
    $("#question ._question").each(function(index){
        var question = $(this);
        var questionId = question.attr("id");
        var questionIndex = questionId.substring(questionId.indexOf("_")+1,questionId.length);
        var temp = Number(questionIndex) + 1;
        var options = "<option value=''>--請選擇--</option>";
        for(var i=temp ;i<=ques_size;i++){
            options += "<option value='"+ i+"'>第"+i+"题</option>";
        }
        options +="<option value='end'>跳到問卷末尾，結束作答</option>";
        question.find("._add_clone").find("#to_question").each(function(){
            var select = $(this);
            select.find("option").remove();
            select.append(options);
        });

    });
}
/***删除题目***/
function delTemplate(obj){
    var questionSize = $("#question").find("._question").size();
    if(questionSize <= 1){
        alertInfo("提示","至少要保留一題題目");
        return;
    }
    var curDiv = $(obj).parent().parent().parent().parent().parent().parent();//当前操作div
    $(curDiv).remove();//删除
    //题目重新设定序号
    $("#question ._question").each(function(index){
        var _index = index + 1;
        $(this).attr("id","question_" + _index);
        $(this).find("#title").text("第" + _index + "题");
    });
    setToQuestion();
}
/**
 * 上移
 * */
function templateUp(obj){
    var curDiv = $(obj).parent().parent().parent().parent().parent().parent();//当前div
    var curid= $(curDiv).attr("id");
    var curTitle = $(curDiv).find("#title").text();

    var preDiv = $(curDiv).prev();//前一个div
    var preTitle = $(preDiv).find("#title").text();
    var preid = $(preDiv).attr("id");

    //变换题目和id属性
    if(preTitle!="" && preid!='undefined'){
        preDiv.attr("id",curid);
        preDiv.find("#title").text(curTitle);

        curDiv.attr("id",preid);
        curDiv.find("#title").text(preTitle);
        preDiv.before(curDiv);
    }
    setToQuestion();
}
/**下移**/
function templateDown(obj){
    var curDiv = $(obj).parent().parent().parent().parent().parent().parent();//当前div
    var curid= $(curDiv).attr("id");
    var curTitle = $(curDiv).find("#title").text();

    var nextDiv = $(curDiv).next();//下一个div
    var nextTitle = $(nextDiv).find("#title").text();
    var nexteid = $(nextDiv).attr("id");
    //变换题目和id属性
    if(nextTitle!="" && nexteid!='undefined'){
        nextDiv.attr("id",curid);
        nextDiv.find("#title").text(curTitle);

        curDiv.attr("id",nexteid);
        curDiv.find("#title").text(nextTitle);
        nextDiv.after(curDiv);
    }
    setToQuestion();
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
    $("#question ._question").each(function(index){

        var question = $(this);
        question.find("._add_clone").find("#to_question").each(function(){
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
    confirmInfo_1("確認", "是否保存信息？", function() {
        $.ajax({
            url:context_path + "/questionnaire/addQuestionnaire",
            type:"post",
            data:data,
            datatype: "json",
            success: function(result) {
                if(result.success){
                    alertInfo("提示",result.message);
//			    	  alertInfo("提示",result.message,function(){
//			    		  gotoPage();
//			    	  });
                }else{
                    alertInfo("提示",result.message);
                }
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
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
        }else if(value == "04"){
            $("#prodLabel").show();
            $("#prodSelect").show();
            var isNotSelect = true;
            $("#prodCode option").each(function(){
                if (this.value == '04' || this.value == '05' || this.value == '06'
                    || this.value == '07' || this.value == '08' || this.value == '09'
                    || this.value == '10' || this.value == '11' || this.value == '12') {
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
}
/**
 * 進入編輯頁面
 * @param obj
 * @returns
 */
function viewEditQueston(obj){
    var questionanaireId = $(obj).attr("questionId");
    if(questionanaireId==""||questionanaireId=="undefined"){
        alertInfo("提示","問卷ID為空!");
        return;
    }
    window.location.href = context_path + "/questionnaire/viewEditQuestionnaire?questionnaireId=" + questionanaireId;
}
/**
 * 更新
 * @returns
 */
function updateQuestion(){
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
    $("#question ._question").each(function(index){

        var question = $(this);
        question.find("._add_clone").find("#to_question").each(function(){
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



    confirmInfo_1("確認", "是否提交修改？", function() {

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

        $.ajax({
            url:context_path + "/questionnaire/updateQuestionnaire",
            type:"post",
            data:data,
            datatype: "json",
            success: function(result) {

                bodyunmask();
                alertInfo("提示",result.message);
                if(result.message=="更新成功"){



                }
//		    	if(result.success){
//			    	  alertInfo("提示",result.message,function(){
//			    		  gotoPage();
//			    	  });
//			      }else{
//			    	  alertInfo("提示",result.message);
//			     }
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                alert(textStatus + " " + XMLHttpRequest.readyState + " "
                    + XMLHttpRequest.status + " " + errorThrown);
            }
        });
    });
}
/**
 * 刪除
 * @param obj
 * @returns
 */
function deleteQueston(obj){
    var questionnaireId = $(obj).attr("questionId");
    if(questionnaireId==""||questionnaireId=="undefiend"){
        alertInfo("提示","問卷ID為空");
        return;
    }
    confirmInfo_1("確認", "是否刪除該問卷？", function() {
        $.ajax({
            url:context_path + "/questionnaire/deleteQuestionnaire",
            type:"post",
            data:{"questionnaireId":questionnaireId},
            datatype: "json",
            success: function(result) {
                if(result.success){
                    alertInfo("提示",result.message,function(){
                        gotoPage();
                    });
                }else{
                    alertInfo("提示",result.message);
                }
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
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
    window.location.href = context_path + "/questionnaire/viewQuestionnaires";
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
        || questionType == "10" || questionType == "11"  || questionType == "12"){
        $("#prodLabel").show();
        $("#prodSelect").show();
        var isNotSelect = true;
        $("#prodCode option").each(function(){
            if (this.value == '04' || this.value == '05' || this.value == '06'
                || this.value == '07' || this.value == '08' || this.value == '09'
                || this.value == '10' || this.value == '11' || this.value == '12') {
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
    $("#question ._question").each(function(index){
        var _index = index + 1;
        $(this).attr("id","question_" + _index);
        $(this).find("#title").text("第" + _index + "题");
    });

    setToQuestion();
    setFatherQuestion();
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