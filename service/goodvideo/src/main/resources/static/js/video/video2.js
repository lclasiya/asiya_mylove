$(function () {
    $("#videoLength,#videoObject,#videoQuality").change(function () {
        $.ajax({
            url: "/ero",
            contentType : 'application/json',
            data:{
                "async":true,
                "nagasa":$("#videoLength").val(),
                "object":$("#videoObject").val(),
                "tags":$("#tagoption li a.tag-a").attr("value"),
                "gashitsu":$("#videoQuality").val()
            },
            success: function(data){
                $("#mainContainer").html(data);
            },
            error : function() {
                toastr.error("error!");
            }
        });
    });
    $("#tagoption li a").click(function () {
        $("#tagoption li a").removeClass("tag-a");
        $(this).addClass("tag-a");
        var flkwej= $(this).attr("value");
        $.ajax({
            url:"/ero",
            contentType : 'application/json',
            data:{
                "async":true,
                "nagasa":$("#videoLength").val(),
                "object":$("#videoObject").val(),
                "tags":$(this).attr("value"),
                "gashitsu":$("#videoQuality").val()
            },
            success: function(data){
                $("#mainContainer").html(data);
            },
            error : function() {
                toastr.error("error!");
            }
        })
    });

});