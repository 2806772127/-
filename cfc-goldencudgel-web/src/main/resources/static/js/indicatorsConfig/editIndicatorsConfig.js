/*$(function(){
    $("#RATING_DESCRIBE").val($("#ratings").val());
});*/

function saves() {
	
	var rate=  $("#rate").val();
	
    var re = /^\d+(?=\.{0,1}\d+$|$)/;
    if (rate != "") { 
        if (!re.test(rate)) { 
         alertInfo("提示","比例只能輸入數字!");  
         return false;
        } 
    }
	
    saveCommon("/indicatorsConfig/saveIndicators","/indicatorsConfig/viewIndicatorsConfig");
}
