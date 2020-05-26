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
        $("#compilationNo").val(localStorage.getItem("applyCompilationNo"));
        $("#companyName").val(localStorage.getItem("applyCompanyName"));
        $("#startDate").val(localStorage.getItem("applyStartDate"));
        $("#endDate").val(localStorage.getItem("applyEndDate"));
        $("#areaCode").val(localStorage.getItem("applyAreaCode"));
        $("#groupCode").val(localStorage.getItem("applyGroupCode"));
        $("#userCode").val(localStorage.getItem("applyUserCode"));

        curPage = localStorage.getItem("applyCurPage");
        // 清除所有缓存
        localStorage.clear();
    }

    queryApplyIncoms(curPage);
});
//显示图片
var len = $("#imgCon").length;
var arrPic = new Array(); //定义一个数组
for (var i = 0; i < len; i++) {
    arrPic[i] = $("img[modal='zoomImg']").eq(i).prop("src"); //将所有img路径存储到数组中
}
var page=$("#curPage").val();

function  queryApplyIncoms(curPage) {


    if (page=="" && page=='undefined') {
        if(curPage!="" && curPage!='undefined'){
            curPage = curPage;
        }else{
            curPage = 1;
        }

    }else  {
        if(curPage!="" && curPage!='undefined'){
            curPage = curPage;
        }else{
            curPage = page;
            page="";
        }


    }

    var compilationNo = $("#compilationNo").val();
    var companyName = $("#companyName").val();
    var startDate = $("#startDate").val();
    var endDate = $("#endDate").val();

    //下拉框搜索条件
    var areaCode = $("#areaCode").val();
    var groupCode = $("#groupCode").val();
    var userCode = $("#userCode").val();


    $.ajax({
        url: context_path + "/ApplyIncom/queryApplyIncom",
        type: "post",
        data: {
            "compilationNo": compilationNo, "companyName": companyName,
            "startDate": startDate, "endDate": endDate, "curPage": curPage,
            "areaCode": areaCode, "groupCode": groupCode, "userCode": userCode
        },
        datatype: "html",
        success: function(result) {
            $("#applyIncom_list").empty();
            $("#applyIncom_list").html(result);
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            alert(textStatus + " " + XMLHttpRequest.readyState + " "
                + XMLHttpRequest.status + " " + errorThrown);
        }
    })
}

function  viewIncomDetail(obj) {

    localStorage.setItem("applyCompilationNo", $("#compilationNo").val());
    localStorage.setItem("applyCompanyName", $("#companyName").val());
    localStorage.setItem("applyStartDate", $("#startDate").val());
    localStorage.setItem("applyEndDate", $("#endDate").val());
    localStorage.setItem("applyAreaCode", $("#areaCode").val());
    localStorage.setItem("applyGroupCode", $("#groupCode").val());
    localStorage.setItem("applyUserCode", $("#userCode").val());
    localStorage.setItem("applyCurPage", $("#page_select").val());
    localStorage.setItem("selfPage", "Y");

    var trandId = $(obj).attr("trandId");
    if (trandId == null || trandId == "" || trandId == "undefined" || trandId == undefined) {
        trandId = "";
    }
    var compilationNo = $(obj).attr("compilationNo");
    var comIndustryCode = $(obj).attr("comIndustryCode");
    window.location.href = context_path + "/ApplyIncom/seeApplyIncom?compilationNo="+compilationNo+"&trandId="+trandId+"&comIndustryCode="+comIndustryCode;

}


function editApplyIncom(obj) {

    localStorage.setItem("applyCompilationNo", $("#compilationNo").val());
    localStorage.setItem("applyCompanyName", $("#companyName").val());
    localStorage.setItem("applyStartDate", $("#startDate").val());
    localStorage.setItem("applyEndDate", $("#endDate").val());
    localStorage.setItem("applyAreaCode", $("#areaCode").val());
    localStorage.setItem("applyGroupCode", $("#groupCode").val());
    localStorage.setItem("applyUserCode", $("#userCode").val());
    localStorage.setItem("applyCurPage", $("#page_select").val());
    localStorage.setItem("selfPage", "Y");

    var trandId = $(obj).attr("trandId");
    var trandId = $(obj).attr("trandId");
    if (trandId == null || trandId == "" || trandId == "undefined" || trandId == undefined) {
        trandId = "";
    }
    var compilationNo = $(obj).attr("compilationNo");
    var comIndustryCode = $(obj).attr("comIndustryCode");
    var applyNumber = $(obj).attr("applyNumber");
    var curPage = $(obj).attr("curPage");
    window.location.href = context_path + "/ApplyIncom/editApplyIncom?compilationNo="+compilationNo+"&trandId="+trandId+"&comIndustryCode="+comIndustryCode+"&applyNumber="+applyNumber+"&curPage="+curPage;

}


function  changClass(flag) {

    if(flag =='1'){
        $("#new").attr("class","gpic");
        $("#old").attr("class","pic");
    }else{
        $("#old").attr("class","gpic");
        $("#new").attr("class","pic");
    }

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

function showImg(obj) {
    // 清掉上次打開的圖片
    $("#photo_index").attr("src", "");

    //1清除iframe防止頁面報錯 2.清除歷史打開數據
    $("#otherDocSrc").children("iframe").remove();

    var imgName = $(obj).attr("imgName");
    var imgDate = $(obj).attr("imgDate");
    var imgId = $(obj).attr("imgId");
    var imgType = $(obj).attr("imgType");
    $("#phName").val(imgName);
    $("#phDate").val(imgDate);
    var suffix = imgName.substr(imgName.lastIndexOf("."));
    if (suffix==".pdf"){
       window.open("/fileService/"+imgId, "_blank")
        return false;
    }
    bodymask("搜索中...");
    $.ajax({
        url :context_path + "/ApplyIncom/showView",
        type: "post",
        data:{"id":imgId},
        datatype : "json",
        success :function (filesrc) {
            bodyunmask();
            if(imgType=="1"){
                $("#vedioCon").attr("src",filesrc);
                // 重新加载音频
                $("#audioBox").load();
                $("#videoName").val(imgName);
                $("#videoDate").val(imgDate);
                openDialog("musicorvideo","音頻文件",525,325);
            }else if(imgType=="2"){
                openDialog("musicorimg","圖片文件",650,665);
                $("#phName").val(imgName);
                $("#phDate").val(imgDate);
                showPhoto(filesrc);
            } else {
                window.open(context_path + "/ApplyIncom/viewOtherFile?otherDocName="+imgName+"&otherDocDate="+imgDate+"&filesrc="+filesrc);
            }
        },error: function(XMLHttpRequest, textStatus, errorThrown) {
            alert(textStatus + " " + XMLHttpRequest.readyState + " "
                + XMLHttpRequest.status + " " + errorThrown);
        }
    });

}

function closeImg(id){
    $("#"+id).dialog('close');
    if(id=='musicorvideo'){
        var audio = document.getElementById('audioBox');
        if(audio!= null){
            audio.pause();//暫停播放音頻
        }
    }
}

function  findPage(curPage) {
    queryApplyIncoms(curPage);
}


/**
 * Created by TF on 2018/1/15.
 */
function changImg(){
$("#imgCon").each(function () {
    $(this).on("click", function () {
        //给body添加弹出层的html
        $("body").append("<div class=\"mask-layer\">" +
            "   <div class=\"mask-layer-black\"></div>" +
            "   <div class=\"mask-layer-container\">" +
            "       <div class=\"mask-layer-container-operate\">" +
            "           <button class=\"mask-prev btn-default-styles\" style=\"float: left\">上一张</button>" +
            "           <button class=\"mask-out btn-default-styles\">放大</button>" +
            "           <button class=\"mask-in btn-default-styles\">缩小</button>" +
            "           <button class=\"mask-clockwise btn-default-styles\">顺旋转</button>" +
            "           <button class=\"mask-counterclockwise btn-default-styles\">逆旋转</button>" +
            "           <button class=\"mask-close btn-default-styles\">关闭</button>" +
            "           <button class=\"mask-next btn-default-styles\" style=\"float: right\">下一张</button>" +
            "       </div>" +
            "       <div class=\"mask-layer-imgbox auto-img-center\"></div>" +
            "   </div>" +
            "</div>"
        );

        var $this = $(this);
        var img_index = $this.index(); //获取点击的索引值
        var num = img_index;

        function showImgs() {
            $(".mask-layer-imgbox").append("<p><img src=\"\" alt=\"\"></p>");
            $(".mask-layer-imgbox img").prop("src", arrPic[num]); //给弹出框的Img赋值

            //图片居中显示
            var box_width = $(".auto-img-center").width(); //图片盒子宽度
            var box_height = $(".auto-img-center").height();//图片高度高度
            var initial_width = $(".auto-img-center img").width();//初始图片宽度
            var initial_height = $(".auto-img-center img").height();//初始图片高度
            if (initial_width > initial_height) {
                $(".auto-img-center img").css("width", box_width);
                var last_imgHeight = $(".auto-img-center img").height();
                $(".auto-img-center img").css("margin-top", -(last_imgHeight - box_height) / 2);
            } else {
                $(".auto-img-center img").css("height", box_height);
                var last_imgWidth = $(".auto-img-center img").width();
                $(".auto-img-center img").css("margin-left", -(last_imgWidth - box_width) / 2);
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
                    var now_x = (offset_x + _x ) + "px";
                    var now_y = (offset_y + _y ) + "px";
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
                        "transform":"scale(.1)",
                        "-moz-transform":"scale(.1)",
                        "-ms-transform":"scale(.1)",
                        "-o-transform":"scale(.1)",
                        "-webkit-transform":"scale(.1)"
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
            //旋转
            var spin_n = 0;
            $(".mask-clockwise").click(function () {
                spin_n += 15;
                $(".mask-layer-imgbox img").parent("p").css({
                    "transform":"rotate("+ spin_n +"deg)",
                    "-moz-transform":"rotate("+ spin_n +"deg)",
                    "-ms-transform":"rotate("+ spin_n +"deg)",
                    "-o-transform":"rotate("+ spin_n +"deg)",
                    "-webkit-transform":"rotate("+ spin_n +"deg)"
                });
            });
            $(".mask-counterclockwise").click(function () {
                spin_n -= 15;
                $(".mask-layer-imgbox img").parent("p").css({
                    "transform":"rotate("+ spin_n +"deg)",
                    "-moz-transform":"rotate("+ spin_n +"deg)",
                    "-ms-transform":"rotate("+ spin_n +"deg)",
                    "-o-transform":"rotate("+ spin_n +"deg)",
                    "-webkit-transform":"rotate("+ spin_n +"deg)"
                });
            });
            //关闭
            $(".mask-close").click(function () {
                $(".mask-layer").remove();
            });
            $(".mask-layer-black").click(function () {
                $(".mask-layer").remove();
            });
        }
        showImgs();

        //下一张
        $(".mask-next").on("click", function () {
            $(".mask-layer-imgbox p img").remove();
            num++;
            if (num == len) {
                num = 0;
            }
            showImgs();
        });
        //上一张
        $(".mask-prev").on("click", function () {
            $(".mask-layer-imgbox p img").remove();
            num--;
            if (num == -1) {
                num = len - 1;
            }
            showImgs();
        });
    })
});
}


//区别点击js
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
            // searchStroke();
        },
        fail : function () {
        }
    });
}

//组别点击js
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
            }
          //  searchStroke();
        },
        fail : function () {

        }
    });
}

//经办人点击js
function searchStroke() {
    var searchType = $("#searchType").val();
    var data = $("#missionStroke").serialize();
    bodymask("請銷后...");
    if (searchType == "searchByMonth") {
        var data = $("#missionStroke").serialize();
        var queryArr = data.split("&");
        var params = "";
        for (var i = 0; i < queryArr.length; i++) {
            var param = queryArr[i];
            if (param.indexOf("appointmentDate") != -1) {
                var year = $("#_select_date").attr("_select_year") + "";
                var month = $("#_select_date").attr("_select_month") + "";
                if (month.length == 1) {
                    month = "0" + month;
                }
                param = "appointmentDate=" + year + "-" + month;
            }
            if (i == 0) {
                params = params + param;
            } else {
                params = params + "&" + param;
            }
        }
        searchStrokeByMonth(params)

    } else {
        $.ajax({
            url: context_path + "/missionStroke/searchStroke",
            data: data,
            type : "POST",
            dataType : "HTML",
            success : function (result) {
                $("#strokeDetialList").empty();
                $("#strokeDetialList").html(result);
                bodyunmask();
            },
            fail : function () {
                bodyunmask();
            }
        });
    }

}





function showPhoto(photoSrc) {

    $("#photo_index").attr("src",photoSrc);
    //图片居中显示
    var box_width = 525; //图片盒子宽度
    var box_height = 300;//图片高度高度
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

function backPage(isEdit) {
    var updateFlag = $("#updateFlag").val();
   
    if (isEdit == "1" && updateFlag == "1") {
        confirmInfo_2("提示","確定要離開頁面嗎, 離開後信息將不予保留", 300, 140, function () {
            if (localStorage.getItem("selfPage") == "Y") {
                window.location.href = context_path + "/ApplyIncom/viewApplyIncom?backFlag=-1";
            } else {
                history.go(-1);
            }
        });
    } else {
        if (localStorage.getItem("selfPage") == "Y") {
            window.location.href = context_path + "/ApplyIncom/viewApplyIncom?backFlag=-1";
        } else {
            history.go(-1);
        }
    }
    
}