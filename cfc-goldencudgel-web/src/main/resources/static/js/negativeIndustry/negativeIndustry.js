$(function(){
    findNegativeIndustry(1);
});

function  findNegativeIndustry(curPage) {
    if(curPage!="" && curPage!='undefined'){
        curPage = curPage;
    }else{
        curPage = 1;
    }
    var negativeType = $("#negativeType").val();
    var negativeName = $("#negativeName").val();
    $.ajax({
        url:context_path +"/negativeIndustry/queryNegativeIndustry",
        type:'post',
        data: {"negativeType":negativeType,"negativeName":negativeName,"curPage":curPage},
        datatype: 'html',
        success:function (result) {
            $("#dataListDiv").empty();
            $("#dataListDiv").html(result);
        },
        error:function (XMLHttpRequest, textStatus, errorThrown) {
            alert(textStatus + " " + XMLHttpRequest.readyState + " "
                + XMLHttpRequest.status + " " + errorThrown);
        }
    })

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
function openQueryWin(obj){

    var negativeId = $(obj).attr("negativeId");
    var negativeType = $(obj).attr("negativeType");
    var negativeName = $(obj).attr("negativeName");
    var negativeScore = $(obj).attr("negativeScore");
    $("#negativeIdList").val(negativeId);
    $("#negativeTypeList").val(negativeType);
    $("#negativeNameList").val(negativeName);
    $("#negativeScoreList").val(negativeScore);


    openDialog("changeWindow", "修改負面表列行業", 600, 200);
}

function closeChange() {
    $("#changeWindow").dialog('close');
}

function deleteWin(obj){
	
	 alertInfoCOPY("提示","請確認是否刪除？",function deleteSelectRole() {
        var negativeId = $(obj).attr("negativeId");
        $.ajax({
            url:context_path +"/negativeIndustry/deleteNegativeIndustry",
            type:'post',
            data: {"negativeId":negativeId},
            datatype: 'json',
            success:function (data) {
                alertInfo("提示","刪除成功！",function () {;
                findNegativeIndustry(1);
                });
                // alert("DELETE SUCCESS");
                // findRole(1);

            },
            error:function (XMLHttpRequest, textStatus, errorThrown) {
                alert(textStatus + " " + XMLHttpRequest.readyState + " "
                    + XMLHttpRequest.status + " " + errorThrown);
            }
        })

    })
   
}

function saveNegative() {
    var negativeIdList = $("#negativeIdList").val();
    var negativeTypeList = $("#negativeTypeList").val();
    var negativeNameList = $("#negativeNameList").val();
    var negativeScoreList = $("#negativeScoreList").val();
    
   var data = {"negativeId": negativeIdList, "negativeType": negativeTypeList, "negativeName": negativeNameList, "negativeScore": negativeScoreList};
   $.ajax({
       url:context_path +"/negativeIndustry/saveNegativeIndustry",
       type:'post',
       data: data,
       datatype: 'json',
       success:function (data) {
           alertInfo("提示","儲存成功！",function () {
               closeChange();
               $("#changeWindow").remove();
               window.location.href = "/negativeIndustry/viewNegativeIndustry";
           });
       },
       error:function (XMLHttpRequest, textStatus, errorThrown) {
           alert(textStatus + " " + XMLHttpRequest.readyState + " "
               + XMLHttpRequest.status + " " + errorThrown);
       }
   });

}

function openNewWin(){
    openDialog("newWindow", "新增", 600, 200);
}

function closenew() {
    $("#newWindow").dialog('close');
}
//新增
function saveNewNegative() {
	
	 //var newNegativeType = $("#newNegativeType").val();
	 var negativeId = $("#newNegativeName").val();
	 var newNegativeName = $("#newNegativeName option:selected").text();
	 var newNegativeScore = $("#newNegativeScore").val();
	 var data = {"negativeId": negativeId, "newNegativeName": newNegativeName, "newNegativeScore": newNegativeScore};
	
    if($("#newNegativeName").val().trim()==""){
        alertInfo("提示","負面行業名稱不能為空！",function () {
        });
        return ;
    }
    if($("#newNegativeScore").val().trim()==""){
        alertInfo("提示","中華民國稅務行業標準分類(細類)不能為空！",function () {
        });
        return ;
    }
    $.ajax({
        url:context_path +"/negativeIndustry/saveNewNegative",
        type:'post',
        data: data,
        datatype: 'json',
        success:function (data) {
            if(data == "success"){
                alertInfo("提示","儲存成功！",function () {
                    //$("#newNegativeType").val("");
                    $("#newNegativeName").val("");
                    $("#newNegativeScore").val("");
                    $("#newWindow").dialog('close');
                    window.location.href = "/negativeIndustry/viewNegativeIndustry";
                });
            } else {
                alertInfo_1("提示","該負面行業名稱已經存在，請重新輸入");
            }
        },
        error:function (XMLHttpRequest, textStatdus, errorThrown) {
            alert(textStatus + " " + XMLHttpRequest.readyState + " "
                + XMLHttpRequest.status + " " + errorThrown);
        }
    })
}

function  findPage(curPage) {
	findNegativeIndustry(curPage);
}

//导入
function uploanTarget() {
    $("#uploadfile").dialog({
        title: "導入文檔",
        width: 600,
        height: 150,
        modal: true,
    });
}

function upload(){
	 confirmInfo_1("確認", "是否導入？", function() {
		var formData = new FormData($("#form_upload")[0]);
	    var form = $("#form_upload");
		 $.ajax({
	         url:context_path + "/negativeIndustry/saveUpload",
	         type:"post",
	         data:formData,
	         processData:false,
	         contentType:false,
	         success:function(result){
	        	 alertInfo('提示', result.message);
	        	 findNegativeIndustry(1);
	             colse();
	         },
	         error:function(e){
	        	 alertInfo('提示', e);
	         }
	     });  
	 })
	}

function getFileName(path) {
	  // alert(path);
	  if (!path) {
	    $("#message").text("您還未選取任何文件!");
	    $("#alertMessage").dialog({
	      title: "信息提示框",
	      width: 293,
	      height: 150,
	      modal: true,
	    });
	    return false;
	  }
	  var fileName = path.substring(path.lastIndexOf(".") + 1);
	  if ("xls" != fileName & "xlsx" != fileName) {
	    $("#showMessage").text("您選擇的不是Excel文件！");
	    $("#showMessage").css("color", "red");
	    $("#_upload").attr("disabled", true);
	  } else {
	    $("#showMessage").text("");
	    $("#_upload").removeAttr("disabled");

	  }
	}

//close弹框关闭
function colse() {
    $("#uploadfile").dialog('close');

    setTimeout(function(){
     window.location.reload();
    },1000);
}