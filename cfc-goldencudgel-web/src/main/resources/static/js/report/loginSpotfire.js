/*$(function() {
    getLoginPage();
})*/
var isLogin = false;
function getCookie(name) {
    var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
    if (arr = document.cookie.match(reg)) {
        return unescape(arr[2]);
    }

    else {
        return null;
    }
}

function getLoginPage() {
    $.ajax({
        type : "get",
        dataType : "html",
        url : "/spotfire/login.html",
        success : function(data) {
            console.info("获取spotfire登录页面成功");
            loginSpotfire();
        },
        beforeSend : function() {
        },
        complete : function(data, status) {
            console.info(data + " " + status);
        }
    });
}

function loginSpotfire() {
    var spotfireParam = $("#spotfireParam").val();
    var data = $.parseJSON(spotfireParam);
    var xsrf = getCookie("XSRF-TOKEN");
    if ($("#sp_user").val() == "" || $("#sp_pwd").val() == "") {
        return;
    }

    $.ajax({
        url : "/spotfire/sf_security_check",
        method : "POST",
        data : data,
        async : false,
        dataType : "json",
        beforeSend : function(request) {
            request.setRequestHeader("Access-Control-Allow-Origin", "*");
            if (xsrf != null) {
                request.setRequestHeader("X-XSRF-TOKEN", xsrf);
            }
        },
        success : function(response) {
            console.info("spotfire登录成功");
        },
        error : function(e) {
            if (e.status == 200) {
                console.info("spotfire登录成功");
            } else {
                e.error("spotfire登录失败");
            }

        }
    });
}
