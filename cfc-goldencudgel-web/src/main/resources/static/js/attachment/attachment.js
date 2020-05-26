$(function () {
    var backFlag = $("#backFlag").val();
    var curPage = 1;
    if ("-1" == backFlag) {
        $("#dictId").val(localStorage.getItem("attachmentDictId"));

        var jieCode = localStorage.getItem("attachmenJieCode");
        $("#jieCode").val(jieCode);
        var nodeCodeInput = $("input[nodeCode='" + jieCode + "']");
        $(".gpic").attr("class", "pic");
        $(nodeCodeInput).attr("class", "gpic");
        if ("1" == jieCode) {
            $("#intyId").hide();
        } else {
            $("#intyId").show();
        }
        curPage = localStorage.getItem("attachmenCurPage");
        // 清除所有缓存
        localStorage.clear();
    }
    findRole(curPage);
    /* c*/
})

function  findRole(curPage) {
    //var data = $("#viewData_form").serialize();
    if(curPage!="" && curPage!='undefined'){
        curPage = curPage;
    }else{
        curPage = 1;
    }
    var industryType  = $("#dictId option:selected").val();
    var nodeCode = $("#jieCode").val();
    console.log(industryType);
    console.log(nodeCode);

    $.ajax({
        url:context_path +"/attachment/queryAttachment",
        type:'post',
        data: {"industryType":industryType,"nodeCode":nodeCode,"curPage":curPage},
        datatype: 'html',
        success:function (result) {
            $("#dataListDiv").empty();
            $("#dataListDiv").html(result);
            if(nodeCode==1){
                $(".category").hide();
            }else{
                $(".category").show();
            }
        },
        error:function (XMLHttpRequest, textStatus, errorThrown) {
            alert(textStatus + " " + XMLHttpRequest.readyState + " "
                + XMLHttpRequest.status + " " + errorThrown);
        }
    })

}

function  findRoles(curPage) {
    //var data = $("#viewData_form").serialize();
    if(curPage!="" && curPage!='undefined'){
        curPage = curPage;
    }else{
        curPage = 1;
    }
    var industryType  = $("#dictId option:selected").val();
    var nodeCode = $("#jieCode").val();

    $.ajax({
        url:context_path +"/attachment/queryAttachment",
        type:'post',
        data: {"industryType":industryType,"nodeCode":nodeCode,"curPage":curPage},
        datatype: 'html',
        success:function (result) {
            $("#dataListDiv").empty();
            $("#dataListDiv").html(result);
            $(".category").hide();
        },
        error:function (XMLHttpRequest, textStatus, errorThrown) {
            alert(textStatus + " " + XMLHttpRequest.readyState + " "
                + XMLHttpRequest.status + " " + errorThrown);
        }
    })

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
function openQueryWin(obj){

    // 缓存查询条件
    localStorage.setItem("attachmentDictId", $("#dictId option:selected").val());
    localStorage.setItem("attachmenJieCode", $("#jieCode").val());
    localStorage.setItem("attachmenCurPage", $("#page_select").val());

    var attachTypeId = $(obj).attr("attachTypeId");
    var attachTypeName = $(obj).attr("attachTypeName");
    var nodeCode = $(obj).attr("nodeCode");
    var nodeName = $(obj).attr("nodeName");
    var industryType = $(obj).attr("industryType");
    var attachTypeCode = $(obj).attr("attachTypeCode");
    var isEnable = $(obj).attr("isEnable");
    var isRequired = $(obj).attr("isRequired");
    var attachNameCode = $(obj).attr("attachNameCode");
    var attachName = $(obj).attr("attachName");
    var changeshowOrder = $(obj).attr("showOrder");

    $("#changeshowOrder").val(changeshowOrder);
    $("#changeAttachTypeId").val(attachTypeId);
    $("#changeNodeCode").val(nodeCode);
    $("#changeIndustryType").val(industryType);
    $("#changeAttachTypeCode").val(attachTypeCode);
    $("#changeNodeName").val(nodeName);
    $("#changeAttachTypeName").val(attachTypeName);
    $("#changeIsEnable").val(isEnable);
    $("#changeAttachCode").val(attachNameCode);
    $("#changeAttachName").val(attachName);
    changClass(isEnable,"change");
    $("#changeIsRequired").val(isRequired);
    if(isRequired=="1"){
        changClass("3","change");
    }
    else{
        changClass("4","change");
    }
    //changSelec("changeshowOrder",changeshowOrder);
    changSelec("changeIndustryId",industryType);
    changSelec("changeCheckId",attachTypeCode);
    changeName(attachTypeCode);
    //如果附件类型为空 附件名称也设置空
    if($("#changeCheckId").val() == "")
        attachNameCode = "";
    changSelec("changedictId",attachNameCode);
    if(nodeCode=='3'){
        $("#changintyId").show();
        changClass("13","");
    }else if(nodeCode=='2') {
        $("#changintyId").hide();
        changClass("12","");
    }else if(nodeCode=='1') {
        $("#changintyId").hide();
        changClass("11","");
    }

    openDialog("changeWindow", "修改", 600, 300);
}

function closeChange() {
    $("#changeWindow").dialog('close');
}

function deleteWin(){
    // 缓存查询条件
    localStorage.setItem("attachmentDictId", $("#dictId option:selected").val());
    localStorage.setItem("attachmenJieCode", $("#jieCode").val());
    localStorage.setItem("attachmenCurPage", $("#page_select").val());
    var attachTypeId = $("#deleteId").val();
    $.ajax({
        url:context_path +"/attachment/deleteAttachment",
        type:'post',
        data: {"attachTypeId":attachTypeId},
        datatype: 'json',
        success:function (data) {
            alertInfo("提示","刪除成功！",function () {;
                closedel();
                window.location.href = context_path + "/attachment/viewAttachment?backFlag=-1";
            });
        },
        error:function (XMLHttpRequest, textStatus, errorThrown) {
            alert(textStatus + " " + XMLHttpRequest.readyState + " "
                + XMLHttpRequest.status + " " + errorThrown);
        }
    });

}

function saveItem(flag) {
    //通过class属性判断当前是在拜访笔记，进件申请，微信实访
    var visitingNotes=document.getElementById('changebaiId');
    if(visitingNotes.classList.contains('gpic')) {
        if($("#changeIndustryId").val()=='nothing' || $("#changeCheckId").val()=='nothing' || $("#changedictId").val()=='' || $("#changeshowOrder").val().trim()=='' ) {
            alertInfo("提示","參數不能為空！",function () {
            });
            return ;
        }
    }else{
        if($("#changeIndustryId").val()=='nothing' || $("#changeCheckId").val()=='nothing' || $("#changedictId").val()=='nothing' || $("#changedictId").val()=='' || $("#changeshowOrder").val().trim()=='' ) {
            alertInfo("提示","參數不能為空！",function () {
            });
            return ;
        }
    }

    var nodeName = $("#changeNodeName").val();
    var data =  $("#changeItemData").serialize();
    /* data += "&nodeName=" +nodeName + "&flag="+ flag;;*/
    var attachTypeName = $("#changeCheckId option:selected").text();
    data += "&nodeName=" +nodeName + "&flag="+ flag+"&attachTypeName="+attachTypeName;
    $.ajax({
        url:context_path +"/attachment/saveAttachment",
        type:'post',
        data: data,
        datatype: 'json',
        success:function (result, status, xhr) {
            alertInfo("提示", result.msg, function () {
                closeChange();
                window.location.href = context_path + "/attachment/viewAttachment?backFlag=-1";
            });
        },
        error:function (XMLHttpRequest, textStatus, errorThrown) {
            alert(textStatus + " " + XMLHttpRequest.readyState + " "
                + XMLHttpRequest.status + " " + errorThrown);
        }
    })
}

function openNewWin(){

    openDialog("newWindow", "新增", 600, 300);
}

function closenew() {
    $("#newWindow").dialog('close');
}

function saveNewItem(flag) {

    if( $("#newNodeCode").val().trim()=="" || ($("#newNodeCode").val().trim() != "1" && $("#newIndustryType").val().trim()=="")
        || $("#newAttachTypeCode").val().trim()=="" || $("#newAttachTypeName").val().trim()=="" || $("#newAttachCode").val().trim()==""
        || $("#newAttachName").val().trim()=="" || $("#newIsEnable").val().trim()=="" || $("#newIsRequired").val().trim()=="" || $("#newshowOrder").val().trim()==""){
        alertInfo("提示","參數不能為空！",function () {
        });
        return ;
    }

    //通过class属性判断当前是在拜访笔记，进件申请，微信实访
    var visitingNotes=document.getElementById('newbaiId');
    if(visitingNotes.classList.contains('gpic')) {
        if($("#newCheckId").val()=='nothing' || $("#newdictId").val()=='nothing' || $("#newdictId").val()=='' || $("#newshowOrder").val().trim()=='' ) {
            alertInfo("提示","參數不能為空！",function () {
            });
            return ;
        }
    }else{
        if($("#newIndustryId").val()=='nothing' || $("#newCheckId").val()=='nothing' || $("#newdictId").val()=='nothing' || $("#newdictId").val()=='' || $("#newshowOrder").val().trim()=='' ) {
            alertInfo("提示","參數不能為空！",function () {
            });
            return ;
        }
    }

    var nodeName  = $("#newNodeName").val();
    var data =  $("#newItemData").serialize();
    data = data + "&nodeName=" +nodeName + "&flag="+ flag;
    $.ajax({
        url:context_path +"/attachment/saveAttachment",
        type:'post',
        data: data,
        datatype: 'json',
        success:function (result, status, xhr) {
            alertInfo("提示", result.msg, function () {;
                $("#newviewCode").val("");
                $("#newviewName").val("");
                $("#newWindow").dialog('close');
                findRole(1);
            });
        },
        error:function (XMLHttpRequest, textStatdus, errorThrown) {
            alert(textStatdus + " " + XMLHttpRequest.readyState + " "
                + XMLHttpRequest.status + " " + errorThrown);
        }
    })
}

function changSelec(id,valu) {
    //设置下拉框选中 没有值则选择 空
    $("#"+id).val(valu);
    if($("#"+id).val() == null)
        $("#"+id).val("");
    //更新默认值（附件类型、附件名称）
    var code  = $("#"+id+" option:selected").val();
    var name  = $("#"+id+" option:selected").text();
    var codeId = "";
    var nameId = "";
    if("changeIndustryId" == id) {
        codeId = "changeIndustryType";
        nameId = "changeIndustryTypeName";
    } else if("changeCheckId" == id){
        codeId = "changeAttachTypeCode";
        nameId = "changeAttachTypeName";
    } else {
        codeId = "changeAttachCode";
        nameId = "changeAttachName"
    }
    $("#"+codeId).val(code);
    $("#"+nameId).val(name);
}

function  changClass(flag,id) {

    if(flag =='1'){
        $("#"+id+"enab").attr("class","gpic");
        $("#"+id+"noenab").attr("class","pic");
        $("#"+id+"IsEnable").val("1");

    }else if(flag =='2'){
        $("#"+id+"noenab").attr("class","gpic");
        $("#"+id+"enab").attr("class","pic");
        $("#"+id+"IsEnable").val("0");

    }else if(flag =='3'){
        $("#"+id+"need").attr("class","gpic");
        $("#"+id+"noneed").attr("class","pic");
        $("#"+id+"IsRequired").val("1");
    }else if(flag =='4'){
        $("#"+id+"noneed").attr("class","gpic");
        $("#"+id+"need").attr("class","pic");
        $("#"+id+"IsRequired").val("0");
    }else if(flag =='5'){
        $("#baiId").attr("class","gpic");
        $("#jinId").attr("class","pic");
        $("#zhengId").attr("class","pic");
        $("#intyId").hide();
        $("#jieCode").val("1");
        findRoles();
    }else if(flag =='6'){
        $("#jinId").attr("class","gpic");
        $("#baiId").attr("class","pic");
        $("#zhengId").attr("class","pic");
        $("#intyId").show();
        $("#jieCode").val("2");
        findRole();
    }else if(flag =='7'){
        $("#zhengId").attr("class","gpic");
        $("#baiId").attr("class","pic");
        $("#jinId").attr("class","pic");
        $("#intyId").show();
        $("#jieCode").val("3");
        findRole();
    }else if(flag =='8'){
        $("#newbaiId").attr("class","gpic");
        $("#newzhengId").attr("class","pic");
        $("#newjinId").attr("class","pic");
        $("#newintyId").hide();
        $("#newNodeCode").val("1");
        $("#newNodeName").val("拜訪筆記");
    }else if(flag =='9'){
        $("#newjinId").attr("class","gpic");
        $("#newbaiId").attr("class","pic");
        $("#newzhengId").attr("class","pic");
        $("#newintyId").show();
        $("#newNodeCode").val("2");
        $("#newNodeName").val("進件申請");
    }else if(flag =='10'){
        $("#newzhengId").attr("class","gpic");
        $("#newbaiId").attr("class","pic");
        $("#newjinId").attr("class","pic");
        $("#newintyId").show();
        $("#newNodeCode").val("3");
        $("#newNodeName").val("徵信實訪");
    }else if(flag =='11'){
        $("#changebaiId").attr("class","gpic");
        $("#changezhengId").attr("class","pic");
        $("#changejinId").attr("class","pic");
        $("#changeintyId").hide();
        $("#changeNodeCode").val("1");
        $("#changeNodeName").val("拜訪筆記");
    }else if(flag =='12'){
        $("#changejinId").attr("class","gpic");
        $("#changebaiId").attr("class","pic");
        $("#changezhengId").attr("class","pic");
        $("#changeintyId").show();
        $("#changeNodeCode").val("2");
        $("#changeNodeName").val("進件申請");
    }else if(flag =='13'){
        $("#changezhengId").attr("class","gpic");
        $("#changebaiId").attr("class","pic");
        $("#changejinId").attr("class","pic");
        $("#changeintyId").show();
        $("#changeNodeCode").val("3");
        $("#changeNodeName").val("徵信實訪");
    }
}

function  getSelt(seid,codeid,nameid) {
    var code  = $("#"+seid+" option:selected").val();
    var name  = $("#"+seid+" option:selected").text();
    $("#"+codeid).val(code);
    $("#"+nameid).val(name);
    if(seid == "newCheckId" || seid =="changeCheckId")
        $.ajax({
            url: context_path + "/attachment/changeName", // 需要链接到服务器地址
            type: "post",
            secureuri: false,
            dataType: 'JSON', // 服务器返回的格式，可以是json
            data: {"dictId":code,},
            success: function(data) {
                if(data!= null) {
                    if (seid == "newCheckId") {
                        $("#newdictId").empty();//清空select列表数据
                        $("#newdictId").prepend("<option value=''>--請選擇--</option>");//添加第一个option值
                        //for (var i in data) {
                        for(var i=0;i<data.length;i++){
                            //如果在select中传递其他参数，可以在option 的value属性中添加参数
                            $("#newdictId").append("<option value='" + data[i][0] + "'>" + data[i][1] + "</option>");

                        }
                    } else if (seid == "changeCheckId") {
                        $("#changedictId").empty();//清空select列表数据
                        $("#changedictId").prepend("<option value=''>--請選擇--</option>");//添加第一个option值
                        //for (var i in data) {
                        for(var i =0;i<data.length;i++){
                            //如果在select中传递其他参数，可以在option 的value属性中添加参数
                            $("#changedictId").append("<option value='" + data[i][0] + "'>" + data[i][1] + "</option>");
                        }
                    }
                }
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {

            }
        });
}

function  findPage(curPage) {
    findRole(curPage);
}

function changeName(code){
    $.ajax({
        url: context_path + "/attachment/changeName", // 需要链接到服务器地址
        type: "post",
        secureuri: false,
        async: false,
        dataType: 'JSON', // 服务器返回的格式，可以是json
        data: {"dictId":code},
        success: function(data) {
            if(data!= null) {
                $("#changedictId").empty();//清空select列表数据
                $("#changedictId").prepend("<option value=''>--請選擇--</option>");//添加第一个option值
                //for (var i in data) {
                for(var i =0;i<data.length;i++){
                    // alert(data[i]);
                    // alert(data[i][0]);
                    //如果在select中传递其他参数，可以在option 的value属性中添加参数
                    $("#changedictId").append("<option value='" + data[i][0] + "'>" + data[i][1] + "</option>");
                }
            }
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {

        }
    });
}

function deleteOpenWin(obj) {
    var attachTypeId = $(obj).attr("attachTypeId");
    $("#deleteId").val(attachTypeId);
    openDialog("delWindow", "刪除", 300, 150);

}

function closedel() {
    $("#delWindow").dialog('close');
}