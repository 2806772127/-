var windowSize = function() {
	var winHeight = undefined;
	// 获取窗口高度
	if (window.innerHeight) {
		winHeight = window.innerHeight;
	} else if ((document.body) && (document.body.clientHeight)) {
		winHeight = document.body.clientHeight;
	}
	// 通过深入Document内部对body进行检测，获取窗口大小
	if (document.documentElement && document.documentElement.clientHeight) {
		winHeight = document.documentElement.clientHeight;
	}
	// 返回对象结果
	return {
		'height' : winHeight
	};
};

$(function(){  
    $(".big_img").click(function(){  
        var _this = $(this);//将当前的pimg元素作为_this传入函数  
        imgShow("#outerdiv", "#innerdiv", "#bigimg", _this);  
    });  
});  

function imgShow(outerdiv, innerdiv, bigimg, _this){  
    var src = _this.attr("src");//获取当前点击的pimg元素中的src属性  
    $(bigimg).attr("src", src);//设置#bigimg元素的src属性  
  
        /*获取当前点击图片的真实大小，并显示弹出层及大图*/  
    $("<img/>").attr("src", src).load(function(){  
        var windowW = $(window).width();//获取当前窗口宽度  
        var windowH = $(window).height();//获取当前窗口高度  
        var realWidth = this.width;//获取图片真实宽度  
        var realHeight = this.height;//获取图片真实高度  
        var imgWidth, imgHeight;  
        var scale = 0.8;//缩放尺寸，当图片真实宽度和高度大于窗口宽度和高度时进行缩放  
          
        if(realHeight>windowH*scale) {//判断图片高度  
            imgHeight = windowH*scale;//如大于窗口高度，图片高度进行缩放  
            imgWidth = imgHeight/realHeight*realWidth;//等比例缩放宽度  
            if(imgWidth>windowW*scale) {//如宽度扔大于窗口宽度  
                imgWidth = windowW*scale;//再对宽度进行缩放  
            }  
        } else if(realWidth>windowW*scale) {//如图片高度合适，判断图片宽度  
            imgWidth = windowW*scale;//如大于窗口宽度，图片宽度进行缩放  
                        imgHeight = imgWidth/realWidth*realHeight;//等比例缩放高度  
        } else {//如果图片真实高度和宽度都符合要求，高宽不变  
            imgWidth = realWidth;  
            imgHeight = realHeight;  
        }  
                $(bigimg).css("width",imgWidth);//以最终的宽度对图片缩放  
          
        var w = (windowW-imgWidth)/2;//计算图片与窗口左边距  
        var h = (windowH-imgHeight)/2;//计算图片与窗口上边距  
        $(innerdiv).css({"top":h, "left":w});//设置#innerdiv的top和left属性  
        $(outerdiv).fadeIn("fast");//淡入显示#outerdiv及.pimg  
    });  
      
    $(outerdiv).click(function(){//再次点击淡出消失弹出层  
        $(this).fadeOut("fast");  
    });  
}

function show(flag, ele) {
	if (flag == "over") {
		document.getElementById(ele).style.display = 'block';
		if (ele == 'subli02') {
			showPopBoxIframeBackground($("#subli02"), 1001);
		} else if (ele == 'sub_li01' || ele == 'sub_li02' || ele == 'sub_li03'
			|| ele == 'sub_li04' || ele == 'sub_li05'||ele == 'sub_li06'||ele == 'sub_li16') {
			showPopBoxIframeBackground($("#" + ele), 1005);
		}
	} else if (flag == "out") {
		document.getElementById(ele).style.display = 'none';
		hidePopBoxIframeBackground();
	}
}

// 设置main的高度
function resizePage() {
	var SysHeight = windowSize().height;
	// top:63px, menu:32px, foot:32px, 2个margin:20px,共147px
	topHeight = $(".top").height();
	if (SysHeight < 800) {
		$('div.main').css({
			"min-height" : (716 - topHeight) + "px"
		});
		// div_nav:30px
		$('div.sm_div').css({
			"min-height" : (686 - topHeight) + "px"
		});
		$('div.d_left').css({
			"min-height" : (686 - topHeight) + "px"
		});
		$('div.d_right').css({
			"min-height" : (686 - topHeight) + "px"
		});
		$('.d_right div.mgl').css({
			"min-height" : (669 - topHeight) + "px"
		});
		$('.d_right div.zoom').css({
			"min-height" : (686 - topHeight) + "px"
		});
		// prgt01:32px, prgt02:32px, dlt:102px+12px
		$('.d_right div.prht03').css({
			"min-height" : (558 - topHeight) + "px"
		});
		$('#ImageXObject').css({
			"min-height" : (552 - topHeight) + "px"
		});
	} else {
		$('div.main').css({
			"min-height" : (SysHeight - 84 - topHeight) + "px"
		});
		// div_nav:30px
		$('div.sm_div').css({
			"min-height" : (SysHeight - 114 - topHeight) + "px"
		});
		$('div.d_left').css({
			"min-height" : (SysHeight - 114 - topHeight) + "px"
		});
		$('div.d_right').css({
			"min-height" : (SysHeight - 114 - topHeight) + "px"
		});
		$('.d_right div.mgl').css({
			"min-height" : (SysHeight - 131 - topHeight) + "px"
		});
		$('.d_right div.zoom').css({
			"min-height" : (SysHeight - 114 - topHeight) + "px"
		});
		// prgt01:32px, prgt02:32px, dlt:102px+12px
		$('.d_right div.prht03').css({
			"min-height" : (SysHeight - 272 - topHeight) + "px"
		});
		$('#ImageXObject').css({
			"min-height" : (SysHeight - 292 - topHeight) + "px"
		});
	}
}

// 设置main的高度
window.onresize = resizePage;

/** ajax 全局配置* */
// $.ajaxSetup({timeout:120000});//超时两分钟
/**
 * 角色下拉事件，在各自的页面中加如下span <span id="_cur_ajax_fun"
 * style="display:none;">这里填你需要调用的function全名</span> 当角色下拉事件改变时，会调用以上配置的function
 */
$(document).ready(function() {
	resizePage();

	$('.dateInput').datetimepicker({
		dateFormat : 'yy/mm/dd'
	});
	$(".datepicker").datetimepicker({
		dateFormat : 'yy/mm/dd'
	});
	// toPortalUpdateSession();
//	fetchRedirectSystemsHtml();
});

/** 加载JS框架 */
function loadjsframework(framework, url) {
	if (!framework) {
		jQuery.getscript(url);
	}
}

/**
 * 角色变换
 * 
 * @param object
 */
function changeGroup(object) {
	var viewWorkList_url = context_path + "/workList/viewWorkList";
	var form = $("<form></form>");
	form.attr('action', viewWorkList_url);
	form.attr('method', 'post');
	form.append($("<input type='hidden' name='roleId' value='" + $(object).attr("roleId") + "'/>"));
	form.appendTo("body");
	form.submit();
}

function getRoleValue(object) {
	var roleId =$(object).find("option:selected").attr("roleId");
	var bpmGroup = $(object).find("option:selected").attr("bpmGroup");
	$(object).attr("roleId",roleId);
	$(object).attr("bpmGroup",bpmGroup);
}

/*
 * load i18n resource through javascript
 */
function loadi18n(keys) {
	if (keys == null || keys.length == 0) {
		return;
	}
	var method = keys.length > 20 ? "POST" : "GET";
	jQuery.ajax({
		url : context_path + "/js/resource?_t=" + new Date().getTime(),
		data : {
			"key[]" : keys
		},
		type : method,
		cache : true,
		dataType : "script",
		success : function(data) {
		}
	});
}
/*
 * page jump
 */
function gotoPage(obj) {
	bodymask("请稍等...");
	window.location.href = buildUrlParam(null, "curPage", obj.value, true);
}
/*
 * 申请件查询页面 上一页 下一页 查询
 */
function gotoPageByNumber(pageNumber) {
	var obj = {
		"value" : ""
	}
	obj.value = pageNumber;
	gotoPage(obj);
}

/*
 * build url with Parameter
 */
function buildUrlParam(sourceUrl, parameterName, parameterValue, replaceDuplicates) {
	if ((sourceUrl == null) || (sourceUrl.length == 0)) {
		sourceUrl = document.location.href;
	}
	var urlParts = sourceUrl.split("?");
	var newQueryString = "";
	if (urlParts.length > 1) {
		var curors = urlParts[1].split("#");
		var parameters = curors[0].split("&");
		for (var i = 0; (i < parameters.length); i++) {
			var parameterParts = parameters[i].split("=");
			if (!(replaceDuplicates && parameterParts[0] == parameterName)) {
				if (newQueryString == "") {
					newQueryString = "?";
				} else {
					newQueryString += "&";
				}
				newQueryString += parameterParts[0] + "=" + parameterParts[1];
			}
		}
	}
	if (newQueryString == "") {
		newQueryString = "?";
	} else {
		newQueryString += "&";
	}
	newQueryString += parameterName + "=" + parameterValue;
	return urlParts[0] + newQueryString;
}

/* body mask */
function bodymask(message) {
	if (!jQuery("html").isMasked()) {
//		if ($("#ImageShow_container:visible").length == 1) {
//			$("#ImageShow_container").hide();
//		}
		jQuery("html").mask(message ? message : "請稍後...");
	}
}
/* body unmask */
function bodyunmask() {
	jQuery("html").unmask();
	if ($("#ImageShow_container:hidden").length == 1) {
		$("#ImageShow_container").show();
	}
}

/**
 * 增加隐藏按钮
 */
function addHideInformation() {
	// $("#tabInformation:hidden").show();
	// $(".zoom:visible").css({
	// "height" : "48%"
	// });
	// $("#tabInformation")
	// .prepend(
	// "<div style='position:absolute;margin:4px;right:0;'
	// id='_hideTabInformation'>"
	// + "<input type='button' value='隐藏'
	// onclick='hideTabInformation();'/></div>");
	// // bodyunmask();
}

/**
 * 结果处理
 */
function dealResult(response, status) {
	if (!status || status != "success") {
		alertInfo_4("提示", "请求异常，请稍后重试!", 250, 130);
	}
}

/**
 * 隐藏相关资料
 */
function hideTabInformation() {
	$("#tabInformation").hide();
	$(".zoom:visible").css({
		"height" : "auto"
	});
}

/* 刷新最后打开的TAB */
function refreshLastOpenTab() {
	if (currentOpenTabLinkObject) {
		openPageTab(currentOpenTabLinkObject, false);
	}
}

/* page initialization */
jQuery(window.document).ready(function() {
	jQuery("a[tabtype]").click(function() {
		openPageTab(this);
	});
	
	jQuery("#weLoan").click(function() {
    openPageTab(this);
  });
	var defaultSize = jQuery("a[tabtype][default]").length;
	if (defaultSize > 0) {
		for (var i = 0; i < defaultSize; i++) {
			openPageTab(jQuery("a[tabtype][default]").get(i));
		}
	}
	$(".zoom:visible").css({
		"height" : "auto"
	});

});

// Menu
function showMenu(flag, ele) {
	if (flag == "over") {
		document.getElementById(ele).style.display = 'block';
		if (ele == 'subli02') {
			showPopBoxIframeBackground($("#subli02"), 1001);
		} else if (ele == 'sub_li01' || ele == 'sub_li02' || ele == 'sub_li03' || ele == 'sub_li04' || ele == 'sub_li05' || ele == 'sub_li06') {
			showPopBoxIframeBackground($("#" + ele), 1005);
		}
	} else if (flag == "out") {
		document.getElementById(ele).style.display = 'none';
		hidePopBoxIframeBackground();
	}
}

/**
 * 显示影像附件列表
 */
function showAttachment() {
	var li_Elements = document.getElementById("subli01").getElementsByTagName("li");
	var GetHeight = 31;
	var len = li_Elements.length;
	$("#subli01").show();
	if (len > 21) { // 影像附件列表超过20,则另起一列显示
		for (var i = 21; i < len; i++) {
			li_Elements[i].style.position = "absolute";
			li_Elements[i].style.top = GetHeight + "px";
			li_Elements[i].style.marginLeft = "151px";
			GetHeight = GetHeight + li_Elements[i].clientHeight + 1;
		}
		showPopBoxIframeBackground($("#subli01"), 1001, false, 151);
	} else {
		showPopBoxIframeBackground($("#subli01"), 1001);
	}
}

/**
 * 隐藏影像附件列表
 */
function displayAttachment() {
	$("#subli01").hide();
	hidePopBoxIframeBackground();
}

function jqueryChildrenLoopExec(obj, callback) {
	try {
		var children = obj.children();
		callback(children);
		children.each(function(index, item) {
			jqueryChildrenLoopExec(item, callback);
		});
	} catch (e) {
	}
}

function setWholeLevelZIndex(obj, zIndex) {
	obj.css("z-index", zIndex);
	var parent = obj.parent();
	while (parent && parent.length > 0) {
		parent.css("z-index", zIndex);
		parent = parent.parent();
	}
	jqueryChildrenLoopExec(obj, function(item) {
		item.css("z-index", zIndex);
	});
}

/**
 * 显示弹出框或下拉菜單iframe背景,避免被activex控件遮挡
 */
function showPopBoxIframeBackground(frontObject, count, isDialog, frontObjectExtWith) {
	var position = frontObject.position();
	var iframeId = "popBoxIframeBackgroud";
	var iframe = $("#" + iframeId);
	if (iframe.length == 0) {
		iframe = $("<IFRAME id='" + iframeId + "' src='' style='-ms-filter:progid:DXImageTransform.Microsoft.Alpha(style=0,opacity=0);' frameBorder='0' scrolling='0'></IFRAME>");
		iframe.css("position", "absolute");
		iframe.appendTo($("body"));
		iframe.hide();
	}
	var zIndex = frontObject.css("z-index");
	frontObject.addClass("frontBox")
	if (zIndex == "auto") {
		zIndex = count;
		setWholeLevelZIndex(frontObject, zIndex);

	}
	iframe.css("z-index", 0);

	if (isDialog) {
		iframe.css("width", frontObject.width() + 30);
		iframe.css("height", frontObject.height() + 55);
		iframe.css("top", getAbsTop(frontObject.get(0)) - 35);
	} else if (frontObjectExtWith) {
		iframe.css("width", frontObject.width() + frontObjectExtWith);
		iframe.css("height", frontObject.height());
		iframe.css("top", getAbsTop(frontObject.get(0)));
	} else {
		iframe.css("width", frontObject.width());
		iframe.css("height", frontObject.height());
		iframe.css("top", getAbsTop(frontObject.get(0)));
	}

	iframe.css("left", getAbsLeft(frontObject.get(0)));
	iframe.css("background", "#F4F4F4");
	// iframe.css("filter", "alpha(opacity=0)");
	// iframe.css("opacity", "0");
	// iframe.css("-ms-filter",
	// "progid:DXImageTransform.Microsoft.Alpha(style=0,opacity=0)");
	// iframe.css("-moz-opacity", "0");
	iframe.show();
}

/**
 * 获取元素距离屏幕左边的值
 * 
 * @param obj
 * @returns
 */
function getAbsLeft(obj) {
	var left = obj.offsetLeft;
	while (obj.offsetParent != null) {
		obj = obj.offsetParent;
		left += obj.offsetLeft;
	}
	return left;
}

/**
 * 获取元素距离屏幕上方的值
 * 
 * @param obj
 * @returns
 */
function getAbsTop(obj) {
	var top = obj.offsetTop;
	while (obj.offsetParent != null) {
		obj = obj.offsetParent;
		top += obj.offsetTop;
	}
	return top;
}

/**
 * 隐藏弹出框或下拉菜單iframe背景
 */
function hidePopBoxIframeBackground() {
	var iframeId = "popBoxIframeBackgroud";
	$("#" + iframeId).hide();
}

String.prototype.trim = function() {
	return this.replace(/(^\s*)|(\s*$)/g, "");
};

/**
 * 提示信息
 * 
 * @param title
 *            标题
 * @param content
 *            内容
 * @param okCallback
 *            处理函数
 */
function alertInfo(title, content, okCallback, extClass) {
	if (content != undefined && content != '' && (content.indexOf("认领成功") != -1 || content.indexOf("操作成功") != -1)) {
		if (okCallback != undefined) {
			okCallback.call();
		}
	} else {
		alertInfo_2(title, content, 250, 130, okCallback, extClass);
	}
}
function alertInfo_1(title, content, closeCallback, width, height) {
	if (content != undefined && content != '' && (content.indexOf("认领成功") != -1 || content.indexOf("操作成功") != -1)) {
		if (closeCallback != undefined) {
			closeCallback.call();
		}
	} else {
		var dialogObj = {};
		dialogObj.title = title;
		dialogObj.width = width;
		dialogObj.height = height;
		dialogObj.modal = true;
		dialogObj.close = function(event, ui) {
			if (closeCallback != undefined) {
				closeCallback.call();
			}
		};
		var info = $("<div><p class='conct fsize pad>'>" + content + "</p><p class='conct pad'><input type='button' value='確定'/></p></div>");
		$(info).dialog(dialogObj);
		$(info).find("input:eq(0)").bind("click", function() {
			$(info).dialog("close");
		});
	}
}

function alertInfo_2(title, content, width, height, okCallback, extClass) {
	var dialogObj = {};
	dialogObj.title = title;
	dialogObj.width = width;
	dialogObj.height = height;
	dialogObj.modal = true;
	if (extClass) {
		dialogObj.dialogClass = extClass;
	}
	alertInfo_3(dialogObj, content, okCallback);
}

function alertInfo_3(dialogObj, content, okCallback) {
//	if ($("#ImageShow_container:visible").length == 1) {
//		$("#ImageShow_container:visible").hide();
//		dialogObj.close = function() {
//			$("#ImageShow_container:hidden").show();
//		};
//	}
	var info = $("<div><p class='conct fsize pad>'>" + content + "</p><p class='conct pad'><input type='button' value='確定'/></p></div>");
	$(info).dialog(dialogObj);
	$(info).find("input:eq(0)").bind("click", function() {
		if (okCallback != undefined) {
			okCallback.call();
		}
		$(info).dialog("close");
	});
}

function alertInfo_4(title, content, width, height, okCallback, extClass) {
	var dialogObj = {};
	dialogObj.title = title;
	dialogObj.width = width;
	dialogObj.height = height;
	dialogObj.modal = true;
	if (extClass) {
		dialogObj.dialogClass = extClass;
	}
//	if ($("#ImageShow_container:visible").length == 1) {
//		$("#ImageShow_container:visible").hide();
//		dialogObj.close = function() {
//			$("#ImageShow_container:hidden").show();
//			hidePopBoxIframeBackground();
//		};
//	} 
	else {
		dialogObj.close = function() {
			hidePopBoxIframeBackground();
		};
	}
	var info = $("<div><p class='conct fsize pad>'>" + content + "</p><p class='conct pad'><input type='button' value='確定'/></p></div>");
	$(info).dialog(dialogObj);
	showPopBoxIframeBackground($(info), 1005, true);
	$(info).find("input:eq(0)").bind("click", function() {
		if (okCallback != undefined) {
			okCallback.call();
		}
		$(info).dialog("close");
	});
}

/**
 * 确认提示
 * 
 * @param title
 *            标题
 * @param content
 *            内容
 * @param okCallback
 *            【确认】处理函数
 * @param cancelCallback
 *            【取消】处理函数
 * @param maskCallback
 *            【遮罩】函数，需要在处理完成之后调用UnMaskCallback
 */
function confirmInfo_1(title, content, okCallback, cancelCallback, maskCallback) {
	// typeof(obj) == 'object'
	confirmInfo_2(title, content, 250, 130, okCallback, cancelCallback, maskCallback);
}

/**
 * 确认提示
 * 
 * @param title
 *            标题
 * @param content
 *            内容
 * @param width
 *            宽度
 * @param height
 *            高度
 * @param okCallback
 *            【确认】处理函数
 * @param cancelCallback
 *            【取消】处理函数
 * @param maskCallback
 *            【遮罩】函数，需要在处理完成之后调用UnMaskCallback
 */
function confirmInfo_2(title, content, width, height, okCallback, cancelCallback, maskCallback) {
	var dialogObj = {};
	dialogObj.title = title;
	dialogObj.width = width;
	dialogObj.height = height;
	dialogObj.modal = true;
	confirmInfo_3(dialogObj, content, okCallback, cancelCallback, maskCallback);
}

/**
 * 确认提示
 * 
 * @param dialogObj
 *            dialog属性（必须包含title）
 * @param content
 *            内容
 * @param okCallback
 *            【确认】处理函数
 * @param cancelCallback
 *            【取消】处理函数
 * @param maskCallback
 *            【遮罩】函数，需要在处理完成之后调用UnMaskCallback
 */
function confirmInfo_3(dialogObj, content, okCallback, cancelCallback, maskCallback) {
	var info = $("<div><p class='conct fsize pad>'>" + content + "</p><p class='conct pad'><input type='button' value='確認'/>&nbsp;&nbsp;<input type='button' value='取消'/></p></div>");
	$(info).dialog(dialogObj);
	$(info).find("input:eq(0)").bind("click", function() {
		$(info).dialog("close");
		if (okCallback != undefined) {
			if (maskCallback != undefined) {
				maskCallback.call();
			}
			okCallback.call();
		}
	});
	$(info).find("input:eq(1)").bind("click", function() {
		$(info).dialog("close");
		if (cancelCallback != undefined) {
			if (maskCallback != undefined) {
				maskCallback.call();
			}
			cancelCallback.call();
		}
	});
}

function confirmInfo(settings) {
	//默认设置
	var dialogObj = {};
	dialogObj.title = "提示";
	dialogObj.content = "確認";
	dialogObj.width = "300";
	dialogObj.height = "200";
	dialogObj.modal = true;
	dialogObj.okCallback = undefined;
	dialogObj.cancelCallback = undefined;
	dialogObj.maskCallback = undefined;
	dialogObj.okButtonName = "確認";
	dialogObj.okButtonClass = "button_blue";
	dialogObj.cancelButtonName = "取消";
	dialogObj.cancelButtonClass = "button_gray";

	//采用配置项替换默认值，作用类似jquery.extend
	if (arguments.length === 1 && typeof arguments[0] === "object") {
		var src = arguments[0] || {};
		for (key in src) {
			if (src[key] && dialogObj[key] && src[key] === dialogObj[key])
				continue;
			else if (src[key])
				dialogObj[key] = src[key];
		}
	}

	var info = $("<div style=''><p class='conct fsize pad>' style='height:60px'>" + dialogObj.content + "</p><p class='conct pad'>"
		+ "<input type='button' class='"+dialogObj.okButtonClass+"' value='"+dialogObj.okButtonName+"'/>&nbsp;&nbsp;"
	    + "<input type='button' class='"+dialogObj.cancelButtonClass+"' value='"+dialogObj.cancelButtonName+"'/></p></div>");
	$(info).dialog(dialogObj);
	$(info).find("input:eq(0)").bind("click", function() {
		$(info).dialog("close");
		if (dialogObj.okCallback != undefined) {
			if (dialogObj.maskCallback != undefined) {
				dialogObj.maskCallback.call();
			}
			dialogObj.okCallback.call();
		}
	});
	$(info).find("input:eq(1)").bind("click", function() {
		$(info).dialog("close");
		if (dialogObj.cancelCallback != undefined) {
			if (dialogObj.maskCallback != undefined) {
				dialogObj.maskCallback.call();
			}
			dialogObj.cancelCallback.call();
		}
	});
}

function confirmInfo_4(dialogObj, content, okCallback, cancelCallback, maskCallback) {
	var info = $("<div><p class='conct fsize pad>'>" + content + "</p><p class='conct pad'><input type='button' value='登出'/>&nbsp;&nbsp;<input type='button' value='取消'/></p></div>");
	$(info).dialog(dialogObj);
	$(info).find("input:eq(0)").bind("click", function() {
		$(info).dialog("close");
		if (okCallback != undefined) {
			if (maskCallback != undefined) {
				maskCallback.call();
			}
			okCallback.call();
		}
	});
	$(info).find("input:eq(1)").bind("click", function() {
		$(info).dialog("close");
		if (cancelCallback != undefined) {
			if (maskCallback != undefined) {
				maskCallback.call();
			}
			cancelCallback.call();
		}
	});
}

/**
 * 验证给定父节点下所有表单的信息
 * 
 * @param parentNode
 *            父节点
 * @returns {Boolean} 是否验证通过
 * @see jquery-simplevalidator.js有关内容
 */
function validateInput(parentNode) {
	isSubmit = true;// 是否提交
	var bValid = true;
	// required
	$(parentNode).find(":input.required").each(function() {
		bValid = $(this).simpleValidate() && bValid;
		if (!bValid) {
			$(this).bindSimpleValidator();
		}
	});
	// datatype
	$(parentNode).find(":input[datatype]").each(function() {
		bValid = $(this).simpleValidate() && bValid;
		if (!bValid) {
			$(this).bindSimpleValidator();
		}
	});
	return bValid;
}

/**
 * 清除必填错误信息
 */
function removeInputValidateError(parentNode) {
	$(parentNode).find(".required").each(function() {
		$(this).removeValidError();
	});
	$(parentNode).find(":input[datatype]").each(function() {
		$(this).removeValidError();
	});
}

/**
 * 比较时间大小
 * 
 * @param startDate
 *            起始日
 * @param endDate
 *            结束日
 * @returns {Boolean}
 */
function checkEndTime(startDate, endDate) {
	if (startDate != "" && endDate != "") {
		var start = new Date(startDate);
		var end = new Date(endDate);
		if (end < start) {
			alertInfo("提示", "截止日期不能小于起始日期", null);
			return false;
		}
	}
	return true;
}
/**
 * 根据身份证号计算年龄
 * 
 * @param cardNo
 *            身份证号
 * @returns 年龄
 */
function setAgeByCardNo(cardNo) {
	var age = undefined;
	var currentDate = new Date(), currentMonth = currentDate.getMonth() + 1, currentDay = currentDate.getDate();
	if (cardNo !== undefined && typeof cardNo == "string") {
		var tempYear;
		if (cardNo.length == 18) {
			tempYear = cardNo.substring(6, 10);
		} else if (cardNo.length == 15) {
			tempYear = cardNo.substring(6, 8);
			tempYear = 19 + tempYear;
		}
		age = currentDate.getFullYear() - tempYear - 1;
		if (cardNo.substring(10, 12) < currentMonth || cardNo.substring(10, 12) == currentMonth && cardNo.substring(12, 14) <= currentDay) {
			age++;
		}
	}
	return age;
}
/**
 * 根据身份证号计算出生日期
 * 
 * @param cardNo
 *            身份证号
 * @returns 出生日期
 */
function setBirthdayByCardNo(cardNo) {
	var birthday = undefined;
	if (cardNo !== undefined && typeof cardNo == "string") {
		if (cardNo.length == 18) {
			birthday = cardNo.substring(6, 10) + "/" + cardNo.substring(10, 12) + "/" + cardNo.substring(12, 14);
		} else {
			birthday = "19" + cardNo.substring(6, 8) + "/" + cardNo.substring(8, 10) + "/" + cardNo.substring(10, 12);
		}
	}
	return birthday;
}

var OPERATION_TYPE_APPROVAL = "通过";
var OPERATION_TYPE_REJECT = "拒绝";

/** 设置审核备注默认值* */
function setDefaultNote(obj) {
	var selectedValue = $(obj).attr("approveType");
	var selectedNoteArea = $(obj).attr("noteAreaId");
	if (selectedValue == 1) {
		if ($("#" + selectedNoteArea).val() == "" || $("#" + selectedNoteArea).val() == OPERATION_TYPE_REJECT) {
			$("#" + selectedNoteArea).val(OPERATION_TYPE_APPROVAL);
		}
		$("#_1_note").attr("class","required");
		$("#_2_note").attr("class","");
	} else if (selectedValue == 2) {
		if ($("#" + selectedNoteArea).val() == "" || $("#" + selectedNoteArea).val() == OPERATION_TYPE_APPROVAL) {
			$("#" + selectedNoteArea).val(OPERATION_TYPE_REJECT);
		}
		$("#_1_note").attr("class","");
		$("#_2_note").attr("class","required");
	} else {
		if ($("#" + selectedNoteArea).val() == "" || $("#" + selectedNoteArea).val() == OPERATION_TYPE_APPROVAL || $("#" + selectedNoteArea).val() == OPERATION_TYPE_REJECT) {
			$("#" + selectedNoteArea).val("");
		}
		$("#_1_note").attr("class","");
		$("#_2_note").attr("class","");
	}

}


/** 弹出修改窗口* */
function updateUserPasswordDialog(username) {
	$.ajax({
		url : context_path + "/user/viewChangePassword",
		type : "post",
		data : {
			username : username
		},
		dataType : "html",
		success : function(result) {
			// 影藏影像附件
//			if ($("#ImageShow_container:visible").length == 1) {
//				$("#ImageShow_container:visible").hide();
//			}
			$("#updateUser").empty();
			$("#updateUser").html(result);
		}
	});

	$("#updateUser").dialog({
		title : "用户密码修改",
		width : 330,
		height : 230,
		modal : true,
		draggable : false,
		close : function() {
			// 显示影像附件
			if ($("#ImageShow_container:hidden").length == 1) {
				$("#ImageShow_container:hidden").show();
			}
		}

	});
}

/** 修改密码* */
function updateUserPassword() {
	var oldPwd = $("#oldPwd").val();
	var newPwd1 = $("#newPwd1").val();
	var newPwd2 = $("#newPwd2").val();
	var username = $("#userName").val();
	if (oldPwd == "") {
		alertInfo("提示", "当前密码不能为空", null);
		return;
	}
	if (newPwd1 == "") {
		alertInfo("提示", "新密码不能为空", null);
		return;
	}
	if (newPwd1 != newPwd2) {
		alertInfo("提示", "两次密码不一致", null);
		return;
	}
	var userUrl = context_path + "/user/updateUserPassword";
	var params = {
		"username" : username,
		"oldPwd" : oldPwd,
		"newPwd1" : newPwd1,
		"newPwd2" : newPwd1
	};
	$.ajax({
		url : userUrl,
		method : 'post',
		dateType : 'json',
		data : params,
		success : function(json) {
			result = eval("(" + json + ")");
			if (result.success) {
				$("#updateUser").dialog("close");
				alertInfo("提示", result.message, null);
			} else {
				alertInfo("提示", result.message, null);
			}
		}
	});
}

function codeOnChange(obj) {
	var relCode = $(obj).find("option:selected").val();
	var relName = $(obj).find("option:selected").text();
	var par = $(obj).parent();
	par.find("input[attr='relCode']").val(relCode);
	par.find("input[attr='relName']").val(relName);
}

/**
 * 获取上一个月第一天日期
 */
function getLastMonthDate() {
	var dateNow = new Date();
	var date = dateNow;
	var fullYear = dateNow.getFullYear();
	var month = dateNow.getMonth();
	if (month == 0) { // 如果是一月份，则日期是去年的最后一个月
		fullYear -= 1;
		month = 11;
	} else {
		month -= 1;
	}
	date.setFullYear(fullYear, month, 1);
	return date;
}

/**
 * 格式化日期
 */
function formatDate(date, format) {
	var paddNum = function(num) {
		num += "";
		return num.replace(/^(\d)$/, "0$1");
	}
	// 指定格式字符
	var cfg = {
		yyyy : date.getFullYear() // 年 : 4位
		,
		yy : date.getFullYear().toString().substring(2)// 年 : 2位
		,
		M : date.getMonth() + 1 // 月 : 如果1位的时候不补0
		,
		MM : paddNum(date.getMonth() + 1) // 月 : 如果1位的时候补0
		,
		d : date.getDate() // 日 : 如果1位的时候不补0
		,
		dd : paddNum(date.getDate())// 日 : 如果1位的时候补0
		,
		hh : paddNum(date.getHours()) // 时
		,
		mm : paddNum(date.getMinutes()) // 分
		,
		ss : paddNum(date.getSeconds())
	// 秒
	}
	format || (format = "yyyy-MM-dd hh:mm:ss");
	return format.replace(/([a-z])(\1)*/ig, function(m) {
		return cfg[m];
	});
}

/**
 * 将身份证按某个格式显示(空格分割) 15 : xxx xxx xxxxxx xxx 18 : xxx xxx xxxxxxxx xxxx
 * 
 * @param obj
 *            input object
 */
function formatIDCardNum(obj) {
	var val = obj.value.replace(/[ ]+/g, "");
	if (val.length == 15) {
		obj.value = val.replace(/(\d{3})(\d{3})(\d{6})(.+)/, "$1 $2 $3 $4");
	} else if (val.length == 18) {
		obj.value = val.replace(/(\d{3})(\d{3})(\d{8})(.+)/, "$1 $2 $3 $4");
	}
}

/**
 * 将银行卡卡号按4位一显示(空格分割) eg : xxxx xxxx xxxx xxxx xxx
 * 
 * @param obj
 */
function formatBankCardNum(obj) {
	var val = obj.value.replace(/[ ]+/g, "");
	obj.value = val.replace(/(\d{4})(?!$)/g, "$1 ");
}

function replaceAllSpace(str) {
	return str.replace(/[ ]+/g, "");
}

/**
 * 设置选择下拉对象需要儲存的名称
 * 
 * @param obj
 *            选择下拉对象
 */
function setSelectNameByCode(obj) {
	var nameInputId = $(obj).attr("id") + "_name";
	if ($("#" + nameInputId).length > 0) {// 已经存在
		if ($(obj).val() == "") {
			$("#" + nameInputId).val("");
		} else {
			$("#" + nameInputId).val($(obj).find("option:selected").text());
		}
	}
}

/**
 * 设置选择下拉对象需要儲存的名称，选择其他选项需要输入其他的名称
 * 
 * @param obj
 *            选择下拉对象
 * @param otherValue
 *            其他选项的值
 */
function setSelectNameByCode2(obj, otherValue) {
	var textEm = $(obj).parent().find("input[type='text']");
	if ($(obj).val() == otherValue) {
		textEm.prop("disabled", false);
		textEm.addClass("required");
		textEm.attr("onblur", "setValueTextToHidden(this);");
		textEm.bindSimpleValidator();
	} else {
		textEm.val("");
		textEm.removeAttr("onblur");
		textEm.prop("disabled", true);
		textEm.removeClass("required");
		textEm.unbindSimpleValidator();
		setSelectNameByCode(obj);
	}
}

/**
 * 将输入框的值赋值给隐藏框
 * 
 * @param textEm
 *            输入框元素
 * @param hiddenEm
 *            隐藏框元素
 */
function setValueTextToHidden(obj) {
	var hiddenEm = $(obj).parent().find("input[type='hidden']");
	hiddenEm.val($(obj).val());
}

/**
 * 显示和隐藏 下拉框是其他时 的备注输入框
 * 
 * @param obj
 *            下拉框节点
 * @param isRequired
 *            其他备注输入框是否必填
 * @param formName
 *            input框的name
 */
function setOther(obj, isRequird, formName) {
	var val = $(obj).find("option:selected").text().trim();
	var objId = $(obj).attr("id");

	if (val == '其他') {// 其他，需填写备注
		$("#" + objId + "_span").show();
		$("#" + objId + "_name").removeAttr("name");
		// $("#" + objId + "_name").val("");
		$("#" + objId + "_text").attr("name", formName);
		if (isRequird) {
			$("#" + objId + "_text").addClass("required");
		}
	} else {
		$("#" + objId + "_text").removeAttr("name");
		if (isRequird) {
			$("#" + objId + "_text").removeClass("required");
		}
		$("#" + objId + "_name").attr("name", formName);
		$("#" + objId + "_span").hide();
	}
}

/**
 * 设置单选按钮的名称
 * 
 * @param obj
 *            单选对象
 */
function setRadioNameByCode(obj) {
	var nameInputId = $(obj).attr("class") + "_name";
	if ($("#" + nameInputId).length > 0) {// 已经存在
		$("#" + nameInputId).val($(obj).attr("itemName"));
	}
}

/**
 * 选中附件选项后，隐藏选项
 */
function displayAttachmentSel() {
	displayAttachment();
}

/**
 * 切换语言
 * @param obj
 */
function changeLanguage() {
	$.ajax({
		url : context_path + "/changeLanauage",
		type : "POST",
		data : {"lang": $("#language").val()},
		async : false,
		success : function(data) {// 这里的data是由请求页面返回的数据
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
		}
	});
}

/**
 * 打开弹窗
 */
function openDialog(divId, title, width, height) {
	$("#" + divId).dialog({
		title : title,
		width : width,
		height : height,
		modal : true,
		draggable : false
	});
}

function gotoIndex() {
	window.location.href =
		context_path + "/missionStroke/viewMissionStroke";
}

function logout() {
	var dialogObj = {};
	dialogObj.title = "提示";
	dialogObj.width = 250;
	dialogObj.height = 150;
	dialogObj.modal = true;

	confirmInfo_4(dialogObj, "您確定登出系統嗎？", function (){
		// window.location.href =
		// 	context_path + "/logout";
			var userAgent = navigator.userAgent;
			if (userAgent.indexOf("Firefox") != -1 || userAgent.indexOf("Chrome") !=-1) {
				window.location.href="about:blank";
			} else {
				window.opener = null;
				window.open("", "_self");
				window.close();
			};
	},null,null);
}
function alertInfoCOPY(title, content, okCallback, extClass) {
    if (content != undefined && content != '' && (content.indexOf("认领成功") != -1 || content.indexOf("操作成功") != -1)) {
        if (okCallback != undefined) {
            okCallback.call();
        }
    } else {
        alertInfo_2COPY(title, content, 250, 130, okCallback, extClass);
    }
}


function alertInfo_2COPY(title, content, width, height, okCallback, extClass) {
    var dialogObj = {};
    dialogObj.title = title;
    dialogObj.width = width;
    dialogObj.height = height;
    dialogObj.modal = true;
    if (extClass) {
        dialogObj.dialogClass = extClass;
    }
    alertInfo_3COPY(dialogObj, content, okCallback);
}

function alertInfo_3COPY(dialogObj, content, okCallback) {
//	if ($("#ImageShow_container:visible").length == 1) {
//		$("#ImageShow_container:visible").hide();
//		dialogObj.close = function() {
//			$("#ImageShow_container:hidden").show();
//		};
//	}
    var info = $("<div><p class='conct fsize pad>'>" + content + "</p><p class='conct pad'><input type='button' value='確定'/> <input type='button' value='取消'/> </p></div>");
    $(info).dialog(dialogObj);
    $(info).find("input:eq(0)").bind("click", function() {
        if (okCallback != undefined) {
            okCallback.call();
        }
        $(info).dialog("close");
    });
    $(info).find("input:eq(1)").bind("click", function() {
        $(info).dialog("close");
    });
}

/**
 * 返回公用方法
 * @param url
 */
function goBack(url){
	var data = encodeURIComponent($("#searchData").val());
    $("#searchData").val("");
    window.location.href = context_path + url + "?searchData="+data;
}

/**
 * 获取查询数据公用方法
 * @returns {string}
 */
function getSearchData(){
    var searchData = encodeURIComponent($("#searchForm").serialize()+"&curPage="+$("#curPage").val());
    return "&searchData="+searchData;
}

/**
 * 查询结果列表页面公用方法
 * @param curPage
 * @param url
 * @param emId
 */
function queryDataCommon(curPage,url,emId){
	bodymask("查詢中，請稍等...");
    var viewUrl = window.location.href.split("?");
    if(viewUrl.length>1){
        var newUrl = viewUrl[0];
        //清除url中带的参数,防止刷新还原查询条件
        history.pushState({},"",newUrl);
	}
    var data = $("#searchData").val();
    if(data==undefined || data==null || data==""){
        curPage = (curPage!=undefined && curPage!=null)? curPage:$("#curPage").val();
        data = $("#searchForm").serialize()+"&curPage="+curPage;
    }
    $.ajax({
        url: context_path + url,
        type: "post",
        data: data,
        datatype: "html",
        success: function(result) {
        	bodyunmask();
            $("#"+emId).empty();
            $("#"+emId).html(result);
            $("#searchData").val("");
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
        	bodyunmask();
            alert(textStatus + " " + XMLHttpRequest.readyState + " "
                + XMLHttpRequest.status + " " + errorThrown);
        }
    });
}

/**
 * 儲存公用方法
 * @param url
 * @param backUrl
 */
function saveCommon(url,backUrl){
    bodymask("儲存中，請稍等...");
	var data = $("#saveForm").serialize();
    $.ajax({
        url:context_path + url,
        datatype:"json",
        type:"post",
        data:data,
        success:function (data) {
        	bodyunmask();
            alertInfo("提示",data,function () {
                goBack(backUrl);
            });
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
        	bodyunmask();
            alert(textStatus + " " + XMLHttpRequest.readyState + " "
                + XMLHttpRequest.status + " " + errorThrown);
        }
    });
}

/**
 * 初始化时间公用方法
 */
function initDate(){
	$(".startDate").each(function(){
		var em = $(this);
        em.next("img").click(function() {
            $(this).focus();
        });
        em.datetimepicker({
            onClose: function(selectedDate) {
                $(this).parent().find(".endDate").datepicker("option", "minDate", selectedDate);
            }
        });
	});

    $(".endDate").each(function(){
        var em = $(this);
        em.next("img").click(function() {
            $(this).focus();
        });
        em.datetimepicker({
            onClose: function(selectedDate) {
                $(this).parent().find(".startDate").datepicker("option", "maxDate", selectedDate);
            }
        });
    });
}

var saveFlag = true;
function setSaveFlag(){
    $(":input").change(function(){
        saveFlag = false;
    });
}

/**
 * @description 將form表單里的數據轉成JSON對象
 * @param formId
 * @returns
 */
function serializeFormJSON(formId) {
    var formArray = $("#" + formId).serializeArray();
    var formJSON = {};

    $.map(formArray, function (n, i) {
        formJSON[n['name']] = n['value'];
    });

    return formJSON;
}

