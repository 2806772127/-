$(function(){
//  $("input[name='startDate']").datetimepicker({dateFormat:"yy/mm/dd"});
//  $("input[name='endDate']").datetimepicker({dateFormat:"yy/mm/dd"});

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
  
  var backFlag = $("#backFlag").val();
  var curPage = 1;
  if ("-1" == backFlag) {
      $("#type option:selected").val(localStorage.getItem("messageType"));
      $("input[name='startDate']").val(localStorage.getItem("messageStartDate"));
      $("input[name='endDate']").val(localStorage.getItem("messageEndDate"));
      curPage = localStorage.getItem("messageCurPage");
      
      // 清除所有缓存
      localStorage.clear();
  }
  
  queryMessage(curPage);
});
$("#startImg").click(function() {
    $("input[name='startDate']").focus();
});

$("#endImg").click(function() {
    $("input[name='endDate']").focus();
});

function queryMessage(curPage){
	if (curPage!="" && curPage!='undefined' && curPage != null && curPage != "null") {
		curPage = curPage;
	} else {
		curPage = 1;
	}
	var messageType = $("#type option:selected").val();
	var startDate =$("input[name='startDate']").val();
	var endDate =$("input[name='endDate']").val();
	$.ajax({
	    url:context_path + "/message/queryMessage",
	    type:"post",
	    data:{"messageType":messageType,"startDate":startDate,"endDate":endDate,"curPage":curPage},
	    datatype: "html",
	    success: function(result) {
	      $("#message_list").empty();
	      $("#message_list").html(result);
            replaceMessageContent();
	    },
	    error: function(XMLHttpRequest, textStatus, errorThrown) {
	      alert(textStatus + " " + XMLHttpRequest.readyState + " "
	              + XMLHttpRequest.status + " " + errorThrown);
	    }
	  });
}

function findPage(curPage){
	queryMessage(curPage);
}

//替换消息内容url为文字 按此前往
function replaceMessageContent() {
    $(".messageContent").each(function () {
        var url = $(this).html().match(/(http|ftp|https):\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-\.,@?^=%&amp;:/~\+#]*[\w\-\@?^=%&amp;/~\+#])/g);
        $(this).html($(this).html().replace(url,'按此前往').substr(0,40));
    });
}

function viewAddMessage(){
    // 缓存查询条件
    localStorage.setItem("messageType", $("#type option:selected").val());
    localStorage.setItem("messageStartDate", $("input[name='startDate']").val());
    localStorage.setItem("messageEndDate", $("input[name='endDate']").val());
    localStorage.setItem("messageCurPage", $("#page_select").val());
	window.location.href = context_path + "/message/viewAddMessage";
}
/***
 * 全选/取消全选事件
 * @param obj
 * @returns
 */
function selectAll(obj){
	//获取属性
	var _type = $(obj).attr("name");
	var checkedValue = $("input[name='"+_type+"']:checked").val();
	//设置选中状态
	$("#"+_type+"Div").find("input[type='checkbox']").each(function(index){
		if(checkedValue=="1"){
			$(this).prop("checked",true);
		}else{
		    var _flag = $(this).prop("checked");
			if(_flag){
				$(this).prop("checked",false);
			}else{
				$(this).prop("checked",true);
			}
		}
	});
	
	if("user"!=_type){//选中时需先清除之前div的内容
		clearDefault('pUserDiv');
		if("area"==_type){
			clearDefault('pGroupDiv');
		}
		//获取选中的地区
		var area = getCheckValue("areaDiv");
		if(checkedValue=="1"){
			setDefaultData(_type,"all",area);
		}else{
			if(_type=="area"){
				setDefaultData(_type,area,area);
			}else if(_type=="group"){
				setDefaultData(_type, getCheckValue("groupDiv"),area);
			}
		}
	}
}
/**
 * 
 * @param divId
 * @returns
 */
function getCheckValue(divId){
	var checkValue = "";
	$("#"+divId).find("input[type='checkbox']").each(function(){
		if($(this).prop("checked")){
			checkValue +=$(this).val()+",";
		}
	});
	//选d中状态的value值
	if(checkValue!="" && checkValue.length > 0){
		checkValue = checkValue.substring(0,checkValue.length-1);
	}
	return checkValue;
}
/**
 * 全选时设置选择框的内容
 * @param type
 * @param pType
 * @returns
 */
function setDefaultData(type,pType,area){
	$.ajax({
	    url:context_path + "/message/getSendUser",
	    type:"post",
	    data:{"type":type,"pType":pType,"userArea":area},
	    datatype: "json",
	    success: function(result) {
	      if(result.success){
	    	  var info ="";
	    	  if("area"== type){
	    		  info =setDivInfo("group",result.data);
	    		  $("#pGroupDiv").empty();
	    		  $("#pGroupDiv").append(info);
					//查询该区底下所有的用户
					setDefaultData("group","all",area);
				}else{
					info =setDivInfo("user",result.data);
					$("#pUserDiv").empty();
					if(result.data!=""&&result.data!=null&&result.data != "undefined" && result.data != undefined){
						$("#pUserDiv").append(info);
					}
				}
			}else{
				alertInfo("提示",result.message);
			}
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			alert(textStatus + " " + XMLHttpRequest.readyState + " "
				+ XMLHttpRequest.status + " " + errorThrown);
		}
	});
}
/**
 * 拼接div内容
 * @param type
 * @param data
 * @returns
 */
function setDivInfo(type,data){
	var divId = type+"Div"
	var info = "<table><tr><td class='tdback' style='width:240px;'><span style='margin-left:10px;'>"+
	"<input type='radio' name='"+type+"' value='1'  onclick='selectAll(this)'>全選</input>&nbsp;&nbsp;&nbsp;&nbsp;"+
	"<input type='radio' name='"+type+"' value='0'  onclick='selectAll(this)'>反選</input>"+
	"</span></td></tr><tr><td><div style='margin-left:10px;height:200px;overflow:auto' id='"+divId+"'>";
	if(data!=""&& data!=null){
		for(var index in data){
			for(var key in data[index]) {
				if(data[index] == '')
					return;
				info += "<span><input type='checkbox' name='" + type + "' class='checkbox' value='" + key + "' onclick='checkChange(this)'>"
					+ data[index][key] + "</input><br/></span>";
			}
		}
	}
	info+="</div></td></tr> </table>";
	return info;
}

/**
 * 取消选中状态，清空div
 * @param type
 * @returns
 */
function clearDefault(type){
	$("#"+type).empty();
}

/***
 * check选中变化事件
 * @param obj
 * @returns
 */
function checkChange(obj){
	var type = $(obj).attr("name");
	var pDiv = $(obj).parent().parent();
	var checkValue = "";
	var _size = pDiv.find("input[type='checkbox']").size();//用于判断是否全部都有选择
	var checkSize = 0;
	pDiv.find("input[type='checkbox']").each(function(){
		if($(this).prop("checked")){
			checkSize++;
			checkValue +=$(this).val()+",";
		}
	});
	//选中状态的value值
	if(checkValue!="" && checkValue.length > 0){
		checkValue = checkValue.substring(0,checkValue.length-1);
	}
	//若没有全部选中，全选按钮取消选中状态
	$("input[name='"+type+"'][type='radio'][value='1']").prop("checked",_size==checkSize);
	$("input[name='"+type+"'][type='radio'][value='0']").prop("checked",false);

	//未选中任何区域群组或者选择变化，清空其后的div
	if("area"==type){
		$("#pGroupDiv").empty();
	}
	if("user"!=type){
		$("#pUserDiv").empty();
	}
		
	if("user"!=type && checkValue!=""){
		//获取选中的地区
	    var area = getCheckValue("areaDiv");
		setDefaultData(type,checkValue,area);
	}
	
	if ("group" == type && checkValue == "") {
        var area = getCheckValue("areaDiv");
        setDefaultData(type,checkValue,area);
    }
}

function clearText(){
	$("#_textarea").val("");
}

/**
 * 下一步
 * @returns
 */
function next(){
	var areaChecked = getCheckValue("areaDiv");

	$("input[name='group']:checked").val();
	$("input[name='user']:checked").val();
	var groupChecked = getCheckValue("groupDiv");
	var userChecked = getCheckValue("userDiv");
	   
     if(areaChecked==""&& groupChecked==""&&userChecked==""){
		   alertInfo("提示","請選擇接收者");
		   return;
	 }
     var messageType = $("select[name='messageType'] option:selected").val();
     if(messageType==""){
    	 alertInfo("提示","請選擇訊息類型");
		   return;  
     }
	 var messageKeyNote= $("input[name='messageKeyNote']").val();
	 if(messageKeyNote==""){
		   alertInfo("提示","請填写訊息主旨");
		   return;  
	 }
	 var _textarea= $("#_textarea").val();
	 if(_textarea==""){
		   alertInfo("提示","請填写訊息內容");
		   return;  
	 }
	//获取接收者群组信息
	$.ajax({
		url:context_path + "/message/getUserInfo",
		type:"post",
		data:{"userAreas":areaChecked,
			"userGroups":groupChecked,
			"users":userChecked
		},
		datatype: "json",
		success: function(result) {
			if(result.success){
				$("#sends").text(result.data);
				$("#sendType").text($("select[name='messageType'] option:selected").text());
				$("#sendContext").text($("#_textarea").val());
				$("#sendTime").text(result.sendTime);
				stepChange("step2","step1");
				$("#one").prop("class","step_gray");
				$("#two").prop("class","step_green");
			}else{
				alertInfo("提示",result.message);
			}
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			alert(textStatus + " " + XMLHttpRequest.readyState + " "
				+ XMLHttpRequest.status + " " + errorThrown);
		}
	});
}
function goBack(){
	stepChange("step1","step2");
	$("#one").prop("class","step_green");
	$("#two").prop("class","step_gray");
}

function stepChange(showId,hideId){
	$("#"+showId).show();
	$("#"+hideId).hide();
}
/**
 * 取消
 * @returns
 */
function cancel(){
	confirmInfo_1("確認", "您確定取消新增嗎？", function() {
		toSearchPage();
	});
}
/**
 * 发送
 * @returns
 */
function confirmSend(){
	var areaChecked = getCheckValue("areaDiv");
	var groupChecked = getCheckValue("groupDiv");
	var userChecked = getCheckValue("userDiv");
    var messageType = $("select[name='messageType'] option:selected").val();
    var messageTypeName = $("select[name='messageType'] option:selected").text();
	var messageKeyNote= $("input[name='messageKeyNote']").val();
	var messageContext= $("#_textarea").val();
	var messageSends =  $("#sends").text();
	var sendTime  = $("#sendTime").text();
	var groupCheckFlag = $("input[name='group']:checked").val();
	var userCheckFlag = $("input[name='user']:checked").val();
	$.ajax({
		url:context_path + "/message/addMessage",
		type:"post",
		data:{"userAreas":areaChecked,"userGroups":groupChecked,"users":userChecked,
			"messageType":messageType,"messageTypeName":messageTypeName,"messageKeyNote":messageKeyNote,
			"messageContext":messageContext,"accpectUser":messageSends,"sendTime":sendTime},
		datatype: "json",
		success: function(result) {
			if(result.success){
				$("#messageCount").text(result.messageCount);
				openDialog("show_dialog","提示",350,160);
			}else{
				alertInfo("提示",result.message);
			}
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			alert(textStatus + " " + XMLHttpRequest.readyState + " "
				+ XMLHttpRequest.status + " " + errorThrown);
		}
	});
}
function openDialog(divId, title, width, height) {
    $("#" + divId).dialog({
        title : title,
        width : width,
        height : height,
        modal : true,
        draggable : false
    });
}
function toSearchPage(){
	window.location.href = context_path + "/message/viewMessage?backFlag=-1";
}

function showContext(obj){
    var receiveId = $(obj).attr("receiveId");
    $.ajax({
        url:context_path + "/message/updateReadTime",
        type:"post",
        data:{"receiveId":receiveId},
        datatype: "json",
        success: function(result) {
            if(result.success){
                var id = $(obj).attr("messageId");
                $("#message_"+id).hide();
                var content = $("#"+id).val().replace(new RegExp(";","gm"),"<br>").replace(new RegExp("；","gm"),"<br>");
                //替换地址用<a>显示
                var url = content.match(/(http|ftp|https):\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-\.,@?^=%&amp;:/~\+#]*[\w\-\@?^=%&amp;/~\+#])/g);
                var aurl = "<a href='" + url + "' style='color: #0096C3'>按此前往</a>";
                content = auditStatus(content,url,aurl);
                $("#show_info").html(content);
                $("#messageCount").text(result.messageCount);
                $("#messageCount").text(result.contentCount);
                //从结果中获取未读的消息数给页面隐藏域使用
                $("#messageCountt").val(result.messageCount);
                $("#hasIsReadTime").val(result.contentCount);
                openDialog("show_dialog","查看訊息",500,350);
            }else{
                alertInfo("提示",result.message);
            }
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            alert(textStatus + " " + XMLHttpRequest.readyState + " "
                + XMLHttpRequest.status + " " + errorThrown);
        }
    });
}

function auditStatus(content,url,aurl){
	 
	var begin=content.indexOf("questionnaireId=");
	var last=begin+32;
	var queId = content.substring(begin+16,last+16);
	
	var addDelUpdateFlagBegin=content.indexOf("addDelUpdateFlag=");
	var Flag = content.substring(addDelUpdateFlagBegin+17,content.length-1);
	$.ajax({
	    url:context_path + "/questionnaire/queryQuestionStatus",
	    type:"post",
	    data:{"queId":queId},
	    datatype: "json",
	    async: false,
	    success: function(result) {
	      var aduFlag = result.message;
	      if(!result.success&&aduFlag==Flag){
	    	  content = content.replace(url,aurl);
	      } else {
              content = content.replace(url,'按此前往');
          }
	    },
	    error: function(XMLHttpRequest, textStatus, errorThrown) {
	      alert(textStatus + " " + XMLHttpRequest.readyState + " "
	              + XMLHttpRequest.status + " " + errorThrown);
	    }
	  });
	return content;
}

function closeDialog(){
	$("#show_dialog").dialog("close");
	isZeroCount();
}
//点击返回判断是否调用刷新页面js
function isZeroCount(){
var messageCountt = $("#hasIsReadTime").val();
if(messageCountt=='0') {
	  location.reload();
 }
}

//获取文件名称
var i=0;
function getFileName(path) {

	  if (!path&&i==0) {
	    $("#message").text("您還未選取任何文件!");
	    $("#alertMessage").dialog({
	      title: "信息提示框",
	      width: 293,
	      height: 150,
	      modal: true,
	    });
	    return false;
	  }else{
	      i++;
	      var fileName = path.substring(path.lastIndexOf(".") + 1);
	      if ("" != fileName && "xls" != fileName && "xlsx" != fileName) {
	          $("#showMessage").text("您選擇的不是Excel文件！");
	          $("#showMessage").css("color", "red");
	          $("#_upload").attr("disabled", true);

	      } else {
	          $("#showMessage").text("");
	          $("#_upload").removeAttr("disabled");

	      }
	      return false;
	  }

	}


//导入走的ajax方法
function uploadExcle(){
	 confirmInfo_1("確認", "是否導入？", function() {
			var formData = new FormData($("#form_upload")[0]);
		    var form = $("#form_upload");
		     $("#_upload").attr('disabled',"true");
			 $.ajax({
				 url:context_path + "/message/ImportExcle",
		         type:"post",
		         data:formData,
		         processData:false,
		         contentType:false,
		         success:function(result){
		             if (result.success) {
		                 alertInfo('提示', result.message,function(){
		                     window.location.reload();
		                 });
                     } else {
                         $("#showMessage").text("");
                         alertInfo('提示', result.message);
                     }
		        	 
                     if (!!window.ActiveXObject || "ActiveXObject" in window){
                         $("#_upload").attr('disabled',"true");
                         colse();
                     }else {
                         
                     }
                     $("#documentUpload").val("");
		             colse();
		             
		         },
		         error:function(e){
		             $("#documentUpload").val("");
		        	 alertInfo('提示', e);
		         }
		     });  
		 })
}

function colse() {
    $("#uploadfile").dialog('close');
}


function upload(){
	 confirmInfo_1("確認", "是否導入？", function() {
		var formData = new FormData($("#form_upload")[0]);
	    var form = $("#form_upload");
	     $("#_upload").attr('disabled',"true");
		 $.ajax({
	         url:context_path + "/indexTarget/saveUpload",
	         type:"post",
	         data:formData,
	         processData:false,
	         contentType:false,
	         success:function(result){
	        	 alertInfo('提示', result.message);
	             $("#documentUpload").val("");
	             $("#_upload").attr('disabled',"true");
	             colse();
	             i=0;
	         },
	         error:function(e){
	        	 alertInfo('提示', e);
	             i=0;
	         }
	     });  
	 })
	}


/*导入js*/
//导入窗口
function uploadTarget() {
  $("#uploadfile").dialog({
      title: "導入文檔",
      width: 600,
      height: 150,
      modal: true,
  });
}

function uploanTarget() {
    $("#uploadfile").dialog({
        title: "導入文檔",
        width: 600,
        height: 150,
        modal: true,
    });
}

function deleteMessage(obj){
    // 缓存查询条件
    localStorage.setItem("messageType", $("#type option:selected").val());
    localStorage.setItem("messageStartDate", $("input[name='startDate']").val());
    localStorage.setItem("messageEndDate", $("input[name='endDate']").val());
    var curPage = $("#page_select").val();
    
    var deleteIndex = $(obj).attr("dataIndex");
    var lastTr = $("tr.messageList").last();
    var lastTrIndex = $(lastTr).find("td").eq(0).text();
    if (lastTrIndex == deleteIndex && deleteIndex % 15 == 1 && curPage != 1) {
        localStorage.setItem("messageCurPage", curPage - 1);
    } else {
        localStorage.setItem("messageCurPage", curPage);
    }
    
	var messageId = $(obj).attr("messageId");
	if(messageId==""||messageId=="undefiend"){
		alertInfo("提示","消息ID為空");
		return;
	}
	var receiveId = $(obj).attr("receiveId");
	confirmInfo_1("確認", "是否刪除該消息？", function() {
	 $.ajax({
		    url:context_path + "/message/deleteMessage",
		    type:"post",
		    data:{"messageId":messageId,"receiveId":receiveId},
		    datatype: "json",
		    success: function(result) {
		    	if(result.success){
			    	  alertInfo("提示",result.message,function(){
			    		  toSearchPage();
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