$(function(){
	//findTarget();
});

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
      if ("xls" != fileName & "xlsx" != fileName) {
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

function  findTarget(curPage){

    if(curPage!="" && curPage!='undefined'){
        curPage = curPage;
    }else{
        curPage = 1;
    }
    var dictId  = $("#dictId option:selected").val();
    var areaId  = $("#areaId option:selected").val();
    var groupId  = $("#groupId option:selected").val();
    var userId  = $("#userId option:selected").val();
    var monId  = $("#monId option:selected").val();
    var targetId  = $("#targetId option:selected").val();

    $.ajax({
        url:context_path +"/indexTarget/queryIndexTarget",
        type:'post',
        data: {"dictId":dictId,"areaId":areaId,"groupId":groupId,"userId":userId,"mon":monId,
            "targetId":targetId,"curPage":curPage},
        datatype: 'html',
        success:function (result) {
            $("#dataLists").empty();
            $("#dataLists").html(result);
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
		        	 alertInfo('提示', result.message);
                     if (!!window.ActiveXObject || "ActiveXObject" in window){
                         $("#_upload").attr('disabled',"true");
                         colse();
                         setTimeout(function(){
                             window.location.reload();
                         },1000);
                     }else {
                         $("#documentUpload").val("");
                     }

		             $("#_upload").attr('disabled',"true");
		             colse();



		         },
		         error:function(e){
		        	 alertInfo('提示', e);
		         }
		     });  
		 })
}

function getGroupList() {
    $.ajax({
        url: context_path + "/indexTarget/getGroupList",
        data: {
            "areaId" : $("#areaId").val()
        },
        type : "POST",
        dataType : "json",
        success : function (result) {
            var groupSelect = document.getElementById("groupId");
            groupSelect.options.length = 0;
            groupSelect.add(new Option("全部", "allgroup"));
            var groupMap = result[0];
            for (var code in groupMap) {
                var option = document.createElement("option");
                option.setAttribute("value", code);
                option.text = groupMap[code];
                groupSelect.appendChild(option);
            }
            
            var userSelect = document.getElementById("userId");
            userSelect.options.length = 0;
            userSelect.add(new Option("全部", "alluser"));
            var userMap = result[1];
            for (var code in userMap) {
                var option = document.createElement("option");
                option.setAttribute("value", code);
                option.text = userMap[code];
                userSelect.appendChild(option);
            }
            //findTarget();
        },
        fail : function () {
        }
    })
}

function getUserList() {
    $.ajax({
        url: context_path + "/indexTarget/getUserList",
        data: {
            "areaId" : $("#areaId").val(),
            "groupId" : $("#groupId").val()
        },
        type : "POST",
        dataType : "json",
        success : function (result) {
            var userSelect = document.getElementById("userId");
            userSelect.options.length = 0;
            userSelect.add(new Option("全部","alluser"));
            for(var code in result) {
                var option = document.createElement("option");
                option.setAttribute("value",code);
                option.text= result[code];
                userSelect.appendChild(option);
            };
          //  findTarget();
        },
        fail : function () {

        }
    });
}
