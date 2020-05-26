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


function upload(){
 confirmInfo_1("確認", "是否導入？", function() {
	var formData = new FormData($("#form_upload")[0]);
    var form = $("#form_upload");
	 $.ajax({
         url:context_path + "/holiday/saveUpload",
         type:"post",
         data:formData,
         processData:false,
         contentType:false,
         success:function(result){
        	 alertInfo('提示', result.message);
             queryhoilday(1);
             colse();
         },
         error:function(e){
        	 alertInfo('提示', e);
         }
     });  
 })
}

function  queryhoilday(curPage) {


    if (curPage=="" && curPage=='undefined') {
            curPage = 1;

    }else  {
        curPage=curPage;
    }

    var years = $("#years").val();
    var holiday = $("#holiday").val();


    $.ajax({
        url: context_path + "/holiday/HolidayList",
        type: "post",
        data: {
             "curPage": curPage,
            "years":years,
            "holiday":holiday
        },
        datatype: "html",
        success: function(result) {
            $("#holiday_list").empty();
            $("#holiday_list").html(result);
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            alert(textStatus + " " + XMLHttpRequest.readyState + " "
                + XMLHttpRequest.status + " " + errorThrown);
        }
    })
}

$(function(){
    queryhoilday(1);
});

function  findPage(curPage) {
    queryhoilday(curPage);
}

function uploanTarget() {
    $("#uploadfile").dialog({
        title: "導入文檔",
        width: 600,
        height: 150,
        modal: true,
    });
}