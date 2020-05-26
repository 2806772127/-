$(function(){
  queryAuditConfiguration(1);
});

function queryAuditConfiguration(curPage){
	if (curPage!="" && curPage!='undefined' && curPage != null && curPage != "null") {
		curPage = curPage;
	} else {
		curPage = 1;
	}
	var productCode = $("#productFunction").val();
	var agentUserCode = $("#agentUserCode").val();
	var agentUserName = $("#agentUserNameCondition").val();
	var approveUserCode = $("#approveUserCode").val();
	var approveUserName = $("#approveUserNameCondition").val();
	
	$.ajax({
	    url:context_path + "/auditCOnfiguration/queryAuditConfiguration",
	    type:"post",
	    data:{"productCode":productCode,"agentUserCode":agentUserCode,"agentUserName":agentUserName,"approveUserCode":approveUserCode,"approveUserName":approveUserName,"curPage":curPage},
	    datatype: "html",
	    success: function(result) {
	      $("#auditConfiguration_list").empty();
	      $("#auditConfiguration_list").html(result);
	    },
	    error: function(XMLHttpRequest, textStatus, errorThrown) {
	      alert(textStatus + " " + XMLHttpRequest.readyState + " "
	              + XMLHttpRequest.status + " " + errorThrown);
	    }
	  });
}

function findPage(curPage){
	queryAuditConfiguration(curPage);
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

function openNewWin(){
	openDialog("newWindow", "新增審核配置", 1000, 700);
}
function closenew() {
    $("#newWindow").dialog('close');
    window.location.reload();
}

function openUpdateWin(obj){
	var editFlag = $(obj).attr("editFlag");
	var approveId = $(obj).attr("approveId");
	var agentUserArea = $(obj).attr("agentUserArea");
	var agentUserGroup = $(obj).attr("agentUserGroup");
	var agentUserCode = $(obj).attr("agentUserCode");
	var approveUserArea = $(obj).attr("approveUserArea");
	var approveUserGroup = $(obj).attr("approveUserGroup");
	var approveUserCode = $(obj).attr("approveUserCode");
	var functionCode = $(obj).attr("functionCode");
	$("#editPageApproveId").val(approveId);
	$("#newProductFunction option").each(function(){
		var $that = $(this);
		if($that.val()==functionCode){
			$that.attr("selected","selected");
		}
    });
	openDialog("newWindow", "修改審核配置", 1000, 700);
	$("#areaDiv").find("input[type='radio']").each(function(){
	    var $that = $(this);
	    if($that.val()==agentUserArea){
	    $that.attr("checked","checked");
	    checkChange(this,editFlag,agentUserGroup,agentUserCode,approveUserGroup,approveUserCode);
	    }
	});
	//審核人區組
	$("#areaDivShenHe").find("input[type='radio']").each(function(){
	    var $that = $(this);
	    if($that.val()==approveUserArea){
	    $that.attr("checked","checked");
	    checkChangeShenHe(this,editFlag,agentUserGroup,agentUserCode,approveUserGroup,approveUserCode);
	    }
	});
}

function checkChange(obj,editFlag,agentUserGroup,agentUserCode,approveUserGroup,approveUserCode){
	var type = $(obj).attr("name");
	var pDiv = $(obj).parent().parent();
	var checkValue = "";
	var checkText = "";
	var _size = pDiv.find("input[type='radio']").size();//用于判断是否全部都有选择
	var checkSize = 0;
	pDiv.find("input[type='radio']").each(function(){
		if($(this).prop("checked")){
			checkSize++;
			checkValue +=$(this).val()+",";
			checkText +=$(this).next("span").html()+",";
		}
	});
	//选中状态的value值
	if(checkValue!="" && checkValue.length > 0){
		checkValue = checkValue.substring(0,checkValue.length-1);
		checkText = checkText.substring(0,checkText.length-1);
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
	
	if("user"==type){
		$("#chargeCode").val(checkValue);
		$("#agentUserName").val(checkText);
	}
		
	if("user"!=type && checkValue!=""){
		//获取选中的地区
	    var area = getCheckValue("areaDiv");
		setDefaultData(type,checkValue,area,editFlag,agentUserGroup,agentUserCode,approveUserGroup,approveUserCode);
	}
}

function areaGroupchecked(editFlag,agentUserGroup,agentUserCode,approveUserGroup,approveUserCode){
	if(editFlag!=""||editFlag!=undefined){
		$("#pGroupDiv").find("input[type='radio']").each(function(){
			var $that = $(this);
			if($that.val()==agentUserGroup){
			$that.attr("checked","checked");
			//checkChange(this);
			}
		});
		$("#pUserDiv").find("input[type='radio']").each(function(){
			var $that = $(this);
			if($that.val()==agentUserCode){
			$that.attr("checked","checked");
			checkChange(this);
			}
		});	
		$("#pGroupDivShenHe").find("input[type='radio']").each(function(){
			var $that = $(this);
			if($that.val()==approveUserGroup){
			$that.attr("checked","checked");
			//checkChange(this);
			}
		});
		$("#pUserDivShenHe").find("input[type='radio']").each(function(){
			var $that = $(this);
			if($that.val()==approveUserCode){
			$that.attr("checked","checked");
			checkChangeShenHe(this);
			}
		});	
	}
}

function getCheckValue(divId){
	var checkValue = "";
	$("#"+divId).find("input[type='radio']").each(function(){
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

function setDefaultData(type,pType,area,editFlag,agentUserGroup,agentUserCode,approveUserGroup,approveUserCode){
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
					setDefaultData("group","all",area,editFlag,agentUserGroup,agentUserCode,approveUserGroup,approveUserCode);
				}else{
					info =setDivInfo("user",result.data);
					$("#pUserDiv").empty();
					if(result.data!=""&&result.data!=null&&result.data != "undefined" && result.data != undefined){
						$("#pUserDiv").append(info);
					}
					areaGroupchecked(editFlag,agentUserGroup,agentUserCode,approveUserGroup,approveUserCode);
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



function setDivInfo(type,data){
	var divId = type+"Div"
	var info = "<table><tr><td style='width:240px;'><div style='margin-left:10px;height:200px;overflow:auto' id='"+divId+"'>";
	if(data!=""&& data!=null){
		for(var index in data){
			for(var key in data[index]) {
				if(data[index] == '')
					return;
				info += "<span><input type='radio' name='" + type + "' class='radio' value='" + key + "' onclick='checkChange(this)'><span>"
					+ data[index][key] + "</span></input><br/></span>";
			}
		}
	}
	info+="</div></td></tr> </table>";
	return info;
}

//审核人
function checkChangeShenHe(obj,editFlag,agentUserGroup,agentUserCode,approveUserGroup,approveUserCode){
	var type = $(obj).attr("name");
	var pDiv = $(obj).parent().parent();
	var checkValue = "";
	var checkText = "";
	var _size = pDiv.find("input[type='radio']").size();//用于判断是否全部都有选择
	var checkSize = 0;
	pDiv.find("input[type='radio']").each(function(){
		if($(this).prop("checked")){
			checkSize++;
			checkValue +=$(this).val()+",";
			checkText +=$(this).next("span").html()+",";
		}
	});
	//选中状态的value值
	if(checkValue!="" && checkValue.length > 0){
		checkValue = checkValue.substring(0,checkValue.length-1);
		checkText = checkText.substring(0,checkText.length-1);
	}
	//若没有全部选中，全选按钮取消选中状态
	$("input[name='"+type+"'][type='radio'][value='1']").prop("checked",_size==checkSize);
	$("input[name='"+type+"'][type='radio'][value='0']").prop("checked",false);

	//未选中任何区域群组或者选择变化，清空其后的div
	if("areaShenHe"==type){
		$("#pGroupDivShenHe").empty();
	}
	
	if("userShenHe"==type){
		$("#auditorCode").val(checkValue);
		$("#approveUserName").val(checkText);
	}
		
	if("userShenHe"!=type && checkValue!=""){
		//获取选中的地区
	    var area = getCheckValueShenHe("areaDivShenHe");
	    if("areaShenHe"==type){
	    	type="area";
	    }
	    if("groupShenHe"==type){
	    	type="group";
	    }
	    if("userShenHe"==type){
	    	type="user";
	    }
		setDefaultDataShenHe(type,checkValue,area,editFlag,agentUserGroup,agentUserCode,approveUserGroup,approveUserCode);
	}
}

function getCheckValueShenHe(divId){
	var checkValue = "";
	$("#"+divId).find("input[type='radio']").each(function(){
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

function setDefaultDataShenHe(type,pType,area,editFlag,agentUserGroup,agentUserCode,approveUserGroup,approveUserCode){

    if("groupShenHe"==type){
    	type="group";
    }
	$.ajax({
	    url:context_path + "/message/getSendUser",
	    type:"post",
	    data:{"type":type,"pType":pType,"userArea":area},
	    datatype: "json",
	    success: function(result) {
	      if(result.success){
	    	  var info ="";
	    	  if("area"== type){
	    		  info =setDivInfoShenHe("groupShenHe",result.data);
	    		  $("#pGroupDivShenHe").empty();
	    		  $("#pGroupDivShenHe").append(info);
					//查询该区底下所有的用户
					setDefaultDataShenHe("groupShenHe","all",area,editFlag,agentUserGroup,agentUserCode,approveUserGroup,approveUserCode);
				}else{
					info =setDivInfoShenHe("userShenHe",result.data);
					$("#pUserDivShenHe").empty();
					if(result.data!=""&&result.data!=null&&result.data != "undefined" && result.data != undefined){
						$("#pUserDivShenHe").append(info);
					}
					areaGroupchecked(editFlag,agentUserGroup,agentUserCode,approveUserGroup,approveUserCode);
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

function setDivInfoShenHe(type,data){
	var divId = type+"Div"
	var info = "<table><tr><td style='width:240px;'><div style='margin-left:10px;height:200px;overflow:auto' id='"+divId+"'>";
	if(data!=""&& data!=null){
		for(var index in data){
			for(var key in data[index]) {
				if(data[index] == '')
					return;
				info += "<span><input type='radio' name='" + type + "' class='radio' value='" + key + "' onclick='checkChangeShenHe(this)'><span>"
					+ data[index][key] + "</span></input><br/></span>";
			}
		}
	}
	info+="</div></td></tr> </table>";
	return info;
}

//新增
function saveNewAuditConfiguration() {
	 var approveId = $("#editPageApproveId").val();
	 var functionCode = $("#newProductFunction option:selected").val();
	 var functionName = $("#newProductFunction option:selected").text();
	 var agentUserCode = $("#chargeCode").val();
	 var agentUserName = $("#agentUserName").val();
	 var approveUserCode = $("#auditorCode").val();
	 var approveUserName = $("#approveUserName").val();
	 var agentUserArea;
	 var agentUserGroup;
	 var approveUserArea;
	 var approveUserGroup;
	 var agentAreaCheckSize = 0;
	 var agentGroupCheckSize = 0;
	 var agentCheckSize = 0;
	 var approveAreaCheckSize = 0;
	 var approveGroupCheckSize = 0;
	 var approveCheckSize = 0;
	 //經辦人区组
	 $("#areaDiv").find("input[type='radio']").each(function(){
			var $that = $(this);
			if($that.is(":checked")){
				 agentUserArea = $that.val();
				 agentAreaCheckSize++;
			}
		});	
	 $("#pGroupDiv").find("input[type='radio']").each(function(){
			var $that = $(this);
			if($that.is(":checked")){
				 agentUserGroup = $that.val();
				 agentGroupCheckSize++;
			}
		});
	 //審核人區組
	 $("#areaDivShenHe").find("input[type='radio']").each(function(){
			var $that = $(this);
			if($that.is(":checked")){
				approveUserArea = $that.val();
				approveAreaCheckSize++;
			}
		});	
	 $("#pGroupDivShenHe").find("input[type='radio']").each(function(){
			var $that = $(this);
			if($that.is(":checked")){
				approveUserGroup = $that.val();
				approveGroupCheckSize++;
			}
		});
	 
	 //经办人审核人
	 $("#pUserDiv").find("input[type='radio']").each(function(){
			var $that = $(this);
			if($that.is(":checked")){
				agentCheckSize++;
			}
		});
	 $("#pUserDivShenHe").find("input[type='radio']").each(function(){
			var $that = $(this);
			if($that.is(":checked")){
				approveCheckSize++;
			}
		});
	 //判断radio必填
	 if(agentAreaCheckSize == 0){
		 alertInfo("提示","區中心不能為空！",function () {
	        });
	     return ;
	 }
	/* if(agentGroupCheckSize == 0){
		 alertInfo("提示","業務組別不能為空！",function () {
	        });
	     return ;
	 }*/
	 if(agentCheckSize == 0){
		 alertInfo("提示","經辦人不能為空！",function () {
	        });
	     return ;
	 }
	 if(approveAreaCheckSize == 0){
		 alertInfo("提示","區中心不能為空！",function () {
	        });
	     return ;
	 }
	 /*if(approveGroupCheckSize == 0){
		 alertInfo("提示","業務組別不能為空！",function () {
	        });
	     return ;
	 }*/
	 if(approveCheckSize == 0){
		 alertInfo("提示","經辦人不能為空！",function () {
	        });
	     return ;
	 }
	 
	 var data = {"approveId":approveId,"functionCode": functionCode,"functionName":functionName, "agentUserCode": agentUserCode,"agentUserName":agentUserName, "approveUserCode": approveUserCode,
			     "approveUserName":approveUserName,"agentUserArea":agentUserArea,"agentUserGroup":agentUserGroup,"approveUserArea":approveUserArea,"approveUserGroup":approveUserGroup};
	
    if(functionCode.trim()==""){
        alertInfo("提示","功能不能為空！",function () {
        });
        return ;
    }
    if(agentUserCode.trim()==""){
        alertInfo("提示","經辦人員編不能為空！",function () {
        });
        return ;
    }
    if(approveUserCode.trim()==""){
        alertInfo("提示","審核人員編不能為空！",function () {
        });
        return ;
    }
    if(approveUserCode.trim()==agentUserCode.trim()){
        alertInfo("提示","經辦人和審核人不能相同！",function () {
        });
        return ;
    }
    $.ajax({
        url:context_path +"/auditCOnfiguration/saveNewAuditConfiguration",
        type:'post',
        data: data,
        datatype: 'json',
        success:function (data) {
            if(data == "success"){
            	if(approveId!=""){
            	alertInfo("提示","修改成功！",function () {
                    $("#newWindow").dialog('close');
                    window.location.href = "/auditCOnfiguration/viewAuditCOnfiguration";
                });	
            	}else {
                alertInfo("提示","新增成功！",function () {
                    $("#newWindow").dialog('close');
                    window.location.href = "/auditCOnfiguration/viewAuditCOnfiguration";
                });
            	}
            } else {
            	if(data.indexOf("AuditState") != -1){
            	alertInfo_1("提示","該經辦人有問卷正在審核中");	
            	}else{
            		alertInfo_1("提示","該設置已存在，請重新填寫");	
            	}
            }
        },
        error:function (XMLHttpRequest, textStatdus, errorThrown) {
            alert(textStatus + " " + XMLHttpRequest.readyState + " "
                + XMLHttpRequest.status + " " + errorThrown);
        }
    })
}


function deleteWin(obj){
	
	 alertInfoCOPY("提示","您確認刪除嗎？",function deleteSelectRole() {
       var agentUserCode = $(obj).attr("agentUserCode");
       var approveId = $(obj).attr("approveId");
       $.ajax({
           url:context_path +"/auditCOnfiguration/deleteAuditCOnfiguration",
           type:'post',
           data: {"approveId":approveId,"agentUserCode":agentUserCode},
           datatype: 'json',
           success:function (data) {
        	     if(data == "success"){
                     alertInfo("提示","刪除成功！",function () {
                         window.location.href = "/auditCOnfiguration/viewAuditCOnfiguration";
                     });
                 } else {
                     alertInfo_1("提示","該經辦人有問卷正在審核中");
                 }
           },
           error:function (XMLHttpRequest, textStatus, errorThrown) {
               alert(textStatus + " " + XMLHttpRequest.readyState + " "
                   + XMLHttpRequest.status + " " + errorThrown);
           }
       })

   })
  
}


function openDialog(divId, title, width, height) {
    $("#" + divId).dialog({
        title : title,
        width : width,
        height : height,
        modal : true,
        draggable : false,
        //closeOnEscape:false,
        //open:function(event,ui){$(".ui-dialog-titlebar-close").hide();}
        close: function() { window.location.reload(); }
    });
     //$('.ui-dialog-titlebar-close').hide();
}