$(function () {
    var timer = null,index = 0;
    var $image;

    function resume() {
        $(".imageCover").css("display","block");
    }
    setInterval(resume,4000);
    function startAutoPlay(){
        timer = setInterval(function(){
            if(index>2){
                index = 0;
            }
            changeImg();
            index++;
        },400);
    }
    function stopAutoPlay(){
        if (timer) {
            clearInterval(timer);
        }
    }
    function changeImg(){
        $image.find(".imageCover").css("display","none");
        $image.find(".imageHover a").css("display","none");
        $image.find(".imageHover a")[index].style.display = "block";
    }

    $("#mainContainer").off("mouseover",".imageAll").on("mouseover",".imageAll",function () {
        var imageID = $(this).attr("id");
        $image = $('#'+imageID);
        startAutoPlay();
    });
    $("#mainContainer").off("mouseout",".imageAll").on("mouseout",".imageAll",function () {
        stopAutoPlay()});
});