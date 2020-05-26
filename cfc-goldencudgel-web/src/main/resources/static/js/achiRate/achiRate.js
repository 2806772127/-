//时间插件
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
    
 });

//list页面数据方法
function  findTarget(curPage){

    if(curPage!="" && curPage!='undefined'){
        curPage = curPage;
    }else{
        curPage = 1;
    }
    var employeeIdNum  = $("#employeeIdNum").val();
    var employeeNum  = $("#employeeNum").val();
    var startDate  = $("#startDate").val();
    var endDate  = $("#endDate").val();
   

    $.ajax({
        url:context_path +"/achiRate/queryAchiRateList",
        type:'post',
        data: {"employeeIdNum":employeeIdNum,"employeeNum":employeeNum,"startDate":startDate,"endDate":endDate,"curPage":curPage},
        datatype: 'html',
        success:function (result) {
            $("#achiRateList").empty();
            $("#achiRateList").html(result);
        },
        error:function (XMLHttpRequest, textStatus, errorThrown) {
            alert(textStatus + " " + XMLHttpRequest.readyState + " "
                + XMLHttpRequest.status + " " + errorThrown);
        }
    })

}

function  findPage(curPage) {
    findTarget(curPage);
}


//导入弹框js
function uploanTarget() {
    $("#uploadfile").dialog({
        title: "導入文檔",
        width: 600,
        height: 150,
        modal: true,
    });
}

function colse() {
    $("#uploadfile").dialog('close');

    setTimeout(function(){
     window.location.reload();
    },1000);
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

//导入excle方法
function upload(){
 confirmInfo_1("確認", "是否導入？", function() {
	var formData = new FormData($("#form_upload")[0]);
    var form = $("#form_upload");
	 $.ajax({
         url:context_path + "/achiRate/saveUpload",
         type:"post",
         data:formData,
         processData:false,
         contentType:false,
         success:function(result){
        	 alertInfo('提示', result.message);
        	 colse();
         },
         error:function(e){
        	 alertInfo('提示', e);
         }
     });  
 })
}