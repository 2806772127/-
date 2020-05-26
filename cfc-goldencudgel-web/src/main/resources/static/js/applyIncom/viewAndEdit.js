
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
function openPunchCardRecords(){
    openDialog("punchCardWindow", "打卡歷史", 600, 280);
}
/**z
 * 关闭弹窗
 */
function closeWindow(id) {
    $("#"+id).dialog('close');
}

function openTaskDetail() {
    openDialog("taskDetailWindow", "查看任務行程", 650, 400);
}

function showLocationDetail(flag) {
    if(flag=='1'){
        locationTime = $("#startLocaTime").val();
        locationAddress = $("#startLocaAddress").val();
        longitude = $("#startLongitude").val();
        latitude =  $("#startLatitude").val();
    }else if(flag=='2'){
        locationTime = $("#endLocaTime").val();
        locationAddress = $("#endLacaAddress").val();
        longitude = $("#endLongitude").val();
        latitude =  $("#endtLatitude").val();
    }else{
        return ;
    }
    $("#dw_time").val(locationTime);
    $("#dw_address").val(locationAddress);
    openDialog("dingwei_location", "查看簽到地圖", 600, 550);

    if (GBrowserIsCompatible()){
        var map = new GMap2(document.getElementById("map_canvas"));
        map.addControl(new GSmallMapControl());
        map.addControl(new GMapTypeControl());
        map.setCenter(new GLatLng(latitude, longitude),15);
        // Create our "tiny" marker icon
        var baseIcon = new GIcon();
        baseIcon.iconSize = new GSize(20,25);
        baseIcon.iconAnchor = new GPoint(0, 0);
        baseIcon.infoWindowAnchor = new GPoint(9, 2);
        baseIcon.infoShadowAnchor = new GPoint(18, 25);

        function createMarker(point) {
            var icon = new GIcon(baseIcon);
            icon.image = "../images/visit/u3988.png";
            var marker = new GMarker(point, icon);

            GEvent.addListener(marker, "click", function() {
                marker.openInfoWindowHtml(locationAddress);
            });
            return marker;
        }
        var point = new GLatLng(latitude, longitude);
        map.addOverlay(createMarker(point));
    }
}

function showUpL(){
    openDialog("uploanFile", "新增附件", 500, 250);
    $("#documentUpload").val('');
    $("#checkItemCode").val("");
    $("#fileFaName").val("");
    document.getElementById('documentUpload').focus();
}

function changeAttach(flag) {
    if(flag =='2'){
        $("#photoAttach").attr("class","vsb");
        $("#voiceAttach").attr("class","vsw");
        $("#otherAttach").attr("class","vsw");
        $("#fileType").val("2");
        $("#fCode").show();
        $("#fName").show();
    }else if(flag =='1') {
        $("#voiceAttach").attr("class","vsb");
        $("#photoAttach").attr("class","vsw");
        $("#otherAttach").attr("class","vsw");
        $("#fileType").val("1");
        $("#fCode").hide();
        $("#fName").hide();
    } else {
        $("#voiceAttach").attr("class","vsw");
        $("#photoAttach").attr("class","vsw");
        $("#otherAttach").attr("class","vsb");
        $("#fileType").val("9");
        $("#fCode").hide();
        $("#fName").hide();
    }
    var hasFile = document.getElementById("documentUpload").files;
    if (hasFile != null && hasFile != "" && hasFile.length>0){
        checkfile();
    }
}

function changeAttachName() {
    var checkItem  = $("#checkItemCode").find("option:selected").val();
    if(checkItem==null || checkItem =="null" ||checkItem=="undifined" ||checkItem==""){
        return;
    }
    $.ajax({
        url :context_path + "/ApplyIncom/changeAttachName",
        type: "post",
        data:{
            "checkItem":checkItem,
            "compCode":$("#compilationNote").val(),
            "comIndustryCode":$("#comIndustryCode").val()
        },
        success :function (result) {
            $("#fileFaName").find("option").remove();
            var select = $("select[name='fileFaName']");
            select.append(result);

        },error: function(XMLHttpRequest, textStatus, errorThrown) {
            alert(textStatus + " " + XMLHttpRequest.readyState + " "
                + XMLHttpRequest.status + " " + errorThrown);
        }
    });
}

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
    if("1"==fileType || "99"==fileType){
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
        if("1"==fileType){ //錄音
            fileChName=fileName;
            fileFacode = "MIC";
            fileChCode = "1";
            fileFaName = "錄音";
        } else if("9"==fileType) {
            fileChName=fileName;
            fileFacode = filepath.substring(filepath.lastIndexOf(".")+1, filepath.length).toUpperCase();
            fileChCode = "9";
            fileFaName = "附加檔案";
        }
        var fileStrs = new Array();
        var uuid = generateUUID();
        fileStrs[0]=uuid+"|"+fileType+"|"+fileFacode+"|"+fileFaName+"|"+fileChCode+"|"+fileChName+
            "|"+complicationNo+"|"+trandId+"|"+fileTexts;
        fileStrs[1] = e.target.result;
        $.ajax({
            url :context_path + "/ApplyIncom/upload",
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
                        /*document.getElementById('documentUpload').focus();*/
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
//imgId==uuid  imgType==fileType  fileFaName附件类型
function addAttachToHtml(uuid,fileType,fileFacode,fileName,fileFaName,fileChName,respDate,fileChCode,fileTexts) {

    var imglink = document.createElement("img");
    imglink.setAttribute("src","../images/common/delete.png");
    imglink.setAttribute("imgId",uuid);
    imglink.setAttribute("id",uuid+"_2");
    imglink.setAttribute("style","width:10px;height:10px;text-align: center;");
    imglink.setAttribute("onclick","deleteFile(this)");

    if("2"==fileType){//photo
        var myDate = new Date();
        /*var datestr = myDate.getFullYear()+""+(myDate.getMonth()+1)+""+myDate.getDate();*/
        var datestr = myDate.getFullYear()+""+"0"+(myDate.getMonth()+1)+""+myDate.getDate();
        var alink = document.createElement("a");
        if(fileTexts=="pdf"){
            var filename = fileChName+"_"+datestr+".pdf"

            alink.setAttribute("href","/fileService/"+uuid);
            alink.setAttribute("imgId",uuid);
            alink.setAttribute("imgName",filename);
            alink.setAttribute("imgType","2");
            alink.setAttribute("imgDate",respDate);
            alink.setAttribute("id",uuid+"_1");
            alink.setAttribute("target", "_blank");
        }else{
            var filename = fileChName+"_"+datestr+".jpeg"
            alink.setAttribute("href","#");
            alink.setAttribute("imgId",uuid);
            alink.setAttribute("imgName",filename);
            alink.setAttribute("imgType","2");
            alink.setAttribute("imgDate",respDate);
            alink.setAttribute("id",uuid+"_1");
            alink.setAttribute("onclick","showImg(this)");
        }

        alink.innerText = filename;
        var fielTypeId = fileFaName.replace(/[\(\)~\r\n]/gi, "");
        if($("#"+fielTypeId).length>0 && $("#"+fielTypeId).attr("type") == fileFacode){//检查项存在
            var photocode =  document.getElementById(fielTypeId+"_1");
            photocode.innerHTML+="";
            photocode.appendChild(alink);
            photocode.appendChild(imglink);
        }else{
            var photoTb = document.getElementById("attach_photo_list");
            var tr1 = document.createElement("tr");
            var th1 = document.createElement("th");
            th1.setAttribute("id",fielTypeId);
            th1.setAttribute("type",fileFacode);
            th1.innerHTML=fileFaName;
            th1.setAttribute("width","16%");
            var td1 = document.createElement("td");
            td1.setAttribute("id",fielTypeId+"_1");
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
        var voiceLength = $("#voiceLength").val();
        var yushu = voiceLength%1;
        if("0"==voiceLength || voiceLength ==""){
            var tr1 = document.createElement("tr");
            var td1 = document.createElement("td");
            td1.setAttribute("id","voice_td_list");
            td1.setAttribute("style","text-align: center;");
            td1.appendChild(voiceA);
            td1.appendChild(imglink);
            tr1.appendChild(td1);
            voiceTb.appendChild(tr1);
            $("#voiceLength").val(1);
        }else{
            var voicetd =  document.getElementById("voice_td_list");
            document.getElementById("attach_voice_list").innerHTML ;
            voicetd.appendChild(voiceA);
            voicetd.appendChild(imglink);
            var insertlength= parseInt(voiceLength)+parseInt("1");
            $("#voiceLength").val(insertlength);
        }
    }else{//other
        var otherA = document.createElement("a");
        otherA.setAttribute("href","/fileService/"+uuid);
        otherA.setAttribute("imgName",fileChName);
        otherA.setAttribute("imgDate",respDate);
        otherA.setAttribute("imgId",uuid);
        otherA.setAttribute("id",uuid+"_1");
        otherA.setAttribute("imgType","9");
        otherA.setAttribute("target","_blank");
        otherA.innerText = fileName;
        var otherTb = document.getElementById("attach_other_list");
        var otherTd = document.getElementById("other_td_list");
        if($("#other_td_list").length <= 0) {
            var tr1 = document.createElement("tr");
            var td1 = document.createElement("td");
            td1.setAttribute("id","other_td_list");
            td1.setAttribute("style","text-align: center;");
            td1.appendChild(otherA);
            td1.innerHTML +=" ";
            td1.appendChild(imglink);
            tr1.appendChild(td1);
            otherTb.appendChild(tr1);
        } else {
            otherTd.appendChild(otherA);
            otherTd.innerHTML +=" ";
            otherTd.appendChild(imglink);
        }
        document.getElementById("other_td_list").innerHTML +="    ";
    }
}

function checkFileExt(type,ext) {
    if ("2" == type &&!ext.match(/.jpg|.gif|.pdf|.png|.bmp/i)) {
        return false;
    }else if ("1" == type &&!ext.match(/.wav|.mp3|.wma|.asf|.aac/i)) {
        return false;
    }
    return true;
}

    function  saveImg() {
        var imgIds = $("#imgIds").val();
        var deleImgIds =  $("#deleteimgIds").val();
        var trandId = $("#trandId").val();
        console.log(imgIds);
        var compilationNo = $("#compilationNote").val();
        var applyNumber = $("#applyNumber").val();
        var curPage = $("#curPage").val();

        if(imgIds.length==1 && deleImgIds.length == 1){
            alertInfo("提示","未做任何修改，返回查詢頁面",function () {
                window.location.href=context_path + "/ApplyIncom/viewApplyIncom?backFlag=-1";
            })
        }
        if(imgIds.length>1||deleImgIds.length>1){
            $.ajax({
                url: context_path + "/ApplyIncom/updateCreditReport",
                type: "post",
                secureuri: false,
                dataType: 'JSON',
                data: {"imgIds":imgIds,"trandId":trandId,"compilationNo":compilationNo,"deleImgIds":deleImgIds,"applyNumber":applyNumber},
                success: function(result) {
                    if(result.flag){
                        $("#updateFlag").val("0");
                        alertInfo("提示","儲存成功",function () {
//                            window.location.href=context_path + "/ApplyIncom/viewApplyIncom?backFlag=-1";
                        });
                    }else{
                        alertInfo("提示", result.msg);
                    }
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                },
            });
        }
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

function getNowDate(){
    var myDate = new Date();
    var date = myDate.getFullYear()+"-"+(myDate.getMonth()+1)+"-"+myDate.getDate() +" "
        +myDate.getHours()+":"+myDate.getMinutes()+":"+myDate.getSeconds();
    return date;
}

function closeImg(id){
    $("#"+id).dialog('close');
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

function backfunc() {

/*    var curPage = $("#curPage2").val();
	 //var curPage =  $(obj).attr("curPage");
	
    var compilationNo = $("#compilationNoK2").val();
    var visitName = $("#visitNameK2").val();
    var startDate = $("#startDateK2").val();
    var endDate = $("#endDateK2").val();
    var sy=$("#sy").val();

    $.ajax({

        url:context_path + "/collectionQuery/viewQueryVisit",
        type:"post",
        data:{"compilationNo":compilationNo,"visitName":visitName,
            "startDate":startDate,"endDate":endDate,"curPage":curPage},
        datatype: "html",

        success: function(result) {
        	if (result) {
        		if (sy==null||sy==""||sy=="undefined"||sy==undefined) {
        			window.location.href=context_path + "/collectionQuery/viewQueryVisitt?curPage="+curPage+"&compilationNo="+compilationNo+"&visitName="+visitName+"&startDate="+startDate+"&endDate="+endDate;
            //$("#collectionQuery_list").empty();
        	//$("#collectionQuery_list").html(result);
        		} else {

        		}
        	}
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            alert(textStatus + " " + XMLHttpRequest.readyState + " "
                + XMLHttpRequest.status + " " + errorThrown);
        }
    });*/
    javascript:history.go(-1);
}
