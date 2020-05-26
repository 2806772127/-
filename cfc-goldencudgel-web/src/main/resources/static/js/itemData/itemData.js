$(function(){
    //findRole(1);
});

function  findRole(curPage) {
    //var data = $("#viewData_form").serialize();
    if(curPage!="" && curPage!='undefined'){
        curPage = curPage;
    }else{
        curPage = 1;
    }
    var dictId  = $("#dictId option:selected").val();
    
    if(dictId=='nothing') {
    	
    	 alertInfo("提示","請選擇數據名稱！",function () {
         });
         return ;
    	
    }

    $.ajax({
        url:context_path +"/itemData/queryItemData",
        type:'post',
        data: {"dictId":dictId,"curPage":curPage},
        datatype: 'html',
        success:function (result) {
            $("#dataListDiv").empty();
            $("#dataListDiv").html(result);
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

    var code = $(obj).attr("mainCode");
    var name = $(obj).attr("mainName");
    var viewCode = $(obj).attr("viewCode");
    var viewName = $(obj).attr("viewName");
    $("#mainItemCode").val(code);
    $("#mainItemName").val(name);
    $("#viewCode").val(viewCode);
    $("#viewName").val(viewName);
    $("#oldCode").val(viewCode);


    openDialog("changeWindow", "修改附件設置", 600, 200);
}

function closeChange() {
    $("#changeWindow").dialog('close');
}

/*function deleteWin(obj){

        if (confirm("您確定刪除嗎？")){

            var code = $(obj).attr("mainCode");
            var viewCode = $(obj).attr("viewCode");
            $.ajax({
                url:context_path +"/itemData/deleteItemData",
                type:'post',
                data: {"dictId":code,"itemCode":viewCode},
                datatype: 'json',
                success:function (data) {
                    alertInfo("提示","刪除成功！",function () {;
                        findRole(1);
                    });
                    // alert("DELETE SUCCESS");
                    // findRole(1);

                },
                error:function (XMLHttpRequest, textStatus, errorThrown) {
                    alert(textStatus + " " + XMLHttpRequest.readyState + " "
                        + XMLHttpRequest.status + " " + errorThrown);
                }
            })

        }
        else{}
}*/

function deleteWin(obj){
	
	 alertInfoCOPY("提示","請確認是否刪除？",function deleteSelectRole() {
        var code = $(obj).attr("mainCode");
        var viewCode = $(obj).attr("viewCode");
        $.ajax({
            url:context_path +"/itemData/deleteItemData",
            type:'post',
            data: {"dictId":code,"itemCode":viewCode},
            datatype: 'json',
            success:function (data) {
                alertInfo("提示","刪除成功！",function () {;
                	 findRole(1);
                });
                // alert("DELETE SUCCESS");
                // findRole(1);

            },
            error:function (XMLHttpRequest, textStatus, errorThrown) {
                alert(textStatus + " " + XMLHttpRequest.readyState + " "
                    + XMLHttpRequest.status + " " + errorThrown);
            }
        })

    })
   
}

function saveItem() {
    var viewCode = $("#viewCode").val();
    var viewName = $("#viewName").val();
    var dictId = $("#mainItemCode").val();
    var dictName = $("#mainItemName").val();
    var olditemCode = $("#oldCode").val();
    
    if(viewCode.trim()=="" || viewName.trim()==""){
        alertInfo("提示","參數不能為空！",function () {
        });
        return ;
    }

   var data = {"itemCode": viewCode, "itemName": viewName, "dictId": dictId, "dictName": dictName, "olditemCode": olditemCode};
   $.ajax({
       url:context_path +"/itemData/saveItemData",
       type:'post',
       data: data,
       datatype: 'json',
       success:function (data) {
           alertInfo("提示","儲存成功！",function () {
               closeChange();
               $("#changeWindow").remove();
               // var curPage  = $("#page_select option:selected").val();
               // findRole(curPage);
               //刷新页面 显示修改后数据
               window.location.href = "/itemData/viewItemData";
           });
       },
       error:function (XMLHttpRequest, textStatus, errorThrown) {
           alert(textStatus + " " + XMLHttpRequest.readyState + " "
               + XMLHttpRequest.status + " " + errorThrown);
       }
   });

}

function openNewWin(){
    openDialog("newWindow", "新增附件設置", 600, 200);
}

function closenew() {
    $("#newWindow").dialog('close');
}

function saveNewItem() {

    if($("#newviewCode").val().trim()=="" || $("#newviewName").val().trim()==""){
        alertInfo("提示","參數不能為空！",function () {
        });
        return ;
    }

    var dictId  = $("#newdictId option:selected").val();
    $("#newMainCode").val(dictId);
    var data =  $("#newItemData").serialize();
    $.ajax({
        url:context_path +"/itemData/saveNewItemData",
        type:'post',
        data: data,
        datatype: 'json',
        success:function (data) {
            if(data == "success"){
                alertInfo("提示","儲存成功！",function () {
                    $("#newviewCode").val("");
                    $("#newviewName").val("");
                    $("#newWindow").dialog('close');
                    window.location.href = "/itemData/viewItemData";
                });
            } else {
                alertInfo_1("提示","該編號已經存在，請重新輸入");
            }
        },
        error:function (XMLHttpRequest, textStatdus, errorThrown) {
            alert(textStatus + " " + XMLHttpRequest.readyState + " "
                + XMLHttpRequest.status + " " + errorThrown);
        }
    })
}

function  findPage(curPage) {
    findRole(curPage);
}