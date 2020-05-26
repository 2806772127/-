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

function showImg(obj) {
    var imgName = $(obj).attr("imgName");
    var imgDate = $(obj).attr("imgDate");
    var imgId = $(obj).attr("imgId");
    var imgType = $(obj).attr("imgType");
    var suffix = imgName.substr(imgName.lastIndexOf("."));
    if (suffix==".pdf"){
        window.open("/fileService/"+imgId, "_blank")
        return false;
    }
    bodymask("搜索中...");
    $.ajax({
        url :context_path + "/collectionQuery/showView",
        type: "post",
        data:{"id":imgId},
        success :function (filesrc) {
            bodyunmask();
            if(imgType=="1"){
                $("#vedioCon").attr("src",filesrc);
                // 重新加载音频
                $("#vedioCon").parent().load();
                $("#videoName").val(imgName);
                $("#videoDate").val(imgDate);
                $("#musicorvideo").dialog({
                    title : "音頻文件",
                    width : 525,
                    height : 325,
                    modal : true,
                    draggable : false,
                    closeOnEscape:false,
                    open:function(event,ui){$(".ui-dialog-titlebar-close").hide();}
                });
            }else{
                openDialog("musicorimg","圖片文件",650,665);
                $("#phName").val(imgName);
                $("#phDate").val(imgDate);
                showPhoto(filesrc);
            }
        },error: function(XMLHttpRequest, textStatus, errorThrown) {
            bodyunmask();
            alertInfo("提示","查找附件失敗");
        }
    });
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
//     //旋转
//     var spin_n = 0;
//     $(".mask-clockwise").click(function () {
//         spin_n += 15;
//         $(".mask-layer-imgbox img").parent("p").css({
//             "transform": "rotate(" + spin_n + "deg)",
//             "-moz-transform": "rotate(" + spin_n + "deg)",
//             "-ms-transform": "rotate(" + spin_n + "deg)",
//             "-o-transform": "rotate(" + spin_n + "deg)",
//             "-webkit-transform": "rotate(" + spin_n + "deg)"
//         });
//     });
//     $(".mask-counterclockwise").click(function () {
//         spin_n -= 15;
//         $(".mask-layer-imgbox img").parent("p").css({
//             "transform": "rotate(" + spin_n + "deg)",
//             "-moz-transform": "rotate(" + spin_n + "deg)",
//             "-ms-transform": "rotate(" + spin_n + "deg)",
//             "-o-transform": "rotate(" + spin_n + "deg)",
//             "-webkit-transform": "rotate(" + spin_n + "deg)"
//         });
//     });
}
