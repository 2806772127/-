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
        $("#compilationNo").val(localStorage.getItem("creditCompilationNo"));
        $("#companyName").val(localStorage.getItem("creditCompanyName"));
        $("#startDate").val(localStorage.getItem("creditStartDate"));
        $("#endDate").val(localStorage.getItem("creditEndDate"));
        $("#areaCode").val(localStorage.getItem("creditAreaCode"));
        $("#groupCode").val(localStorage.getItem("creditGroupCode"));
        $("#userCode").val(localStorage.getItem("creditUserCode"));

        curPage = localStorage.getItem("creditCurPage");
        // 清除所有缓存
        localStorage.clear();
    }

    queryNcoms(curPage);
 });


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
            //searchStroke();
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
            };
         //   searchStroke();
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

function  queryNcoms(curPage) {

    if(curPage!="" && curPage!='undefined'){
        curPage = curPage;
    }else{
        curPage = 1;
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
        url: context_path + "/CreditReport/queryCreditReport",
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

    // 缓存查询条件
    localStorage.setItem("creditCompilationNo", $("#compilationNo").val());
    localStorage.setItem("creditCompanyName", $("#companyName").val());
    localStorage.setItem("creditStartDate", $("#startDate").val());
    localStorage.setItem("creditEndDate", $("#endDate").val());
    localStorage.setItem("creditAreaCode", $("#areaCode").val());
    localStorage.setItem("creditGroupCode", $("#groupCode").val());
    localStorage.setItem("creditUserCode", $("#userCode").val());
    localStorage.setItem("creditCurPage", $("#page_select").val());
    localStorage.setItem("selfPage", "Y");

    var reportingId = $(obj).attr("reportingId");
    var trandId = $(obj).attr("trandId");
    var compilationNo = $(obj).attr("compilationNo");
    var reporType = $(obj).attr("reporType");
    var industryType = $(obj).attr("industryType");
    window.location.href = context_path + "/CreditReport/seeCreditRepor?compilationNo="+compilationNo+"&trandId="+trandId+"&flag="+reporType+"&industryType="+industryType+"&reportingId="+reportingId;

}

function  editIncomDetail(obj) {

    // 缓存查询条件
    localStorage.setItem("creditCompilationNo", $("#compilationNo").val());
    localStorage.setItem("creditCompanyName", $("#companyName").val());
    localStorage.setItem("creditStartDate", $("#startDate").val());
    localStorage.setItem("creditEndDate", $("#endDate").val());
    localStorage.setItem("creditAreaCode", $("#areaCode").val());
    localStorage.setItem("creditGroupCode", $("#groupCode").val());
    localStorage.setItem("creditUserCode", $("#userCode").val());
    localStorage.setItem("creditCurPage", $("#page_select").val());
    localStorage.setItem("selfPage", "Y");

    var reportingId = $(obj).attr("reportingId");
    var trandId = $(obj).attr("trandId");
    var compilationNo = $(obj).attr("compilationNo");
    var reporType = $(obj).attr("reporType");
    var industryType = $(obj).attr("industryType");
    window.location.href = context_path + "/CreditReport/editCreditRepor?compilationNo="+compilationNo+"&trandId="+trandId+"&flag="+reporType+"&industryType="+industryType+"&reportingId="+reportingId;

}

// function  changClass(flag) {
//
//     if(flag =='1'){
//         $("#new").attr("class","gpic");
//         $("#old").attr("class","pic");
//     }else{
//         $("#old").attr("class","gpic");
//         $("#new").attr("class","pic");
//     }
//
// }


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
        url :context_path + "/CreditReport/showView",
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
            }else{
                openDialog("musicorimg","圖片文件",650,665);
                $("#phName").val(imgName);
                $("#phDate").val(imgDate);
                showPhoto(filesrc);
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

/*
function deleteFile(obj){

    if (confirm("您確定刪除嗎？")) {
        var imgId = $(obj).attr("imgId");
        $.ajax({
            url: context_path + "/CreditReport/deleteFilsFlag",
            type: "post",
            data: {"id": imgId},
            datatype: "json",
            success: function (re) {
                alert(re);
                if(re=="success"){
                   var de =  $("#deleteimgIds").val();
                   de += imgId +",";
                   $("#deleteimgIds").val(de);
                }
            }, error: function (XMLHttpRequest, textStatus, errorThrown) {
                alert(textStatus + " " + XMLHttpRequest.readyState + " "
                    + XMLHttpRequest.status + " " + errorThrown);
            }
        });
    }else{

    }

}
*/

function deleteFile(obj){
    confirmInfo_1("提示","您確定刪除嗎",function () {
        var imgId = $(obj).attr("imgId");
        var de =  $("#deleteimgIds").val();
        de += imgId +",";
        $("#deleteimgIds").val(de);
        $("#"+imgId+"_1").hide();
        $("#updateFlag").val("1");
        document.getElementById(imgId+"_2").style.display = "none";
        alertInfo("提示","刪除成功！");
    });
}


function showUpL(){
    openDialog("uploanFile", "新增附件", 500, 250);
    $("#documentUpload").val('');
   // $("#checkItemCode").val("请选择");
   // $("#fileFaName").val("");
    $("#checkItemCode option[value='']").prop("selected",true);
    $("#fileFaName option[value='']").prop("selected",true);
}

function  changClass(flag) {
    if (flag == '2') {
        $("#photoAttach").attr("class", "vsb");
        $("#voiceAttach").attr("class", "vsw");
        $("#fileType").val("2");
        $("#fCode").show();
        $("#fName").show();
    } else {
        $("#voiceAttach").attr("class", "vsb");
        $("#photoAttach").attr("class", "vsw");
        $("#fileType").val("1");
        $("#fCode").hide();
        $("#fName").hide();
    }
    var hasFile = document.getElementById("documentUpload").files;
    if (hasFile != null && hasFile != "" && hasFile.length>0){
        checkfile();
    }
}


/** 判断上传文件格* */
function checkfile() {
    var fname = document.getElementById('documentUpload').value;
    var fileText = fname.substring(fname.lastIndexOf("."), fname.length); // 获取文件扩展名
    fileText = fileText.toLowerCase();
    var fileType = $("#fileType").val();
    var flag =  checkFileExt(fileType,fileText);
    if (!flag) {
        /*document.getElementById("submitimport").disabled = true;*/
        alertInfo("提示",'附件格式不支持，請重新選擇',null);
        document.getElementById('documentUpload').focus();

    } else {
        document.getElementById("submitimport").disabled = false;
    }
    return flag;
}

function generateUUID() {
    var d = new Date().getTime();
    var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        var r = (d + Math.random()*16)%16 | 0;
        d = Math.floor(d/16);
        return (c=='x' ? r : (r&0x3|0x8)).toString(16);
    });
    uuid=uuid.replace(/[-]/g,"");
    return uuid;
};


function formsum(){
    var fileTexts="jpeg";
    if(typeof FileReader == 'undefined'){
        alert("未支持該版本瀏覽器附件上傳，請更新瀏覽器或更換瀏覽器");
        return false;
    }

    var complicationNo=$("#compilationNote").val();
    var trandId = $("#trandId").val();

    var file_max_size = 1024*1024*10;

    var fileType = $("#fileType").val();//附件类型
    var fileFacode = $("#checkItemCode").find("option:selected").val();//附件类型
    var fileFaName = $("#checkItemCode").find("option:selected").text();
    var fileChCode = $("#fileFaName").find("option:selected").val();//附件名称
    var fileChName = $("#fileFaName").find("option:selected").text();

    if("2"==fileType && (fileFacode==""|| fileFacode==null ||fileFacode=="null")){
        alertInfo("提示","請選擇附件類型!");
        return false;
    }else if("2"==fileType && (fileChCode==""|| fileChCode==null ||fileChCode=="null")){
        alertInfo("提示","請選擇附件名稱!");
        return false;
    }
    var hasFile = document.getElementById("documentUpload").files;
    if (hasFile == null||hasFile == ""||hasFile.length==0){
        alertInfo("提示","請選擇上傳文件!");
        return false;
    }

    if (trandId == null || trandId == "" || trandId == "undefined" || trandId == undefined) {
        alertInfo_2("提示", "此案尚未提交訪談內容，暫不可新增附件!", "300", "150");
        return false;
    }
    if(!checkfile()){
        return false;
    }
    var fname = document.getElementById('documentUpload').value;
    var fileText = fname.substring(fname.lastIndexOf("."), fname.length);

    if (fileText.match(/.pdf/i))
    {
        fileTexts="pdf"
    }

    var fileSize = hasFile[0].size;
    if("1"==fileType){
        fileChName =hasFile[0].name;
    }
    if(file_max_size<fileSize){
        alertInfo("提示","請選擇10M以內文件！");
        return false;
    }else if(fileSize == 0){
        alertInfo("提示","附件內容為空，請重新選擇！");
        return false;
    }
    var file = document.getElementById("documentUpload").files[0];
    $("#submitimport").removeAttr("onclick");
    var reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = function (e) {
        // bodymask("上傳中...");
        var filepath = document.getElementById('documentUpload').value;
        var fileName = filepath.replace(/^.+?\\([^\\]+?)(\.[^\.\\]*?)?$/gi,"$1");
        if("1"==fileType){
            fileChName=fileName;
            fileFacode = "MIC";
            fileChCode = "1";
        }

        var fileStrs = new Array();
        var uuid = generateUUID();
        fileStrs[0]=uuid+"|"+fileType+"|"+fileFacode+"|"+fileFaName+"|"+fileChCode+"|"+fileChName+
            "|"+complicationNo+"|"+trandId+"|"+fileTexts;
        fileStrs[1] = e.target.result;
        $.ajax({
            url :context_path + "/CreditReport/upload",
            type: "post",
            contentType : "application/json" ,
            data: JSON.stringify(fileStrs),
            success :function (result) {
              $("#submitimport").attr("onclick","formsum();");
                bodyunmask();
                if(result.flag){
                    var imgIds = $("#imgIds").val();
                    imgIds = imgIds+uuid + ',';
                    $("#imgIds").val(imgIds);
                    $("#updateFlag").val("1");
                    var respDate = result.respDate;
                    addAttachToHtml(uuid,fileType,fileFacode,fileName,fileFaName,fileChName,respDate,fileChCode,fileTexts);

                    alertInfo("提示",result.msg,function () {;

                        document.getElementById('documentUpload').focus();
                        $("#uploanFile").dialog('close');



                        //setTimeout(function () { window.location.reload() }, 2000);
                    });
                }else{
                    $("#submitimport").attr("onclick","formsum();");
                    alertInfo("提示",result.msg);
                }
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                bodyunmask();
                $("#submitimport").attr("onclick","formsum();");
            }
        });
    }

}
function closeWindow(id) {
    $("#"+id).dialog('close');
}

function changeName(){
    var fileFacode = $("#checkItemCode").find("option:selected").val();
    var industryType = $("#industryType_").val();
    $.ajax({
        url: context_path + "/CreditReport/changeName", // 需要链接到服务器地址
        type: "post",
        dataType: 'html', // 服务器返回的格式，可以是json
        data: {"industryType":industryType,"fileFacode":fileFacode},
        success: function(result) {
            if(result!= null){
                $("#fileFaName").empty();//清空select列表数据
                $("#fileFaName").append(result);
            }
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
        },
    });
}

function  saveImg() {
    var imgIds = $("#imgIds").val();
    var deleImgIds =  $("#deleteimgIds").val();
    var trandId = $("#trandId").val();
    var compilationNo = $("#compilationNo").val()
    var curPage = $("#curPage").val();
    if(imgIds.length==1 && deleImgIds.length == 1){
        alertInfo("提示","未做任何修改，返回查詢頁面",function () {
            /*  window.location.href=context_path+"/collectionQuery/queryCollectionRecords";*/
            window.location.href=context_path+"/CreditReport/viewCreditReport";
        })
    }
    $.ajax({
        url: context_path + "/CreditReport/updateCreditReport",
        type: "post",
        secureuri: false,
        dataType: 'JSON',
        data: {"imgIds":imgIds,"trandId":trandId,"compilationNo":compilationNo,"deleImgIds":deleImgIds,"curPage":curPage},
        success: function(result) {
            if(result.flag){
                $("#updateFlag").val("0");
                alertInfo("提示","儲存成功",function () {
//                    window.location.href=context_path+"/CreditReport/viewCreditReport?backFlag=-1";
                });
            }else{
                alertInfo("提示",result.msg);
            }
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
        },
    });

}

function  findPage(curPage) {
    queryNcoms(curPage);
}

function openPunchCardRecords(){
    openDialog("punchCardWindow", "打卡歷史", 600, 280);
}

function openTaskDetail() {
    /*openDialog("taskDetailWindow", "查看任務行程", 650, 400);*/
    openDialog("taskDetailWindow", "查看任務行程", 700, 400);
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
function getNowDate(){
    var myDate = new Date();
    var date = myDate.getFullYear()+"-"+(myDate.getMonth()+1)+"-"+myDate.getDate() +" "
        +myDate.getHours()+":"+myDate.getMinutes()+":"+myDate.getSeconds();
    return date;
}

//imgId==uuid  imgType==fileType  fileFaName附件类型
function addAttachToHtml(uuid,fileType,fileFacode,fileName,fileFaName,fileChName,respDate,fileChCode,fileTexts) {
    var alink = document.createElement("a");
    var myDate = new Date();
    /*var datestr = myDate.getFullYear()+""+(myDate.getMonth()+1)+""+myDate.getDate();*/
    var datestr = myDate.getFullYear()+""+"0"+(myDate.getMonth()+1)+""+myDate.getDate();
    if("2"==fileType) {
        if(fileTexts=="pdf") {
            var fileName = fileChName + "_" + datestr + ".pdf";
        }else{
            var fileName = fileChName + "_" + datestr + ".jpeg";
        }

    }
    alink.setAttribute("href","#");
    alink.setAttribute("imgId",uuid);
    alink.setAttribute("imgName",fileName);
    alink.setAttribute("imgType","2");
    alink.setAttribute("imgDate",respDate);
    alink.setAttribute("id",uuid+"_1");
    alink.setAttribute("onclick","showImg(this)");
    alink.innerText = fileName;
    var imglink = document.createElement("img");
    imglink.setAttribute("src","../images/common/delete.png");
    imglink.setAttribute("imgId",uuid);
    imglink.setAttribute("id",uuid+"_2");
    imglink.setAttribute("onclick","deleteFile(this)");
    imglink.setAttribute("style","width:10px;height:10px;");


 /*   var imglink = document.createElement("img");
    imglink.setAttribute("src","../images/common/delete.png");
    imglink.setAttribute("imgName",fileName);
    imglink.setAttribute("imgId",uuid);
    imglink.setAttribute("onclick","deleteFile(this)");*/
    var spans = document.createElement("span");
    spans.setAttribute("style","width:10px;height:10px;text-align: center;");
    spans.innerText=" ";
    spans.innerHTML += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";


    if("2"==fileType){//photo
        var attachTypeCodeAndName = fileFacode + fileFaName
        if($("#"+attachTypeCodeAndName).length>0){//检查项存在
            var photocode = document.getElementById(attachTypeCodeAndName+"_1");
            photocode.appendChild(spans);
            photocode.appendChild(alink);
            photocode.appendChild(imglink);


        }else{
            var photoTb = document.getElementById("attach_photo_list");
            var tr1 = document.createElement("tr");
            var th1 = document.createElement("th");
            th1.setAttribute("id",attachTypeCodeAndName);
            th1.innerHTML=fileFaName + "&nbsp;&nbsp;&nbsp;&nbsp;";
            th1.setAttribute("width","16%");
            var td1 = document.createElement("td");
            td1.setAttribute("id",attachTypeCodeAndName+"_1");
            td1.appendChild(spans);
            td1.appendChild(alink);
            td1.appendChild(imglink);
            tr1.appendChild(th1);
            tr1.appendChild(td1);

            photoTb.appendChild(tr1);
        }

    }else if("1"== fileType){//voice
     /*   var voicelist = document.getElementById("attach_voice_list");
        voicelist.appendChild(alink);
        voicelist.appendChild(imglink);*/
        var voiceA = document.createElement("a");
        voiceA.setAttribute("href","#");
        voiceA.setAttribute("imgName",fileChName);
        voiceA.setAttribute("imgDate",respDate);
        voiceA.setAttribute("imgId",uuid);
        voiceA.setAttribute("id",uuid+"_1");
        voiceA.setAttribute("imgType","1");
        voiceA.setAttribute("onclick","showImg(this)");
        voiceA.innerText = fileName;
        var voiceTb = document.getElementById("attach_voice_list");
        var imglink = document.createElement("img");
        imglink.setAttribute("src","../images/common/delete.png");
        imglink.setAttribute("imgId",uuid);
        imglink.setAttribute("id",uuid+"_2");
        imglink.setAttribute("style","width:10px;height:10px;text-align: center;");
        imglink.setAttribute("onclick","deleteFile(this)");
        var voiceLength = $("#voiceLength").val();
        var yushu = voiceLength%1;
        if("0"==voiceLength || voiceLength ==""){
            var tr1 = document.createElement("tr");
            var td1 = document.createElement("td");
            td1.setAttribute("id","voice_td_list");
            td1.setAttribute("style","text-align: center;");
            td1.appendChild(spans);
            td1.appendChild(voiceA);
            td1.appendChild(imglink);
            tr1.appendChild(td1);
            voiceTb.appendChild(tr1);
            $("#voiceLength").val(1);
        }else{
            var voicetd =  document.getElementById("voice_td_list");
            document.getElementById("voice_td_list").innerHTML+"" ;
            voicetd.appendChild(spans);
            voicetd.appendChild(voiceA);
            voicetd.appendChild(imglink);
            var insertlength= parseInt(voiceLength)+parseInt("1");
            $("#voiceLength").val(insertlength);
        }
    }
}

/*function addAttachToHtml(uuid,fileType,fileFacode,fileName,fileFaName,fileChName,respDate,fileChCode) {

    if("2"==fileType){//photo
        var myDate = new Date();
        var datestr = myDate.getFullYear()+""+(myDate.getMonth()+1)+""+myDate.getDate();
        var alink = document.createElement("a");
        var filename = fileChName+"_"+datestr+".jpeg"
        alink.setAttribute("href","#");
        alink.setAttribute("imgId",uuid);
        alink.setAttribute("imgName",filename);
        alink.setAttribute("imgType","2");
        alink.setAttribute("imgDate",respDate);
        alink.setAttribute("id",uuid+"_1");
        alink.setAttribute("onclick","showImg(this)");
        alink.innerText = filename;

        var imglink = document.createElement("img");
        imglink.setAttribute("src","../images/common/delete.png");
        imglink.setAttribute("imgId",uuid);
        imglink.setAttribute("id",uuid+"_2");
        imglink.setAttribute("onclick","deleteFile(this)");
        imglink.setAttribute("style","width:10px;height:10px;");
        if($("#"+fileFacode).length>0){//检查项存在
            var photocode = document.getElementById(fileFacode+"_1");
            document.getElementById(fileFacode+"_1").innerHTML+="&nbsp;&nbsp;&nbsp;&nbsp;"
            photocode.appendChild(alink);
            photocode.appendChild(imglink);
        }else{
            var photoTb = document.getElementById("attach_photo_list");
            var tr1 = document.createElement("tr");
            var th1 = document.createElement("th");
            th1.setAttribute("id",fileFacode);
            th1.innerHTML=fileFaName;
            th1.setAttribute("width","16%");
            var td1 = document.createElement("td");
            td1.setAttribute("id",fileFacode+"_1");
            td1.appendChild(alink);
            td1.appendChild(imglink);
            tr1.appendChild(th1);
            tr1.appendChild(td1);
            console.log(tr1);
            photoTb.appendChild(tr1);
        }
    }else if("1"== fileType){//voice
        var voiceA = document.createElement("a");
        voiceA.setAttribute("href","#");
        voiceA.setAttribute("imgName",fileChName);
        voiceA.setAttribute("imgDate",respDate);
        voiceA.setAttribute("imgId",uuid);
        voiceA.setAttribute("id",uuid+"_1");
        voiceA.setAttribute("imgType","1");
        voiceA.setAttribute("onclick","showImg(this)");
        voiceA.innerText = fileName;
        var voiceTb = document.getElementById("attach_voice_list");
        var imglink = document.createElement("img");
        imglink.setAttribute("src","../images/common/delete.png");
        imglink.setAttribute("imgId",uuid);
        imglink.setAttribute("id",uuid+"_2");
        imglink.setAttribute("style","width:10px;height:10px;");
        imglink.setAttribute("onclick","deleteFile(this)");
        var voiceLength = $("#voiceLength").val();
        var yushu = voiceLength%1;
        if("0"==voiceLength || voiceLength ==""){
            var tr1 = document.createElement("tr");
            var td1 = document.createElement("td");
            td1.setAttribute("id","voice_td_list");
            td1.appendChild(voiceA);
            td1.appendChild(imglink);
            tr1.appendChild(td1);
            voiceTb.appendChild(tr1);
            $("#voiceLength").val(1);
        }else{
            var voicetd =  document.getElementById("voice_td_list");
            document.getElementById("voice_td_list").innerHTML +="&nbsp;&nbsp;&nbsp;&nbsp;";
            voicetd.appendChild(voiceA);
            voicetd.appendChild(imglink);
            var insertlength= parseInt(voiceLength)+parseInt("1");
            $("#voiceLength").val(insertlength);
        }
    }
}*/

function checkFileExt(type,ext) {
    if ("2" == type &&!ext.match(/.jpg|.gif|.pdf|.png|.bmp/i)) {
        return false;
    }else if ("1" == type &&!ext.match(/.wav|.mp3|.wma|.asf|.aac/i)) {
        return false;
    }
    return true;
}

function checkFileIdExsit(id,fileType,fileFacode,fileName,fileFaName,fileChName) {
    $.ajax({
        url: context_path + "/CreditReport/checkFileIdExsit",
        type: "post",
        secureuri: false,
        data: {"id":id},
        success: function(result) {
            if(result.flag){
                var imgIds = $("#imgIds").val();
                imgIds += id + ",";
                $("#imgIds").val(imgIds);
                addAttachToHtml(id,fileType,fileFacode,fileName,fileFaName,fileChName);
                alertInfo("提示","儲存成功！",function () {;
                    $("#uploanFile").dialog('close');
                });
            }else{
                alertInfo("提示","儲存失敗！");
            }
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            return false;
        },
    });
}

function backPage(isEdit) {
    var updateFlag = $("#updateFlag").val();

    if (isEdit == "1" && updateFlag == "1") {
        confirmInfo_2("提示","確定要離開頁面嗎, 離開後信息將不予保留", 300, 140, function () {
            if (localStorage.getItem("selfPage") == "Y") {
                window.location.href = context_path + "/CreditReport/viewCreditReport?backFlag=-1";
            } else {
                history.go(-1);
            }
        });
    } else {
        if (localStorage.getItem("selfPage") == "Y") {
            window.location.href = context_path + "/CreditReport/viewCreditReport?backFlag=-1";
        } else {
            history.go(-1);
        }
    }

}