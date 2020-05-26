$(function() {
    $("#appointmentImg").click(function() {
        $("#appointmentDate").focus();
    });
    
    $("#appointmentDate").datetimepicker({
        dateFormat: 'yy-mm-dd'
    });
    $("#comEstabdate").datetimepicker({
        dateFormat: 'yy-mm-dd'
    });
    
    $("#appointmentPosition option[value='FZR']").attr("selected","selected");
    
    var comActualCapital = $("#comActualCapital").val();
    $("#comActualCapital").val(toThousandth(comActualCapital));
    
    var data = getUpdateData();
    $("#updateData").val(data);
});

/**類型變化選擇**/
function changeType(value){
    $("#newCus").attr("class",value != "01" ? "pic" : "gpic");
    $("#oldCus").attr("class",value == "01" ? "pic" : "gpic");
    $("#comCustomerType").val(value);

    if($("#userType").val() != "")
        getEnterSource(value);
}

function getEnterSource(value) {
    $.ajax({
        url: context_path + "/missionStroke/getEnterSource",
        data: {'comCustomerType': $("#comCustomerType").val()},
        type : "POST",
        dataType : "json",
        sync : false,
        success : function (result) {
            var enterSource = document.getElementById("enterSource");
            enterSource.options.length=0;
            if(value != "02")
                enterSource.add(new Option("-請選擇-",""));
            for(var code in result) {
                enterSource.add(new Option(result[code].itemName,result[code].itemCode));
            };
            $("#enterSource").val(value != "02" ? "" : result[0].itemCode);
            changeEnterSource();
        },
        fail : function () {

        }
    });
}

/**
 * 返回
 */
function goBack(){
    window.location.href =
        context_path + "/missionStroke/viewMissionStroke";
}

/**
 * 查询公司详细信息
 */
function searchComDetail() {
    var compilationNo = $("#compilationNo").val();
    $.ajax({
        url: context_path + "/missionStroke/searchComDetail",
        data : {'compilationNo': compilationNo },
        type : "POST",
        dataType : "json",
        success : function (result) {
            var comName = $("#compilationName");
            comName.val("");
            var princilpalName = $("#princilpalName");
            comName.val("");
            var mobile = $("#mobile");
            mobile.val("");
            var email = $("#email");
            email.val("");
            var appointmentUser = $("#appointmentUser");//拜访对象
            var comEstabdate = $("#comEstabdate");
            comEstabdate.val("");
            var comActualCapital = $("#comActualCapital");
            comActualCapital.val("");
            var comOrganization = $("#comOrganization");
            comOrganization.val("");
            var comOrganizationName = $("#comOrganizationName");
            comOrganizationName.val("");
            var comAddress = $("#comAddress");
            comAddress.val("");
            var compRegAddress = $("#compRegAddress");
            compRegAddress.val("");
            var enterSource = $("#enterSource");
            enterSource.val("");
            var comPhoneAreaCode = $("#comPhoneAreaCode");
            comPhoneAreaCode.val("");
            var comPhoneNum = $("#comPhoneNum");
            comPhoneNum.val("");
            var comPhoneExten = $("#comPhoneExten");
            comPhoneExten.val("");
            var appointmentPosition = $("#appointmentPosition");//职称
            appointmentPosition.val("FZR");
            var appoipositionOther = $("#appoipositionOther");//职称其他
            appoipositionOther.val("");
            var introduceName = $("#introduceName");//案源轉介人姓名
            introduceName.val("");
            var introduceId = $("#introduceId");//案源轉介人員编/ID
            introduceId.val("");
            if(result.returnCode) {
                var loanCom = result.returnResult;
                //changeType(loanCom.isNew);
                comName.val(loanCom.compName);
                princilpalName.val(loanCom.chargePerson);
                mobile.val(loanCom.mobile);
                email.val(loanCom.email);
                appointmentUser.val(loanCom.interviewee);
                appointmentPosition.val(loanCom.profeCode);
				changePosition();
                appoipositionOther.val(loanCom.professionalOther);
                
                comEstabdate.val(loanCom.compCreation != "" ? new Date(loanCom.compCreation).format('Y-m-d') : '');
                comActualCapital.val(toThousandth(loanCom.compCapital));
                comOrganization.val(loanCom.compType);
                comOrganizationName.val(loanCom.compTpyeName);
                comAddress.val(loanCom.compAddress);
                compRegAddress.val(loanCom.compRegAddress);
                enterSource.val(loanCom.sourceType);
                changeEnterSource();
                introduceName.val(loanCom.referralName);
                introduceId.val(loanCom.referralId);

                var compContact = loanCom.compContact;
                if (compContact != "" && compContact != null && compContact != "undefined" && compContact != undefined) {
                    var compContactArr = compContact.split("-");
                    var compContactArr1 = compContactArr.length > 1 ? compContactArr[1].split("#") : {};
                    comPhoneAreaCode.val(compContactArr[0]);
                    comPhoneNum.val(compContactArr1.length > 0 ? compContactArr1[0] : "");
                    comPhoneExten.val(compContactArr1.length > 1 ? compContactArr1[1] : "");
                }
            }
         },
        fail: function(){

        }
    });
}

/**
 * 显示使用说明
 */
function showUseDescription() {
    openDialog("useDescription","使用說明", 450,200);
}

/**
 * 儲存预约记录
 */




function saveStroke() {
    if(!necessityCheck()) return;
    /*var address = $("#comRealCityName").val() + $("#comRealDistrictName").val() + $("#comRealStreetName").val();
    var comRealTunnel=$("#comRealTunnel").val();
    var comRealLane=$("#comRealLane").val();
    var comRealAddnumber=$("#comRealAddnumber").val();
    var comRealSpace1=$("#comRealSpace1").val();
    var comRealFloor=$("#comRealFloor").val();
    var comRealSpace2=$("#comRealSpace2").val();
    var comRealRoom=$("#comRealRoom").val();
    
  
    if(comRealTunnel!=""){
    	address+=comRealTunnel+"巷";
    }
    if(comRealLane!=""){
    	address+=comRealLane+"弄";
    }
    if(comRealAddnumber!=""){
    	
    	if(comRealSpace1==""&&comRealFloor==""){
    		address+=comRealAddnumber+"號";
    	}else{
    	
    	address+=comRealAddnumber;
    }
    }
  
    if(comRealSpace1!=""||comRealFloor!=""){
    	
    	if(comRealAddnumber==""){
    		address+="之"+comRealSpace1+comRealFloor+"樓";
    	}else{
    		address+="號之"+comRealSpace1+comRealFloor+"樓";
    	}
    	
    }
    
    if(comRealSpace2!=""||comRealRoom!=""){
    	
    	if(comRealSpace1==""&&comRealFloor==""){
    		address+="之"+comRealSpace2+comRealRoom+"室";
    	}else{
    		address+="之"+comRealSpace2+comRealRoom+"室";
    	}
    	
    	
    }*/
   
 /*   var introducePhone = "(" + $("#introducePhoneExt").val() + ")" + $("#introducePhone").val();*/
    
    var data = getSaveData();
    bodymask("請銷后...");
    $.ajax({
        url: context_path + "/missionStroke/saveStroke",
        data : data ,
        type : "POST",
        dataType : "json",
        success : function (result) {
            bodyunmask();
            alertInfo("提示",result.returnMessage,function (){
                if (result.returnCode) {
                    history.go(-1);
                }
            });
        },
        fail: function(){
            bodyunmask();
        }
    });
}

/**
 * @description 獲取頁面上需要保存的數據
 * @returns
 */
function getSaveData() {
    // 實際營業地址
    var address = $("#comAddress").val();
    // 公司登記地址
    var redAddress = $("#compRegAddress").val();
    if ($("#introducePhoneExt").val() == "") {
        var introducePhone = $("#introducePhone").val();
    }
    if($("#introducePhoneExt").val() != "") {
        var introducePhone = "" + $("#introducePhoneExt").val() + "-" + $("#introducePhone").val();
    }
    
    //案件編號
    var caseNo = $("#caseNo").val();
    
    //行動電話
    var mobile = $("#mobile").val();
    //電子郵箱
    var email = $("#email").val();
    
    var data = $.param({"businessAddress":address, "introducePhone" :introducePhone}) + "&" + $("#missionStroke").serialize()+"&mobile="+mobile+"&email="+email+"&caseNo="+caseNo;
    return data;
}

/**
 * @description 返回上一頁方法
 * @returns
 */
function backPage(isEdit) {
    var oriSaveData = $("#saveData").val();
    var newSaveData = getSaveData();
    if (isEdit == "1" && oriSaveData != newSaveData) {
        confirmInfo_2("提示","確定要離開頁面嗎, 離開後信息將不予保留", 300, 140, function () {
            history.go(-1);
        });
    } else {
        history.go(-1);
    }
}

function updateStroke() {
    if(!necessityCheck()) {
        return;
    }
    
    /*var comRealCityName = "";
    var comRealCityNameVal = $("#comRealCityCode").find("option:selected").val();
    if (comRealCityNameVal != "" && comRealCityNameVal != null && comRealCityNameVal != "undefined" && comRealCityNameVal != undefined) {
        comRealCityName = $("#comRealCityCode").find("option:selected").text();
    }
        
    var comRealDistrictName = "";
    var comRealDistrictNameVal = $("#comRealDistrictCode").find("option:selected").val();
    if (comRealDistrictNameVal != "" && comRealDistrictNameVal != null && comRealDistrictNameVal != "undefined" && comRealDistrictNameVal != undefined) {
        comRealDistrictName = $("#comRealDistrictCode").find("option:selected").text();
    }
        
    var comRealStreetName = "";
    var comRealStreetNameVal = $("#comRealStreetCode").find("option:selected").val();
    if (comRealStreetNameVal != "" && comRealStreetNameVal != null && comRealStreetNameVal != "undefined" && comRealStreetNameVal != undefined) {
        comRealStreetName = $("#comRealStreetCode").find("option:selected").text();
    }
        
    var address = comRealCityName + comRealDistrictName + comRealStreetName;
    var comRealTunnel=$("#comRealTunnel").val();
    var comRealLane=$("#comRealLane").val();
    var comRealAddnumber=$("#comRealAddnumber").val();
    var comRealSpace1=$("#comRealSpace1").val();
    var comRealFloor=$("#comRealFloor").val();
    var comRealSpace2=$("#comRealSpace2").val();
    var comRealRoom=$("#comRealRoom").val();
    console.log($("#introduceId").val());
    if(comRealTunnel!=""){
        address+=comRealTunnel+"巷";
    }
    if(comRealLane!=""){
        address+=comRealLane+"弄";
    }
    if(comRealAddnumber!=""){
        
        if(comRealSpace1==""&&comRealFloor==""){
            address+=comRealAddnumber+"號";
        }else{
        
        address+=comRealAddnumber;
    }
    }
  
    if(comRealSpace1!=""||comRealFloor!=""){
        
        if(comRealAddnumber==""){
            address+="之"+comRealSpace1+comRealFloor+"樓";
        }else{
            address+="號之"+comRealSpace1+comRealFloor+"樓";
        }
        
    }
    
    if(comRealSpace2!=""||comRealRoom!=""){
        
        if(comRealSpace1==""&&comRealFloor==""){
            address+="之"+comRealSpace2+comRealRoom+"室";
        }else{
            address+="之"+comRealSpace2+comRealRoom+"室";
        }
        
        
    }*/
   
 /*   var introducePhone = "(" + $("#introducePhoneExt").val() + ")" + $("#introducePhone").val();*/
    var data = getUpdateData();
    bodymask("請銷后...");
    $.ajax({
        url: context_path + "/missionStroke/updateStroke",
        data : data ,
        type : "POST",
        dataType : "json",
        success : function (result) {
            $("#updateData").val(data);
            bodyunmask();
            alertInfo("提示", result.returnMessage, function (){
                if (result.returnCode) {
                    $("#updateData").val(data);
                }
            });
        },
        fail: function(){
            bodyunmask();
        }
    });
}

/**
 * @descripiton 獲取需要更新的數據
 * @returns
 */
function getUpdateData() {
    var address = $("#comAddress").val();
    if ($("#introducePhoneExt").val() == "") {
        var introducePhone = $("#introducePhone").val();
    }
    if($("#introducePhoneExt").val() != "") {
        var introducePhone = "" + $("#introducePhoneExt").val() + "-" + $("#introducePhone").val();
    }
    //行動電話
    var mobile = $("#mobile").val();
    //電子郵箱
    var email = $("#email").val();
    
    console.log($("#introduceId").val());
    var data = $.param({"businessAddress":address, "introducePhone" :introducePhone}) + "&" + $("#missionStroke").serialize()+"&mobile="+mobile+"&email="+email;
    return data;
}

/**
 * @description 返回上一頁方法
 * @returns
 */
function editbBackPage(isEdit) {
    var oriSaveData = $("#updateData").val();
    var newSaveData = getUpdateData();
    if (isEdit == "1" && oriSaveData != newSaveData) {
        confirmInfo_2("提示","確定要離開頁面嗎, 離開後信息將不予保留", 300, 140, function () {
            history.go(-1);
        });
    } else {
        history.go(-1);
    }
}

/*function saveStroke() {
    if(!necessityCheck()) return;
    var address = $("#comRealCityName").val() + $("#comRealDistrictName").val() + $("#comRealStreetName").val() + $("#comRealTunnel").val()
    +"巷" + $("#comRealLane").val() + "弄" + $("#comRealAddnumber").val()+ "號之" + $("#comRealSpace1").val()
    + $("#comRealFloor").val() + "樓之" + $("#comRealSpace2").val()+$("#comRealRoom").val()+ "室";
    var introducePhone = "(" + $("#introducePhoneExt").val() + ")" + $("#introducePhone").val();
    var data = $.param({"businessAddress":address,"introducePhone" :introducePhone})+ "&" + $("#missionStroke").serialize();
    bodymask("請銷后...");
    $.ajax({
        url: context_path + "/missionStroke/saveStroke",
        data : data ,
        type : "POST",
        dataType : "json",
        success : function (result) {
            bodyunmask();
            alertInfo("提示",result.returnMessage,function (){
                if(result.returnCode)
                    goBack();
            });
        },
        fail: function(){
            bodyunmask();
        }
    });
}
*/
/**
 * 必输性校验
 * @returns {boolean}
 */
function necessityCheck() {
    var returnResult = true;
    if ($("#compilationNo").val() == "") {
        returnResult = false;
        alertInfo("提示","授信戶統編不能為空");
        return returnResult;
    }
    
    if ($("#compilationNo").val().length < 8) {
        returnResult = false;
        alertInfo("提示","授信戶統編不能小於8位");
        return returnResult;
    }
    
    if ($("#compilationName").val() == "") {
        returnResult = false;
        alertInfo("提示","授信戶名稱不能為空");
        return returnResult;
    }
    
    if ($("#princilpalName").val() == "") {
        returnResult = false;
        alertInfo("提示","負責人不能為空");
        return returnResult;
    }
    if ($("#enterSource").val() == "") {
        returnResult = false;
        alertInfo("提示","案件來源不能為空");
        return returnResult;
    }
    if ($("#comEstabdate").val() == "") {
        returnResult = false;
        alertInfo("提示","公司設立日期不能為空");
        return returnResult;
    }
    if ($("#comOrganization").val() == "") {
        returnResult = false;
        alertInfo("提示","組織型態不能為空");
        return returnResult;
    }
    if ($("#comActualCapital").val() == "") {
        returnResult = false;
        alertInfo("提示","資本額不能為空");
        return returnResult;
    }
    
    if ($("#compRegAddress").val() == "") {
        returnResult = false;
        alertInfo("提示","公司登記地址不能為空");
        return returnResult;
    }
    
    if ($("#comAddress").val() == "") {
        returnResult = false;
        alertInfo("提示","實際營業地址不能為空");
        return returnResult;
    }
    
    if ($("#userType").val() != "" && $("#userType").val() != undefined && $("#userType").val() != "undefined" && $("#enterSource").val().indexOf("ZJ")>=0) {
        if($("#introduceName").val() == "") {
            returnResult = false;
            alertInfo("提示","案源轉介人姓名不能為空");
            return returnResult;
        }
        if($("#introduceId").val() == "") {
            returnResult = false;
            alertInfo("提示","案源轉介人员编/ID不能為空");
            return returnResult;
        }
    }
    
    if ($("#comRealCityCode").val() == "") {
        returnResult = false;
        alertInfo("提示","實際營業地址-縣市不能為空");
        return returnResult;
    }
    
    if($("#appointmentUser").val() == "") {
        returnResult = false;
        alertInfo("提示","拜訪對象不能為空");
        return returnResult;
    }
    
    if ($("#appointmentPosition").val() == "") {
        returnResult = false;
        alertInfo("提示","職稱不能為空");
        return returnResult;
    }
    
    if ($("#appointmentPosition").val() == "99" && $("#appoipositionOther").val() == "") {
        returnResult = false;
        alertInfo("提示","職稱其他不能為空");
        return returnResult;
    }
    if ($("#comPhoneAreaCode").val() == "" && $("#comPhoneNum").val() == "" && $("#comPhoneExten").val() == "" ) {
        returnResult = false;
        alertInfo("提示","公司聯絡電話不能為空");
        return returnResult;
    }
    if ($("#comPhoneAreaCode").val().length < 2) {
        returnResult = false;
        alertInfo("提示","公司連絡電話區號不能小於2位");
        return returnResult;
    }
    if ($("#comPhoneNum").val().length < 6) {
        returnResult = false;
        alertInfo("提示","公司連絡電話不能小於6位");
        return returnResult;
    }
    
    if ($("#email").val() != "") {
    	if(!chkEmail($("#email").val())){
    		alertInfo("提示","請輸入正確格式的電子郵件信箱");
    		return;
    	}
    }
    
    if ($("#mobile").val() != "" && $("#mobile").val().length != 10) {
        alertInfo("提示","請輸入正確格式的行動電話");
        return;
    }
    
    if ($("#appointmentDate").val() == "") {
        returnResult = false;
        alertInfo("提示","預定拜訪日期不能為空");
        return returnResult;
    }
    if ($("#appointment_H").val() == "" || $("#appointment_M").val() == "") {
        returnResult = false;
        alertInfo("提示","預定拜訪時間不能為空");
        return returnResult;
    }
    
   
    var sdate = $("#appointmentDate").val().split('-');
    var appointmentDateTime = new Date(sdate[0], sdate[1]-1, sdate[2],$("#appointment_H").val(),$("#appointment_M").val());
    if(appointmentDateTime < new Date()) {
        returnResult = false;
        alertInfo("提示","預定拜訪日期不能早於當前");
        return returnResult;
    }
    return returnResult;
}

/* 
*验证邮箱格式是否正确 
*参数strEmail，需要验证的邮箱 
*/ 
function chkEmail(strEmail) { 
    if (!/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/.test(strEmail)) { 
    return false; 
     }else { 
      return true; 
     } 
} 

/**
 * 更改进件来源
 */
function changeEnterSource() {
    var enterSource = $("#enterSource").val();
    $("#introduceId").val("");
    $("#introduceName").val("");
    $("#introduceAddress").val("");
    $("#introducePhone").val("");
    $("#introducePhoneExt").val("");
    if(enterSource.indexOf("ZJ")>=0) {
        var enterSourceHidden = $("#enterSourceHidden").val();
        $(".introduce").each(function () {
            $(this).show();
        });
        if (enterSourceHidden == enterSource) {
            $("#introduceId").val($("#introduceIdHidden").val());
            $("#introduceName").val($("#introduceNameHidden").val());
            $("#introduceAddress").val($("#introduceAddressHidden").val());
            $("#introducePhone").val($("#introducePhoneHidden").val());
            $("#introducePhoneExt").val($("#introducePhoneExtHidden").val());
        }
    }else {
        $(".introduce").each(function () {
            $(this).hide();
        });
    }
}

/**
 * 获取城市数据
 */
function selectCity() {
    $.ajax({
        url: context_path + "/missionStroke/getDistrict",
        data: {"comRealCityCode" : $("#comRealCityCode").val()},
        type : "POST",
        dataType : "json",
        success : function (result) {
            var districtSelect = document.getElementById("comRealDistrictCode");
            districtSelect.options.length=0;
            districtSelect.add(new Option("-請選擇-",""));
            var streetSelect = document.getElementById("comRealStreetCode");
            streetSelect.options.length=0;
            streetSelect.add(new Option("-請選擇-",""));
            $("#comRealDistrictName").val();
            $("#comRealStreetName").val();
            $("#comRealCityName").val($('#comRealCityCode option:selected').text());
            for(var i in result) {
                var option = document.createElement("option");
                option.setAttribute("value",result[i].districtCode);
                option.text= result[i].districtName;
                districtSelect.appendChild(option);
            }
        },
        fail : function () {

        }
    });
}

/**
 * 获取街道数据
 */
function selectDistrict() {
    $.ajax({
        url: context_path + "/missionStroke/getStreet",
        data: {"comRealDistrictCode" : $("#comRealDistrictCode").val()},
        type : "POST",
        dataType : "json",
        success : function (result) {
            var streetSelect = document.getElementById("comRealStreetCode");
            streetSelect.options.length=0;
            streetSelect.add(new Option("-請選擇-",""));
            $("#comRealStreetName").val();
            $("#comRealDistrictName").val($('#comRealDistrictCode option:selected').text());
            for(var i in result) {
                var option = document.createElement("option");
                option.setAttribute("value",result[i].streetCode);
                option.text= result[i].streetName;
                streetSelect.appendChild(option);
            }
        },
        fail : function () {

        }
    });
}

function selectStreet() {
    $("#comRealStreetName").val($('#comRealStreetCode option:selected').text());
}

function changePosition() {
    if($("#appointmentPosition").val() == "99")
        $("#appoipositionOther").show();
    else
        $("#appoipositionOther").hide();
    $("#appoipositionOther").val("");
}

function selectOrganization() {
    $("#comOrganizationName").val($("#comOrganization").find("option:selected").text());
}

/**
 * @description 转化千分位，参数obj只能包含数字
 * @param obj
 * @returns
 */
function toThousandth(obj) {
    obj = obj + "";
    obj = obj.replace(/^(0)*/g, "");
    while (/\d{4}(\.|,|$)/.test(obj)) {
        obj = obj.replace(/(\d)(\d{3}(\.|,|$))/,"$1,$2");
    }
    return obj;
}

/**
 * @description 去掉千分位的逗号
 * @param obj
 * @returns
 */
function reverseThousandth(obj) {
    obj = obj + "";
    obj = obj.replace(/[^\d\.]/g, "");
    return obj;
}

function capitalToThousandth(obj) {
    var numStr = $(obj).val() + ""; 
    numStr = reverseThousandth(numStr);
    $(obj).val(toThousandth(numStr));
}
