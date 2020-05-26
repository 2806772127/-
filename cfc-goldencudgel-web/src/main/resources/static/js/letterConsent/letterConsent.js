$(function(){
    var backFlag = $("#backFlag").val();
    var curPage = 1;
    if ("-1" == backFlag) {
        $("#compilationNo").val(localStorage.getItem("letterCompilationNo"));
        $("#userCode").val(localStorage.getItem("letterUserCode"));
        $("#groupCode").val(localStorage.getItem("letterGroupCode"));
        $("#areaCode").val(localStorage.getItem("letterAreaCode"));
        $("#customerName").val(localStorage.getItem("letterCustomerName"));
        var searchType = localStorage.getItem("letterSearchType");
        $("#searchType").val(searchType);
        $("#account").val(localStorage.getItem("account"));

        var searchTypeInput = $("input[searchType='" + searchType + "']");
        $(".gpic").attr("class", "pic");
        $(searchTypeInput).attr("class", "gpic");
        
        curPage = localStorage.getItem("letterCurPage");
        // 清除所有缓存
        localStorage.clear();
    }
    
	queryLetterConsent(curPage);
});
function queryLetterConsent(curPage){
	var compilationNo =$("#compilationNo").val();
    var createUsers=$("#userCode").val();
    var userGroups=$("#groupCode").val();
    var userAreas=$("#areaCode").val();
    var account=$("#account").val();

	if(curPage=='' ||curPage=='undefined'){
		curPage = 1;
	}
	var customerName = $("#customerName").val();
	var searchType = $("#searchType").val();
	
	$.ajax({
	    url:context_path + "/letterConsent/queryLetterConsents",
	    type:"post",
	    data:{"compilationNo":compilationNo,"customerName":customerName,"searchType":searchType,"curPage":curPage,
            "createUsers":createUsers,"userGroups":userGroups,"userAreas":userAreas,"account":account},
	    datatype: "html",
	    success: function(result) {
	      $("#letterConsent_list").empty();
	      $("#letterConsent_list").html(result);
	    },
	    error: function(XMLHttpRequest, textStatus, errorThrown) {
	      alert(textStatus + " " + XMLHttpRequest.readyState + " "
	              + XMLHttpRequest.status + " " + errorThrown);
	    }
	  });
}
function findPage(curPage){
	queryLetterConsent(curPage);
}
function changeImage(obj,index){
	var result = $(obj).find("input").attr("result");
    if(result=="2"){//标识点击通过图片变换为拒绝
    	$(obj).find("input").attr("class","reject");
    	$(obj).find("input").attr("result","3");
    	$("#approverStatus_"+index).val("3");
    }else{
    	$(obj).find("input").attr("class","approve");
    	$(obj).find("input").attr("result","2");
    	$("#approverStatus_"+index).val("2");
    }
}
function showDialog(flag){
	if(flag=='3'){
		$("#meassage").text("您確定退回嗎？");
		$("#_submitType").val("3");
	}else{
		$("#meassage").text("您確定提交嗎？");
		$("#_submitType").val("2");
	}
	$("#show_dialog").show();
	center($("#show_dialog"));
}

function center(obj){
    var windowWidth = document.documentElement.clientWidth;   
    var windowHeight = document.documentElement.clientHeight;   
    var popupHeight = $(obj).height();   
    var popupWidth = $(obj).width();    
    $(obj).css({   
    "position": "absolute",   
    "top": (windowHeight-popupHeight)/2+$(document).scrollTop(),   
    "left": (windowWidth-popupWidth)/2   
    });
}

function closeDialog(divId){
	$("#"+divId).hide();
}

/**
function approveOrReject(){
	var data = $("#letterConsent").serialize();
	data +="&flag="+$("#_submitType").val(); 
	$.ajax({
			url:context_path + "/letterConsent/auditLetterConsents",
		    type:"post",
		    data:data,
		    datatype: "json",
		    success: function(result) {
		    	
		    },
		    error: function(XMLHttpRequest, textStatus, errorThrown) {
		      alert(textStatus + " " + XMLHttpRequest.readyState + " "
		              + XMLHttpRequest.status + " " + errorThrown);
		    }	
	});
}
**/
function changeSearchType(obj,value){
	$("#searchType").val(value);
	$(".gpic").attr("class","pic");
	$(obj).attr("class","gpic");
	queryLetterConsent();
}
function viewDetail(obj){

    // 缓存查询条件
    localStorage.setItem("letterCompilationNo", $("#compilationNo").val());
    localStorage.setItem("letterUserCode", $("#userCode").val());
    localStorage.setItem("letterGroupCode", $("#groupCode").val());
    localStorage.setItem("letterAreaCode", $("#areaCode").val());
    localStorage.setItem("letterCustomerName", $("#customerName").val());
    localStorage.setItem("letterSearchType", $("#searchType").val());
    localStorage.setItem("letterCurPage", $("#page_select").val());
    localStorage.setItem("account", $("#account").val());
	var letterId = $(obj).attr("letterId");
	if(letterId==""||letterId=="undefined"){
		alertInfo("提示","案件ID为空");
		return;
	}
	window.location.href = context_path + "/letterConsent/queryLetterConsentDetail?letterId="+letterId;
}
function goBack(){
	window.location.href = context_path + "/letterConsent/viewLetterConsents";
}

function showImg(obj){
//	var compliNo = $(obj).attr("compliNo");
////	var trandId = $(obj).attr("tranId");
//	var letterType = $(obj).attr("letterType");
	var attchId = $(obj).attr("attchId");
	var letterDetailId = $(obj).attr("letterDetailId");
	$("#photo_index").attr("src", "");
	 $.ajax({
	        url :context_path + "/letterConsent/showImg",
	        type: "post",
	        data:{"attachId":attchId,"letterDetailId":letterDetailId},
	        datatype : "json",
	        success :function (result) {
	          if(result.success){
		            $("#info").empty();
		            var data = result.data;
		            var info = "";

		            if(data!="") {
                        var customerId = '';
                        var customerName = '';
                        switch (data.letterType) {
                            case "1" :
                                customerName = '授信戶名稱：';
                                customerId = '授信戶ID：';
                                break;
                            case '2' :
                                customerName = '負責人名稱：';
                                customerId = '負責人ID：';
                                break;
                            case '3' :
                                customerName = '負責人配偶名稱：';
                                customerId = '負責人配偶ID：';
                                break;
                            case '4' :
                                customerName = '保證人名稱：';
                                customerId = '保證人/關係戶ID：';
                                break;
                            default :
                                break;
                        }
                        if(customerId == '') return;
                        info = "<table class='tb01 mgt'><tr><td class='tdback' style='width:20%'>"+customerName+"</td><td style='width:30%'>" +
                            (data.customerName == null ? "" : data.customerName) +
                            "</td><td class='tdback' style='width:20%'>"+customerId+"</td><td style='width:30%'>" +
                            (data.customerId == null ? "" : data.customerId) +
                            "</td></tr><tr><td class='tdback'>主管：</td><td>" +
                            (data.approverUser == null ? "" : data.approverUser) +
                            "</td><td class='tdback'>經辦：</td><td>" +
                            (data.createUser == null ? "" : data.createUser) +
                            "</td></tr><tr></td><td class='tdback'>對保/核章：</td><td>" +
                            (data.createUser == null ? "" : data.createUser) +
                            "</td><td class='tdback'></td><td></td></tr></table>"
                    }
		            openDialog("show_img","聯徵同意書",630,620);
		            showPhoto(result.imgUrl);
		            $("#info").append(info);
	          }else{
	        	  alertInfo("提示",result.message);
	          }
	            
	        },error: function(XMLHttpRequest, textStatus, errorThrown) {
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

function showPhoto(photoSrc) {
    $("#photo_index").attr("src",photoSrc);
    //图片居中显示
    var box_width = 525; //图片盒子宽度
    var box_height = 420;//图片高度高度
    var initial_width = $(".auto-img-center img").width();//初始图片宽度
    var initial_height = $(".auto-img-center img").height();//初始图片高度
    if (initial_width > initial_height) {
        $(".auto-img-center img").css("width", box_width);
        var last_imgHeight = $(".auto-img-center img").height();
        $(".auto-img-center img").css("margin-top", "1cm");
    } else {
        $(".auto-img-center img").css("height", box_height);
        var last_imgWidth = $(".auto-img-center img").width();
        $(".auto-img-center img").css("margin-left", "1cm");
    }
    //图片拖拽
    var $div_img = $(".mask-layer-imgbox p");
    //绑定鼠标左键按住事件
    $div_img.bind("mousedown", function (event) {
        event.preventDefault && event.preventDefault(); //去掉图片拖动响应
        //获取需要拖动节点的坐标
        var offset_x = $(this)[0].offsetLeft;//x坐标
        var offset_y = $(this)[0].offsetTop;//y坐标
        //获取当前鼠标的坐标
        var mouse_x = event.pageX;
        var mouse_y = event.pageY;
        //绑定拖动事件
        //由于拖动时，可能鼠标会移出元素，所以应该使用全局（document）元素
        $(".mask-layer-imgbox").bind("mousemove", function (ev) {
            // 计算鼠标移动了的位置
            var _x = ev.pageX - mouse_x;
            var _y = ev.pageY - mouse_y;
            //设置移动后的元素坐标
            var now_x = (offset_x + _x) + "px";
            var now_y = (offset_y + _y) + "px";
            //改变目标元素的位置
            $div_img.css({
                top: now_y,
                left: now_x
            });
        });
    });
    //当鼠标左键松开，接触事件绑定
    $(".mask-layer-imgbox").bind("mouseup", function () {
        $(this).unbind("mousemove");
    });

    //缩放
    var zoom_n = 1;
    $(".mask-out").click(function () {
        zoom_n += 0.1;
        $(".mask-layer-imgbox img").css({
            "transform": "scale(" + zoom_n + ")",
            "-moz-transform": "scale(" + zoom_n + ")",
            "-ms-transform": "scale(" + zoom_n + ")",
            "-o-transform": "scale(" + zoom_n + ")",
            "-webkit-": "scale(" + zoom_n + ")"
        });
    });
    $(".mask-in").click(function () {
        zoom_n -= 0.1;
        console.log(zoom_n)
        if (zoom_n <= 0.1) {
            zoom_n = 0.1;
            $(".mask-layer-imgbox img").css({
                "transform": "scale(.1)",
                "-moz-transform": "scale(.1)",
                "-ms-transform": "scale(.1)",
                "-o-transform": "scale(.1)",
                "-webkit-transform": "scale(.1)"
            });
        } else {
            $(".mask-layer-imgbox img").css({
                "transform": "scale(" + zoom_n + ")",
                "-moz-transform": "scale(" + zoom_n + ")",
                "-ms-transform": "scale(" + zoom_n + ")",
                "-o-transform": "scale(" + zoom_n + ")",
                "-webkit-transform": "scale(" + zoom_n + ")"
            });
        }
    });
}



function getGroupList() {
    $.ajax({
        url: context_path + "/missionStroke/getGroupList",
        data: {"areaCode" : $("#areaCode").val()},
        type : "POST",
        dataType : "json",
        success : function (result) {
            var groupSelect = document.getElementById("groupCode");
            groupSelect.options.length=0;
            groupSelect.add(new Option("全部",""));
            var groupMap = result[0];
            for(var code in groupMap) {
                var option = document.createElement("option");
                option.setAttribute("value", code);
                option.text= groupMap[code];
                groupSelect.appendChild(option);
            }

            var userSelect = document.getElementById("userCode");
            userSelect.options.length=0;
            userSelect.add(new Option("全部",""));
            var userMap = result[1];
            for (var code in userMap) {
                var option = document.createElement("option");
                option.setAttribute("value", code);
                option.text = userMap[code];
                userSelect.appendChild(option);
            }
         //   queryLetterConsent(1);
        },
        fail : function () {
        }
    });
}




function getUserList() {
    $.ajax({
        url: context_path + "/missionStroke/getUserList",
        data: {
            "areaCode" : $("#areaCode").val(),
            "groupCode" : $("#groupCode").val()
        },
        type : "POST",
        dataType : "json",
        success : function (result) {
            var userSelect = document.getElementById("userCode");
            userSelect.options.length=0;
            userSelect.add(new Option("全部",""));
            for(var code in result) {
                var option = document.createElement("option");
                option.setAttribute("value",code);
                option.text= result[code];
                userSelect.appendChild(option);
            };
           // queryLetterConsent(1);
        },
        fail : function () {

        }
    });
}

function backPage() {
    window.location.href = context_path + "/letterConsent/viewLetterConsents?backFlag=-1";
}