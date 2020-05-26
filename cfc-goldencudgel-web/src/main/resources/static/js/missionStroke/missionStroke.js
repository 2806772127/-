$(function(){
    var appointmentDate = $("#appointmentDate").val();
    var year;
    var month;
    var day;
    var today = new Date();
    if (appointmentDate == "undefined" || appointmentDate == undefined || appointmentDate == null || appointmentDate == "") {
        var today = new Date();
        year = today.getFullYear();
        month = today.getMonth() + 1;
        day = today.getDate();
        month = month < 10 ? "0" + month : month;
        day = day < 10 ? "0" + day : day;
        $("#appointmentDate").val(year + "-" + month + "-" + day);
    } else {
        year = appointmentDate.substring(0, 4);
        month = appointmentDate.substring(5, 7);
        day = appointmentDate.substring(8, 10);
    }
    
    showYearList(year);
    showCalender(year, month);
    var selectTd = $("#calendar_tb").find("tr td[d=" + ("" + day).replace(/\b(0+)/gi, "") + "]").eq(0);
    $(selectTd).attr("class", "_select");
    var searchType = $("#searchType").val();
    if (searchType != "searchByMonth") {
        if ($(selectTd).attr("Y") == today.getFullYear() && $(selectTd).attr("M") == today.getMonth() + 1 && $(selectTd).attr("D") == today.getDate()) {
            $(selectTd).css("background-color", "#7DB7F0");
        } else {
            $(selectTd).css("background-color", "#D7D7D7");
        }
    }
    //显示数据
    searchStroke();//任务行程
    // 指标达成率
    changeProdCode();
    var taskProgressPage = $("#taskProgressPage").val();
    if (taskProgressPage == "undefined" || taskProgressPage == undefined || taskProgressPage == null || taskProgressPage == "") {
        taskProgressPage = localStorage.getItem("pageNo");
        if (taskProgressPage == "undefined" || taskProgressPage == undefined || taskProgressPage == null || taskProgressPage == "") {
            taskProgressPage = 1;
        } else {
            localStorage.removeItem("pageNo");
        }
    }
    findPage(taskProgressPage);//行程进度
});
//全局变量
var choseYear = 0;

/**
 * 显示日历
 * @param year
 * @param month
 */
function showCalender(year,month){
    var oTBody = document.getElementById("calendar_tb");//取得table
    var row_length = oTBody.rows.length;
    var trs = oTBody.getElementsByTagName("tr");
    for(var rows = 1 ; rows < row_length +1 ; rows ++){
        oTBody.removeChild(trs[0]);
    }
    var crrentDay = new Date();
    crrentDay.setYear(year);
    crrentDay.setMonth(month);
    var maxIndex = new Date(year,month,0).getDate();//这个月最大天数
    var upMaxIndex = new Date(year,month-1,0).getDate();//上个月最大天数
    var firstIndex = new Date(year,month-1,1).getDay();//当月1号星期几

    var days = new Array(42);

    //上个月
    var upYear = (month  == 1) ? (Number(year)-1) : year;
    var upMonth =  (month == 1) ? 12 : Number(month) -1 ;
    for(var d=0; d < firstIndex;d++){
        days[d] = {"Y":upYear,"M":upMonth,"D":upMaxIndex - firstIndex + d + 2,"type":"U"};
    }

    //当月时间
    for(var d = 0 ; d < maxIndex ; d ++){
        days[firstIndex+d-1] = {"Y":year,"M":month,"D":d+1,"type":"T"};
    }

    //下个月
    var nextYear = (month  == 12) ? (Number(year)+1) : year;
    var nextMonth =  (month == 12) ? 1 : Number(month) + 1;
    for(var d = 0 ; d < 42 - maxIndex - firstIndex +1 ; d ++){
        days[maxIndex+firstIndex-1 + d] = {"Y":nextYear,"M":nextMonth,"D":d+1,"type":"N"};
    }

    var today = new Date();
    for(var j=0; j<3 ; j++) {
        var newTr = document.createElement("TR");
        newTr.setAttribute("style","border-top:1px solid #E4E4E4;");
        for(var i=j*14 ; i< (j+1)*14 ;i++) {
            var newTd = document.createElement("TD");
            if (days[i].type == "T") {
                if((i+1)%7 == 0 || (i+1)%7 ==6)//周末的日期显示不同颜色
                    newTd.setAttribute("style","text-align:center;color: #AEAEAE;cursor:pointer");
                else
                    newTd.setAttribute("style","text-align:center;color: #149ac1;cursor:pointer");
                //当日显示不同背景颜色
                if(days[i].Y == today.getFullYear() && days[i].M == today.getMonth()+1 && days[i].D == today.getDate())
                    newTd.style.backgroundColor="#F5F5F5";
                newTd.onclick=function(){
                    //当日显示不同背景颜色
                    if($("._select").attr("Y") == today.getFullYear() && $("._select").attr("M") == today.getMonth()+1 && $("._select").attr("D") == today.getDate())
                        $("._select").css("background-color","#F5F5F5");
                    else
                        $("._select").css("background-color","");
                    $("._select").removeClass();
                    $(this).attr("class","_select");
                    if($(this).attr("Y") == today.getFullYear() && $(this).attr("M") == today.getMonth()+1 && $(this).attr("D") == today.getDate())
                        $(this).css("background-color","#7DB7F0");
                    else
                        $(this).css("background-color","#D7D7D7");
                    var month =$("#_select_date").attr("_select_month").length == 1 ? "0" + $("#_select_date").attr("_select_month") : $("#_select_date").attr("_select_month");
                    var day = $(this).attr("D").length == 1 ? "0" + $(this).attr("D") : $(this).attr("D");
                    $("#appointmentDate").val($("#_select_date").attr("_select_year")+"-"+month+"-"+day);
                    $("#searchType").val("searchByDay");
                    searchStroke();
                };
            } else {
                newTd.setAttribute("style","text-align:center;color: #F2F2F4");
            }
            newTd.setAttribute("Y",days[i].Y);
            newTd.setAttribute("M",days[i].M);
            newTd.setAttribute("D",days[i].D);

            newTd.innerHTML = +days[i].D;
            newTr.appendChild(newTd);
        }
        oTBody.appendChild(newTr);
    }

    var selectDate = $("#_select_date");
    //设置日期
    var selMonth = month + "";
    if (selMonth < 10 && selMonth.length < 2) {
        selMonth = "0" + selMonth;
    }
    selectDate.html(year + "年" + selMonth + "月");
    selectDate.attr("_select_year",year);
    selectDate.attr("_select_month",month);
}

function showYearList(year) {
    var today = new Date();
    var selectDate = $("#_select_date");
    var oTBody = document.getElementById("_calendar_year_tb");//取得table
    var row_length = oTBody.rows.length;
    var trs = oTBody.getElementsByTagName("tr");
    for(var rows = 1 ; rows < row_length +1 ; rows ++){
        oTBody.removeChild(trs[0]);
    }
    var startYear  = Math.floor(Number(year)/20) * 20;//20年显示
    var years = new Array(20);
    for(var y = 0 ; y < 20 ; y ++){
        years[y] = startYear + y;
    }
    for(var j=0; j<4 ; j++) {
        var newTr = document.createElement("TR");
        newTr.setAttribute("style","border-top:1px solid #E4E4E4;");

        for(var i=j*5 ; i< (j+1)*5 ;i++) {
            var newTd = document.createElement("TD");
            newTd.setAttribute("style","text-align:center;color: #149ac1;cursor:pointer");
            newTd.setAttribute("year", years[i]);
            if($(this).attr("year") == today.getFullYear())
                newTd.style.backgroundColor="#F5F5F5";
            newTd.onclick=function(){
                var year = $(this).attr("year");
                var month = selectDate.attr("_select_month");
                var selMonth = month + "";
                if (selMonth < 10 && selMonth.length < 2) {
                    selMonth = "0" + selMonth;
                }
                selectDate.html(year + "年" + selMonth + "月");
                selectDate.attr("_select_year",year);

                showCalendar();
            };
            newTd.innerHTML = years[i];
            newTr.appendChild(newTd);
        }
        oTBody.appendChild(newTr);
    }
}

/**
 * 上个 月 年
 */
function upMonth() {
    var showType = $("#_select_date").attr("showType");
    var selectDate = $("#_select_date");
    if("day" == showType) {
        var year = selectDate.attr("_select_year");
        var month = selectDate.attr("_select_month");
        var upYear = (month  == 1) ? (Number(year)-1) : year;
        var upMonth =  (month == 1) ? 12 : Number(month) - 1;
        showCalender(upYear,upMonth);
    }
    if( "month" == showType){
        var year = selectDate.attr("_select_year");
        year = Number(year) - 1;
        selectDate.html(year+"年");
        selectDate.attr("_select_year",year);
    }
    if( "year" == showType){
        var year = selectDate.attr("_select_year");
        choseYear = choseYear == 0 ? Number(year)-20 : choseYear - 20;
        showYearList(choseYear);
    }
}

/**
 * 下个月 年
 */
function nextMonth() {
    var showType = $("#_select_date").attr("showType");
    var selectDate = $("#_select_date");
   if("day" == showType) {
       var year = selectDate.attr("_select_year");
       var month = selectDate.attr("_select_month");
       var nextYear = (month == 12) ? (Number(year) + 1) : year;
       var nextMonth = (month == 12) ? 1 : Number(month) + 1;
       showCalender(nextYear, nextMonth);
   }
   if( "month" == showType){
        var year = selectDate.attr("_select_year");
       year = Number(year) + 1;
        selectDate.html(year + "年");
        selectDate.attr("_select_year",year);
    }
    if( "year" == showType){
        var year = selectDate.attr("_select_year");
        choseYear = choseYear == 0 ? Number(year)+20 : choseYear + 20;
        showYearList(choseYear);
    }
}

/**
 * 日期显示类型
 */
function showCalendar(){
    var selectDate = $("#_select_date");
    var year = selectDate.attr("_select_year");
    var month = selectDate.attr("_select_month");
    // 从日期展示到月份展示
    if($("#_select_date").attr("showType") == "day"){
        $("#_calendar_month").show();
        $("#_calendar_day").hide();
        $("#_calendar_year").hide();
        $("#_select_date").attr("showType","month");
        selectDate.html(year+"年");
    // 从月份展示到年份展示
    }else if(($("#_select_date").attr("showType") == "month")){
        $("#_calendar_year").show();
        $("#_calendar_month").hide();
        $("#_calendar_day").hide();
        $("#_select_date").attr("showType","year");
        selectDate.html(year+"年");
        choseYear = Number(year);
    // 其他情况回到日期展示
    }else {
        $("#searchType").val("searchByMonth");
        $("#_calendar_day").show();
        $("#_calendar_month").hide();
        $("#_calendar_year").hide();
        $("#_select_date").attr("showType","day");
        var selMonth = month + "";
        if (selMonth < 10 && selMonth.length < 2) {
            selMonth = "0" + selMonth;
        }
        selectDate.html(year + "年" + selMonth + "月");
    }
}

function changeMonth(obj) {
    var selectDate = $("#_select_date");
    var year = selectDate.attr("_select_year");
    var month = $(obj).attr("month");
    var selMonth = month + "";
    if (selMonth < 10 && selMonth.length < 2) {
        selMonth = "0" + selMonth;
    }
    selectDate.html(year + "年" + selMonth + "月");
    var day = $("#appointmentDate").val().substring(8, 10);
    $("#appointmentDate").val(year + "-" + selMonth + "-" + day);
    $("#_select_date").attr("showType","year");
    showCalendar();
    showCalender(year,month);
    
    var data = $("#missionStroke").serialize();
    var queryArr = data.split("&");
    var params = "";
    for (var i = 0; i < queryArr.length; i++) {
        var param = queryArr[i];
        if (param.indexOf("appointmentDate") != -1) {
            if (month.length == 1) {
                param = "appointmentDate=" + year + "-0" + month;
            } else {
                param = "appointmentDate=" + year + "-" + month;
            }
        }
        if (i == 0) {
            params = params + param;
        } else {
            params = params + "&" + param;
        }
    }
    searchStrokeByMonth(params)
}

function searchStrokeByMonth(queryParams) {
    bodymask("請銷后...");
    $.ajax({
        url: context_path + "/missionStroke/searchStrokeByMonth",
        data: queryParams,
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
            searchStroke();
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
            searchStroke();
        },
        fail : function () {

        }
    });
}

function showSearch() {
    $(".condition1").toggle();
    $(".condition2").toggle();
    $("#comName").val("");
}

/**
 * 查看任务行程详情
 * @param compilationNo
 * @param trandId
 */
function showStrokeDetial(compilationNo,trandId,appointmentType) {
    $.ajax({
        url: context_path + "/missionStroke/showStrokeDetial",
        data: {
            "compilationNo" : compilationNo,
            "trandId" : trandId,
            "appointmentType" : appointmentType
        },
        type : "POST",
        dataType : "HTML",
        success : function (result) {
            $("#strokeDetial").empty();
            $("#strokeDetial").html(result);
            openDialog("strokeDetial","查看任務行程", 650, 400);
        },
        fail : function () {

        }
    });
}

/**
 * 显示达成率
 * @param id
 * @param data
 * @param title
 */
function showRateDetial(id,data,title) {
    var option = {
        graphic: {
            type : 'text',
            left: 'center',
            top : 'center',
            style: {
                text: title,
                textAlign : 'center'
            }
        },
        tooltip: {
            trigger: 'item',
            formatter: "{a} <br/>{b}: {c} ({d}%)"
        },
        color :['#99BB32','#D13E3F'],
        series: [
            {
                name:'進件達成率',
                type:'pie',
                radius: ['50%', '70%'],
                avoidLabelOverlap: false,
                label: {
                    normal: {
                        show: false,
                        position: 'center'
                    },
                    emphasis: {
                        show: true,
                        textStyle: {
                            fontSize: '30',
                            fontWeight: 'bold'
                        }
                    }
                },
                labelLine: {
                    normal: {
                        show: false
                    }
                },
                name:['已完成','未完成'],
                data:[
                    {value: data},
                    {value: 100 > data ? 100 - data : 0}
                 
                ]
        }
        ]
    };
    //初始化echarts实例
    var myChart = echarts.init(document.getElementById(id));

    //使用制定的配置项和数据显示图表
    myChart.setOption(option);
}

function showRate() {
    var canvas = document.getElementsByTagName('canvas');
    $("canvas").each(function() {
        var id = $(this).attr("id");
        var rate = $(this).attr("rate");
        var rateNum = Number(rate);
        showRateDetial(id, rateNum, rateNum + "%");
    });
}

function changeProdCode() {
    var areaCode = $("#reachingAreaCode").val();
    var prodCode  = $("#prodCode").val();
    $.ajax({
        url: context_path + "/missionStroke/changeProdCode",
        data: {"areaCode": areaCode, "prodCode" : prodCode},
        type : "POST",
        dataType : "json",
        async: false,
        success : function (result) {
            if (result.statisticsTime == 0 || null){
                $("#statisticsTime").text("");
            } else{
                $("#statisticsTime").text(result.statisticsTime);

            }

            $("#applyCount").text(result.applyCount);
            $("#applyAmount").text(result.applyAmount);
            $("#applyRate").attr("rate", result.applyRate);
            $("#achieveCount").text(result.achieveCount);
            $("#loanAmount").text(result.loanAmount);
            $("#achieveRate").attr("rate", result.achieveRate);
            $(".money").text(function(i,v){
                return v.replace(/(?!^)(?=(\d{3})+($|\.))/g,",");
            });
            showRate();
        },
        fail : function () {

        }
    });
}

function addStroke(obj) {
    var userType = $(obj).attr("userType");
    if (userType == 'Z') {
        $.ajax({
            url: context_path + "/missionStroke/getCreditCaseList",
            dataType: "html",
            type: "post",
            success: function(result) {
                $("#caseList").empty();
                $("#caseList").html(result);
                openDialog("addStrokeByZXY", "新增任務行程", 650, 400);
            },
            error: function() {
                
            }
        })
        
    } else {
        window.location.href = context_path + "/missionStroke/viewAddStrokeView";
    }
    $("#caseList").on("click", "tbody tr", function() {
        var caseNo = $(this).find("td").eq(2).text();
        window.location.href = context_path + "/missionStroke/viewAddStrokeViewByZXY?caseNo="+caseNo;
    });
}

function editMissionStroke(compilationNo, trandId, appointmentType) {
    window.location.href = context_path + "/missionStroke/editMissionStroke?compilationNo=" + compilationNo + "&trandId=" + trandId + "&appointmentType=" + appointmentType;
}

function findPage(curPage) {
    $("#taskProgressPage").val(curPage);
    $.ajax({
        url: context_path + "/missionStroke/searchTaskProgress",
        data: { "curPage" : curPage },
        type : "POST",
        dataType : "HTML",
        success : function (result) {
            $("#taskProgress").empty();
            $("#taskProgress").html(result);
        },
        fail : function () {

        }
    });
}

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

function showDetial(obj,type) {
    var pageNo = $("#page_select").val();
    localStorage.setItem("pageNo", pageNo);
    var compilationNo = $(obj).parents("td").attr("compilationNo");
    var trandId = $(obj).parents("td").attr("trandId");
    var measureId =  $(obj).parents("td").attr("measureId");
    if(1 == type) {
        window.location.href =
            context_path + "/collectionQuery/visitDetail?compilationNo="+compilationNo +"&trandId=" +trandId+"&sy=sy";
    } if(2 == type) {
       /* window.location.href =
            context_path + "/measureWord/viewMeasureWords?compilationNo="+compilationNo +"&trandId=" +trandId;*/
    	
    	if(measureId!=""){
    	window.location.href = 
    		context_path + "/measureWord/viewMeasureWordDetail?measureId="+measureId;
    	}
    	
    } if(3 == type) {
        window.location.href =
            context_path + "/ApplyIncom/seeApplyIncom?compilationNo="+compilationNo;
    } if(4 == type) {
        window.location.href =
            context_path + "/CreditReport/seeCreditRepor?compilationNo="+compilationNo +"&trandId="+ trandId;
    }
}

/**
 * @description 根據統編查詢案件
 * @returns
 */
function searchCase() {
    var compilationNo = $("#compilationNo").val();
    $.ajax({
        url: context_path + "/missionStroke/searchCaseByCompilationNo",
        type: "post",
        data: {"compilationNo": compilationNo},
        dataType: "html",
        success: function(result, status, xhr) {
            $("#caseList").empty();
            $("#caseList").html(result);
        },
        error: function(xhr, status, error) {
        }
    });
}

/**
 * @description 刪除任務行程
 * @param compilationNo
 * @param trandId
 * @param appointmentType
 * @returns
 */
function delMissionStroke(compilationNo, trandId, appointmentType, createUser) {

    alertInfoCOPY("提示","請確認是否刪除？",function del() {
        $.ajax({
            url: context_path + "/missionStroke/delMissionStroke",
            data: {
                "compilationNo": compilationNo,
                "trandId": trandId,
                "appointmentType": appointmentType,
                "createUser": createUser
            },
            type: "POST",
            dataType: "json",
            success: function (result, status, xhr) {
                alertInfo("提示", result.msg, function () {
                    if (result.flag == true) {
                        searchStroke();
                    }
                });
            },
            error: function (xhr, status, error) {

            }
        });
    });
}
$(function(){

});