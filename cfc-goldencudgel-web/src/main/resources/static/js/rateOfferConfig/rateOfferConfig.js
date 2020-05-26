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
         url:context_path + "/rateOfferConfig/saveUpload",
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
