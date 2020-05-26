$(function(){
    $("#startImg").click(function() {
        $("input[name='startDate']").focus();
    });
    
    $("#endImg").click(function() {
        $("input[name='endDate']").focus();
    });
//    $("input[name='startDate']").datetimepicker({dateFormat:"yy/mm/dd"});
//    $("input[name='endDate']").datetimepicker({dateFormat:"yy/mm/dd"});
//    
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
        $("#keyNote").val(localStorage.getItem("videoKeyNote"));
        $("input[name='startDate']").val(localStorage.getItem("videoStartDate"));
        $("input[name='endDate']").val(localStorage.getItem("videoEndDate"));
        curPage = localStorage.getItem("videoCurPage");
        
        // 清除所有缓存
        localStorage.clear();
    }
    
    queryConference(curPage);
});
$("#startImg").click(function() {
    $("input[name='startDate']").focus();
});

$("#endImg").click(function() {
    $("input[name='endDate']").focus();
});
function queryConference(curPage){
	if (curPage != "" && curPage != 'undefined' && curPage != null && curPage != "null") {
		curPage = curPage;
	} else {
		curPage = 1;
	}
	var keyNote = $("#keyNote").val();
	if (keyNote.length>50){
        alertInfo("提示","字數限制不能大於50字");
        return false;
	}
	var startDate =$("input[name='startDate']").val();
	var endDate =$("input[name='endDate']").val();
	$.ajax({
	    url:context_path + "/videoConference/queryVideoConferences",
	    type:"post",
	    data:{"keyNote":keyNote,"startDate":startDate,"endDate":endDate,"curPage":curPage},
	    datatype: "html",
	    success: function(result) {
	      $("#conference_list").empty();
	      $("#conference_list").html(result);
	    },
	    error: function(XMLHttpRequest, textStatus, errorThrown) {
	      alert(textStatus + " " + XMLHttpRequest.readyState + " "
	              + XMLHttpRequest.status + " " + errorThrown);
	    }
	  });
}

function findPage(curPage){
	queryConference(curPage);
}
function viewAddConference(){
    // 缓存查询条件
    localStorage.setItem("videoKeyNote", $("#keyNote").val());
    localStorage.setItem("videoStartDate", $("input[name='startDate']").val());
    localStorage.setItem("videoEndDate", $("input[name='endDate']").val());
    localStorage.setItem("videoCurPage", $("#page_select").val());
	window.location.href = context_path + "/videoConference/viewAddVideoConference";
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
//	var flag = false;
//	if(checkedValue=="1"){
//		flag = true;
//	}
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
	
//	if(checkedValue=="1"){
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
//	}else{
//		if("area"==_type){
//			clearDefault('pGroupDiv');
//		}
//		clearDefault('pUserDiv');
//	}
}
/**
 * 全选时设置选择框的内容
 * @param type
 * @param pType
 * @returns
 */
function setDefaultData(type,pType,area){
	$.ajax({
	    url:context_path + "/videoConference/getParticipant",
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
					//查询该区底下所有组别为空的用户
					setDefaultData("group","",area);
				}else{
					info =setDivInfo("user",result.data);
					$("#pUserDiv").empty();
					if(result.data!=""&&result.data!=null&&result != "undefined" && result != undefined){
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

function addConference(){

	var areaChecked = getCheckValue("areaDiv");
	var groupChecked = getCheckValue("groupDiv");
	var userChecked = getCheckValue("userDiv");
	var groupCheckFlag = $("input[name='group']:checked").val();
	var userCheckFlag = $("input[name='user']:checked").val();
	var webex=$("#webex").val();

	if(areaChecked==""&&groupChecked==""&&userChecked==""){
		alertInfo("提示","請選擇與會人員");
		return;
	}
    if(webex==""){
        alertInfo("提示","請選擇webex賬號");
        return;
    }
	var conferenceKeyNote= $("input[name='conferenceKeyNote']").val();
	if(conferenceKeyNote==""){
		alertInfo("提示","請填写會議主旨");
		return;
	}
	var startDate = $("input[name='startDate']").val();
	if(startDate==""){
		alertInfo("提示","請選擇會議開始日期");
		return;
	}
	var hour = $("#hour option:selected").val();
	var minute = $("#minute option:selected").val();
	if(hour=="" ||minute==""){
		alertInfo("提示","請選擇會議開始時間");
		return;
	}
    var endhour = $("#endhour option:selected").val();
    var endminute = $("#endminute option:selected").val();
    if(endhour=="" ||endminute==""){
        alertInfo("提示","請選擇會議結束時間");
        return;
    }
    if(endhour*1<hour*1){
        alertInfo("提示","會議結束時間小于會議開始時間");
        return;
    }
    if(endhour*1==hour*1&&endminute*1<=minute*1){
        alertInfo("提示","會議結束時間不能小于或等于會議開始時間");
        return;
    }
	//判断是否全选所有
	var userCheck = $('input:radio[name="user"]:checked').val();
	if(userCheck=='1') {
		userCheck='1';
	}else {
		userCheck='2';
	}
	var keyNotes = $("#keyNotes").val();
	if (keyNotes.length>50){
		alertInfo("提示","字數限制不能大於50字");
		return false;
	}
	var data = $("#videoConference").serialize();
	data+="&areaStr="+areaChecked+"&groupStr="+groupChecked+"&userStr="+userChecked+"&startTime="+hour+":"+minute+"&webex="+webex+"&endTime="+endhour+":"+endminute;
	confirmInfo_1("確認", "是否新增會議？", function() {
		$.ajax({
			url:context_path + "/videoConference/addConference",
			type:"post",
			data:data,
			datatype: "json",
			success: function(result) {
				if(result.success){
					$("#hasIsReadTime").val(result.contentCount);
					alertInfo("提示",result.message,function(){
						goBack();
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
function goBack(){
	window.location.href = context_path + "/videoConference/viewVideoConference?backFlag=-1";
}

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

function clearText(){
	$("#_textarea").val("");
}

function deleteVideoConference(obj){
    // 缓存查询条件
    localStorage.setItem("videoKeyNote", $("#keyNote").val());
    localStorage.setItem("videoStartDate", $("input[name='startDate']").val());
    localStorage.setItem("videoEndDate", $("input[name='endDate']").val());
    var curPage = $("#page_select").val();
    
    var deleteIndex = $(obj).attr("dataIndex");
    var lastTr = $("tr.videoList").last();
    var lastTrIndex = $(lastTr).find("td").eq(0).text();
    if (lastTrIndex == deleteIndex && deleteIndex % 15 == 1 && curPage != 1) {
        localStorage.setItem("videoCurPage", curPage - 1);
    } else {
        localStorage.setItem("videoCurPage", curPage);
    }
    
    var username=$("#username").val();

    var createUser = $(obj).attr("createUser");
    if(createUser!=username){
        alertInfo("提示","只限發起人可以刪除");
        return;
    }
	var conferenceId = $(obj).attr("conferenceId");
	if(conferenceId==""||conferenceId=="undefiend"){
		alertInfo("提示","會議ID為空");
		return;
	}
	confirmInfo_1("確認", "是否刪除該會議？", function() {
	 $.ajax({
		    url:context_path + "/videoConference/deleteVideoConference",
		    type:"post",
		    data:{"conferenceId":conferenceId},
		    datatype: "json",
		    success: function(result) {
		    	if(result.success){
			    	  alertInfo("提示",result.message,function(){
			    		  goBack();
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

function cancel(){
	confirmInfo_1("確認", "是否取消新增會議？", function() {
		goBack();
	});
}