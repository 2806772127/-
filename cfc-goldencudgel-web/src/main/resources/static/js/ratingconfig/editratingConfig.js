$(function(){
    $("#RATING_DESCRIBE").val($("#ratings").val());
});

function saves() {
    saveCommon("/ratingConfig/saveRating","/ratingConfig/viewRatingConfig");
}
