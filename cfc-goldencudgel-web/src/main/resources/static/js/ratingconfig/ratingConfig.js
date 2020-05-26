$(function(){
    //initDate();
	 $("#startImg").click(function() {
	        $("input[id='startDate']").focus();
	    });
	    $("#startImgs").click(function() {
	        $("input[id='startDates']").focus();
	    });


	    $("#endImg").click(function() {
	        $("input[id='endDate']").focus();
	    });
	    $("#endImgs").click(function() {
	        $("input[id='endDates']").focus();
	    });
	    $("input[id='startDate']").datetimepicker({
	        onClose: function(selectedDate) {
	            $("input[id='endDate']").datepicker("option", "minDate", selectedDate);
	        }
	    });
	    $("input[id='endDate']").datetimepicker({
	        onClose: function(selectedDate) {
	            $("input[id='startDate']").datepicker("option", "maxDate", selectedDate);
	        }
	    });
	    $("input[id='startDates']").datetimepicker({
	        onClose: function(selectedDate) {
	            $("input[id='endDates']").datepicker("option", "minDate", selectedDate);
	        }
	    });
	    $("input[id='endDates']").datetimepicker({
	        onClose: function(selectedDate) {
	            $("input[id='startDates']").datepicker("option", "maxDate", selectedDate);
	        }
	    });
	
	
    queryRatingconfig(1);
});

function  queryRatingconfig(curPage) {
    queryDataCommon(curPage,"/ratingConfig/queryRatingConfigConfig","abilibty_list");
}

function  viewIncomDetail(obj) {
    var configid = $(obj).attr("compilationNo");
    window.location.href = context_path + "/ratingConfig/seeRating?configid="+configid+getSearchData();
}

function editIncom(obj) {
    var configid = $(obj).attr("compilationNo");
    window.location.href = context_path + "/ratingConfig/editRating?configid="+configid+getSearchData();
}

function  findPage(curPage) {
    queryRatingconfig(curPage);
}
