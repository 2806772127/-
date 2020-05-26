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
    var backFlag = $("#backFlag").val();
    var curPage = 1;
    if ("-1" == backFlag) {
        // 缓存查询条件
        $("#userCode").val(localStorage.getItem("measureUserCode"));
        $("#groupCode").val(localStorage.getItem("measureGroupCode"));
        $("#areaCode").val(localStorage.getItem("measureAreaCode"));
        $("#compilationNo").val(localStorage.getItem("measureCompilationNo"));
        $("#companyName").val(localStorage.getItem("measureCompanyName"));
        $("input[name='startDate']").val(localStorage.getItem("measureStartDate"));
        $("input[name='endDate']").val(localStorage.getItem("measureEndDate"));
        // 發送企業經營報告書
        $("#opcount").val(localStorage.getItem("measureOpcount"));
        // 是否有額度問卷
        $("#qucount").val(localStorage.getItem("measureQucount"));
        $("#productName").val(localStorage.getItem("productName"));
        curPage = localStorage.getItem("measureCurPage");
        // 清除所有缓存
        localStorage.clear();
    }
   
    
    queryMeasureWords(curPage);
    

    //设置页面禁止右击
    $(document).bind("contextmenu",function(e){
        return false;
    });
    $(document).bind("selectstart", function () { return false; });

});
/**類型變化選擇**/
/*function changeType(value){
    $("#newCus").attr("class",value != "01" ? "pic" : "gpic");
    $("#oldCus").attr("class",value == "01" ? "pic" : "gpic");
    $("#comCustomerType").val(value);

 
}
function changeTypes(value){
    $("#newCuss").attr("class",value != "01" ? "pic" : "gpic");
    $("#oldCuss").attr("class",value == "01" ? "pic" : "gpic");
    $("#comCustomerTypes").val(value);


}*/

function queryMeasureWords(curPage){
	if(curPage!="" && curPage!='undefined'){
		curPage = curPage;
	}else{
		curPage = 1;
	}
	var qucount=  $("#qucount").val();
    var opcount=  $("#opcount").val();
    var createUsers=$("#userCode").val();
    var userGroups=$("#groupCode").val();
    var userAreas=$("#areaCode").val();
	var compilationNo = $("#compilationNo").val();
	var companyName = $("#companyName").val();
	var startDate =$("input[name='startDate']").val();
	var endDate =$("input[name='endDate']").val();
	var productName = $("#productName").val();
	var productName = $("#productName").val();
	$.ajax({
	    url:context_path + "/measureWord/queryMeasureWords",
	    type:"post",
	    data:{"compilationNo":compilationNo,"companyName":companyName,
	      "startDate":startDate,"endDate":endDate,"curPage":curPage,
            "createUsers":createUsers,"userGroups":userGroups,"userAreas":userAreas
        ,"opcount":opcount,"qucount":qucount,"productName":productName},
	    datatype: "html",
	    success: function(result) {
	      $("#measureWords_list").empty();
	      $("#measureWords_list").html(result);
	      if(productName=='P0002'){
	  		$(".wordTimeList").html("測算時間");
	  		$(".resultList").hide();
	  		$(".wordList").hide();
	  		$(".quotaList").hide();
	  		$(".sendList").hide();
	  		$(".CalculatingList").show();
	  	}
	    },
	    error: function(XMLHttpRequest, textStatus, errorThrown) {
	      alert(textStatus + " " + XMLHttpRequest.readyState + " "
	              + XMLHttpRequest.status + " " + errorThrown);
	    }
	  });
}
//刷选条件查询
function changeProductName(that){
	var productName = $("#productName").val();
		$("#opcount").toggle();
		$("#opcountTd").toggle();
		$("#qucountTd").toggle();
		$("#qucount").toggle();
		$(".wordList").toggle();
		$(".quotaList").toggle();
		$(".sendList").toggle();
		$(".resultList").toggle();
		if(productName=='P0002'){
			$(".wordTime").html("測算時間：");
			$(".wordTimeList").html("測算時間");
			$(".CalculatingList").show();
		}else{
			$(".wordTime").html("測字時間：");
			$(".wordTimeList").html("測字時間");
			$(".CalculatingList").hide();
		}
    queryMeasureWords();
}

function findPage(curPage){
	queryMeasureWords(curPage);
}
function viewDetail(obj){
    
    // 缓存查询条件
    localStorage.setItem("measureUserCode", $("#userCode").val());
    localStorage.setItem("measureGroupCode", $("#groupCode").val());
    localStorage.setItem("measureAreaCode", $("#areaCode").val());
    localStorage.setItem("measureCompilationNo", $("#compilationNo").val());
    localStorage.setItem("measureCompanyName", $("#companyName").val());
    localStorage.setItem("measureStartDate", $("input[name='startDate']").val());
    localStorage.setItem("measureEndDate", $("input[name='endDate']").val());
    localStorage.setItem("measureCurPage", $("#page_select").val());
    // 發送企業經營報告書
    localStorage.setItem("measureOpcount", $("#opcount").val());
    // 是否有額度問卷
    localStorage.setItem("measureQucount", $("#qucount").val());
    localStorage.setItem("productName", $("#productName").val());//产品名称
    localStorage.setItem("selfPage", "Y");
    
	var measureId =  $(obj).attr("measureId");
	var produceName =  $(obj).attr("produceName");
	window.location.href = 
		context_path + "/measureWord/viewMeasureWordDetail?measureId="+measureId+"&produceName="+produceName;
}
function viewMeasureResult(obj){  
	var result =  $(obj).attr("result");
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

function showWordDetail(){
	closeDialog("show_dialog");
	$("#show_dialog_word").show();
	center($("#show_dialog_word"));
}

function gotoSearchList(){
	window.location.href = context_path +"/measureWord/viewMeasureWords";
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
            //queryMeasureWords(1);
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
           // queryMeasureWords(1);
        },
        fail : function () {

        }
    });
}

function backPage() {
    if (localStorage.getItem("selfPage") == "Y") {
        window.location.href = context_path + "/measureWord/viewMeasureWords?backFlag=-1";
    } else {
        history.go(-1);
    }
}

/**
 * 查看联征数据
 */
function showJcicDetail(zixId) {
    if(!checkImgByUrl()) {
        alertInfo_1("提示","無權限查看");
        return;
    }
    if(!checkTrustedSites()) {
        alertInfo_1("提示","未添加受信任站點，不可查看");
        return;
    }
    $.ajax({
        url: context_path + "/measureWord/showJcicDetail",
        data: {
            "zixId" : zixId
        },
        type : "POST",
        dataType : "HTML",
        async : false,
        success : function (result) {
            $("#jcic_content").empty();
            $("#jcic_content").html(result);
            openDialog("jcic_detail","聯徵數據", document.body.clientWidth , document.body.clientHeight );
            //设置显示内容的高度
            $("#jcic_content").css("height",document.body.clientHeight-110);
            //设置X关闭按钮的位置大小
            $(".ui-button-icon-only").css({"right":"15px","width":"25px","height":"25px","background-size":"100% 100%"});
        },
        fail : function () {

        }
    });
	var iframeWidth = 0;
    $(".jcic").each(function (obj) {
        $(this).load(function(){
            $(this).attr("height",$(this).get(0).contentDocument.body.scrollHeight+15);
			iframeWidth = ($(this).get(0).contentDocument.body.scrollWidth+15) > iframeWidth ? ($(this).get(0).contentDocument.body.scrollWidth+15) : iframeWidth;
			$(".jcic").attr("width",iframeWidth);
            $(this).get(0).contentDocument.body.style.backgroundImage = "url("+ $("#imgUrl").val() +")";
            //設置浮水印
            // $("#jcic_detail").find(".mask_div").remove();
            // var waterMark = watermark({
            //     watermark_txt: "",
            //     watermark_show_x:$("#jcic_detail").get(0).scrollWidth,
            //     watermark_show_y:$("#jcic_detail").get(0).scrollHeight,
            //     watermark_backGroupImage: $("#imgUrl").val(),
            //     watermark_x_space:0,
            //     watermark_y_space:0,
            //     watermark_width:300,
            //     watermark_height:200,
            //     watermark_alpha:50,
            //     watermark_zIndex:-1,
            //     watermark_angle:0});
            // document.getElementById("jcic_detail").appendChild(waterMark);
        });
    });
	
}

function closeJcic() {
    $("#jcic_detail").dialog('close');
}

function checkImgByUrl(){
    var flag = false;
    $.ajax({
        url: context_path + "/measureWord/checkImgByUrl",
        type : "POST",
        dataType : "JSON",
        async : false,
        success : function (result) {
            flag =  result.returnCode;
            $("#imgUrl").attr("value",result.returnMessage);
        },
        fail : function () {

        }
    });
    return flag;
}

function watermark(settings) {
    //默认设置
    var defaultSettings = {
        watermark_txt: "",//显示的文字
        watermark_x: 20, //水印起始位置x轴坐标
        watermark_y: 20, //水印起始位置Y轴坐标
        watermark_rows: 20, //水印行数
        watermark_cols: 20, //水印列数
        watermark_x_space: 100, //水印x轴间隔
        watermark_y_space: 50, //水印y轴间隔
        watermark_color: '#DDDDDD', //水印字体颜色
        watermark_alpha: 10, //水印透明度
        watermark_fontsize: '18px', //水印字体大小
        watermark_font: '微软雅黑', //水印字体
        watermark_width: 120, //水印宽度
        watermark_height: 80, //水印长度
        watermark_angle: 0,//水印倾斜度数
        watermark_backGroupImage : '',//背景图片url
        watermark_show_x : Math.max(document.body.scrollWidth, document.body.clientWidth),//浮水印覆盖宽度
        watermark_show_y :Math.max(document.body.scrollHeight, document.body.clientHeight),//浮水印覆盖高度
        watermark_zIndex : 9999//显示层数
    };
    //采用配置项替换默认值，作用类似jquery.extend
    if (arguments.length === 1 && typeof arguments[0] === "object") {
        var src = arguments[0] || {};
        for (key in src) {
            if (src[key] && defaultSettings[key] && src[key] === defaultSettings[key])
                continue;
            else if (src[key])
                defaultSettings[key] = src[key];
        }
    }
    var oTemp = document.createDocumentFragment();
    //获取页面最大宽度
    var page_width = defaultSettings.watermark_show_x;
    //获取页面最大长度
    var page_height = defaultSettings.watermark_show_y;
    //如果将水印列数设置为0，或水印列数设置过大，超过页面最大宽度，则重新计算水印列数和水印x轴间隔
    if (defaultSettings.watermark_cols == 0 || (parseInt(defaultSettings.watermark_x + defaultSettings.watermark_width * defaultSettings.watermark_cols + defaultSettings.watermark_x_space * (defaultSettings.watermark_cols - 1)) > page_width)) {
        defaultSettings.watermark_cols = parseInt((page_width - defaultSettings.watermark_x + defaultSettings.watermark_x_space) / (defaultSettings.watermark_width + defaultSettings.watermark_x_space));
        defaultSettings.watermark_x_space = parseInt((page_width - defaultSettings.watermark_x - defaultSettings.watermark_width * defaultSettings.watermark_cols) / (defaultSettings.watermark_cols - 1));
    }
    //如果将水印行数设置为0，或水印行数设置过大，超过页面最大长度，则重新计算水印行数和水印y轴间隔
    if (defaultSettings.watermark_rows == 0 || (parseInt(defaultSettings.watermark_y + defaultSettings.watermark_height * defaultSettings.watermark_rows + defaultSettings.watermark_y_space * (defaultSettings.watermark_rows - 1)) > page_height)) {
        defaultSettings.watermark_rows = parseInt((defaultSettings.watermark_y_space + page_height - defaultSettings.watermark_y) / (defaultSettings.watermark_height + defaultSettings.watermark_y_space));
        defaultSettings.watermark_y_space = parseInt(((page_height - defaultSettings.watermark_y) - defaultSettings.watermark_height * defaultSettings.watermark_rows) / (defaultSettings.watermark_rows - 1));
    }
    var x;
    var y;
    for (var i = 0; i < defaultSettings.watermark_rows; i++) {
        y = defaultSettings.watermark_y + (defaultSettings.watermark_y_space + defaultSettings.watermark_height) * i;
        for (var j = 0; j < defaultSettings.watermark_cols; j++) {
            x = defaultSettings.watermark_x + (defaultSettings.watermark_width + defaultSettings.watermark_x_space) * j;
            var mask_div = document.createElement('div');
            mask_div.id = 'mask_div' + i + j;
            mask_div.setAttribute("class","mask_div");
            mask_div.appendChild(document.createTextNode(defaultSettings.watermark_txt));
            //设置水印div倾斜显示
            mask_div.style.webkitTransform = "rotate(-" + defaultSettings.watermark_angle + "deg)";
            mask_div.style.MozTransform = "rotate(-" + defaultSettings.watermark_angle + "deg)";
            mask_div.style.msTransform = "rotate(-" + defaultSettings.watermark_angle + "deg)";
            mask_div.style.OTransform = "rotate(-" + defaultSettings.watermark_angle + "deg)";
            mask_div.style.transform = "rotate(-" + defaultSettings.watermark_angle + "deg)";
            mask_div.style.visibility = "";
            mask_div.style.position = "absolute";
            mask_div.style.left = x + 'px';
            mask_div.style.top = y + 'px';
            mask_div.style.overflow = "hidden";
            mask_div.style.zIndex = defaultSettings.watermark_zIndex;
            mask_div.style.pointerEvents='none';//让水印不遮挡页面的点击事件
            mask_div.style.wordBreak = 'break-all';//设置文字换行
            //mask_div.style.border="solid #eee 1px";
            mask_div.style.opacity = defaultSettings.watermark_alpha;
            //透明度
            mask_div.style.filter = "progid:DXImagetransform.Microsoft.Matrix(M11=0.707,M12=-0.707,M21=0.707,M22=0.707,SizingMethod='auto expand') alpha(opacity=" + defaultSettings.watermark_alpha.toString() + ") ";//ie
            mask_div.style.opacity = defaultSettings.watermark_alpha.toString();//google
            //设置背景图片
            mask_div.style.backgroundImage = "url("+ defaultSettings.watermark_backGroupImage +")";
            mask_div.style.backgroundSize = "100% 100%";
            mask_div.style.backgroundRepeat = "no-repeat";

            mask_div.style.fontSize = defaultSettings.watermark_fontsize;
            mask_div.style.fontFamily = defaultSettings.watermark_font;
            mask_div.style.color = defaultSettings.watermark_color;
            //设置浮水印居中
            mask_div.style.textAlign = "center";
            mask_div.style.lineHeight = defaultSettings.watermark_height + 'px';
            mask_div.style.width = defaultSettings.watermark_width + 'px';
            mask_div.style.height = defaultSettings.watermark_height + 'px';
            mask_div.style.display = "block";
            mask_div.style.opacity = "0.5";
            oTemp.appendChild(mask_div);
        };
    };

    return oTemp;
    //document.body.appendChild(oTemp);
}

function checkTrustedSites(){
    var flag = false;
    var hostname = $("#trustedSites").val();//授信任站点
    var WshShell=null;
    //如果没有ActiveXObject直接返回true
    if(!window.ActiveXObject) {
        return true;
    }
    WshShell = new ActiveXObject("WScript.Shell");
    var str = [];
    for(var i = 1;i < 2000;i++){ //循环注册表
        try{
            str[i] = WshShell.RegRead("HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings\\ZoneMap\\Ranges\\Range" + i + "\\:Range");
        }catch(e){
        }
    }
    var count = false;
    for(var i = 1;i < str.length;i++){  //循环与ip比较
        if(str[i] == undefined){
            continue;
        }else{
            if(str[i] == hostname){
                flag = true;
                break;
            }
        }
    }
    return flag;
}

/**
 * @description 測字館重測
 * @returns
 */
function resendMeasure(obj) {
    var compilationNo =  $(obj).attr("compilationNo");
    var compilationName =  $(obj).attr("compilationName");
    var trandId =  $(obj).attr("trandId");
    var measureId =  $(obj).attr("measureId");
    var userCode =  $(obj).attr("userCode");
    
    confirmInfo_1("確認", "是否重測展業金信評", function() {
        $.ajax({
            url : context_path + "/measureWord/resendMeasure",
            type : "post",
            data : { "compilationNo" : compilationNo, "compilationName" : compilationName, "trandId" : trandId, "measureId" : measureId, "userCode" : userCode },
            datatype : "json",
            success : function(result) {
                alertInfo('提示', result.returnMsg);
            },
            error : function(XMLHttpRequest, textStatus, errorThrown) {
                alert(textStatus + " " + XMLHttpRequest.readyState + " "
                        + XMLHttpRequest.status + " " + errorThrown);
            }
        });
    });
}