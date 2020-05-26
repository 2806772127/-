$(function(){
	queryProductManagement(1);
    $("input[name='startDate']").datetimepicker({
        timeFormat: "HH:mm",
        dateFormat: "yy/mm/dd"
    });
    
   /* $("input[name='ExpectedTime']").datetimepicker({
        timeFormat: "HH:mm",
        dateFormat: "yy/mm/dd"
    });*/
    

    
});

function  queryProductManagement(curPage) {
	if(curPage!="" && curPage!='undefined'){
        curPage = curPage;
    }else{
        curPage = 1;
    }
    var productName = $("#productName").val();
    var setItem = $("#setItem").val();
    var checkState = $("#checkState").val();
    
    $.ajax({
        url: context_path + "/product/queryProductManagement",
        type: "post",
        data: {
            "productName": productName,"curPage": curPage,"setItem":setItem,"checkState":checkState
        },
        datatype: "html",
        success: function(result) {
            $("#productManagement_list").empty();
            $("#productManagement_list").html(result);
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            alert(textStatus + " " + XMLHttpRequest.readyState + " "
                + XMLHttpRequest.status + " " + errorThrown);
        }
    })
}

function  findPage(curPage) {
	queryProductManagement(curPage);
}

//查看配置
function viewProductDetail(obj){
	var productVersion = $(obj).attr("productVersion");
	var prodNameEncodeUrl = $(obj).attr("prodName");
	var prodName= encodeURI(encodeURI(prodNameEncodeUrl));
	var questionnaireType = $(obj).attr("questionnaireType");
	var estimatedLaunchTime = $(obj).attr("estimatedLaunchTime");
	var editFlag = $(obj).attr("editFlag");
	var prodId = $(obj).attr("prodId");
    

	window.location.href = context_path + "/product/viewProductDetail?productVersion="+productVersion+"&prodName="+prodName
	                                    +"&questionnaireType="+questionnaireType+"&estimatedLaunchTime="+estimatedLaunchTime+"&editFlag="+editFlag+"&prodId="+prodId;
}

//修改配置
function EditProductDetail(obj){
	var productVersion = $(obj).attr("productVersion");
	var prodNameEncodeUrl = $(obj).attr("prodName");
	var prodName= encodeURI(encodeURI(prodNameEncodeUrl));
	var questionnaireType = $(obj).attr("questionnaireType");
	var estimatedLaunchTime = $(obj).attr("estimatedLaunchTime");
	var prodId = $(obj).attr("prodId");
	var editFlag = $(obj).attr("editFlag");
    

	window.location.href = context_path + "/product/EditProductDetail?productVersion="+productVersion+"&prodName="+prodName
	                                    +"&questionnaireType="+questionnaireType+"&estimatedLaunchTime="+estimatedLaunchTime+"&prodId="+prodId+"&editFlag="+editFlag;
}

//审核人审核
function viewAdudit(obj){
	var productVersion = $(obj).attr("productVersion");
	//var prodName = $(obj).attr("prodName");
	var prodNameEncodeUrl = $(obj).attr("prodName");
	var prodName= encodeURI(encodeURI(prodNameEncodeUrl));
	var questionnaireType = $(obj).attr("questionnaireType");
	var estimatedLaunchTime = $(obj).attr("estimatedLaunchTime");
	var prodId = $(obj).attr("prodId");
	var editFlag = $(obj).attr("editFlag");
	var commitName = $(obj).attr("commitName");
    

	window.location.href = context_path + "/product/viewAdudit?productVersion="+productVersion+"&prodName="+prodName
	                                    +"&questionnaireType="+questionnaireType+"&estimatedLaunchTime="+estimatedLaunchTime+"&editFlag="+editFlag+"&prodId="+prodId+"&commitName="+commitName;
}

//审核人查看
function viewAduitDetail(obj){
	var productVersion = $(obj).attr("productVersion");
	//var prodName = $(obj).attr("prodName");
	var prodNameEncodeUrl = $(obj).attr("prodName");
	var prodName= encodeURI(encodeURI(prodNameEncodeUrl));
	var questionnaireType = $(obj).attr("questionnaireType");
	var estimatedLaunchTime = $(obj).attr("estimatedLaunchTime");
	var prodId = $(obj).attr("prodId");

	window.location.href = context_path + "/product/viewAduitDetail?productVersion="+productVersion+"&prodName="+prodName
	                                    +"&questionnaireType="+questionnaireType+"&estimatedLaunchTime="+estimatedLaunchTime+"&prodId="+prodId;
}
//更新上线时间弹框js
function updateOnlineTime(obj){
	   var prodId = $(obj).attr("prodId");
	   openDialog("updateOnlineTimeId", "更新上線日期", 350, 200);
	   $("input[name='ExpectedTime']").datetimepicker({
	        timeFormat: "HH:mm",
	        dateFormat: "yy/mm/dd"
	    });
	   $("#product_id").val(prodId);
}

//业务员审核结果js
function checkResultFlag(obj){
	   var auditRemark = $(obj).attr("auditRemark");
	   var checkResult = $(obj).attr("checkResult");
	   if(checkResult!=null){
		   openDialog("show_dialog", "審核備註", 500, 300);
		   $(".show_info").html(auditRemark);	
	    }
	  
}

function closeDialog(){
	$("#show_dialog").dialog("close");
}

//关闭更新上线时间弹框
function cancell(){
	$("#updateOnlineTimeId").dialog('close');
}

function comfirmm(obj){
	//var prodName = $("input[name='ExpectedTime']").val();
	var updateTime = $('input[name="ExpectedTime"]').val();
	var productId = $("#product_id").val();
	window.location.href = context_path + "/product/updateOnlineTime?updateTime="+updateTime+"&productId="+productId;
}