var videoUrl="";
var videoMsg=new Map();
var a=0;
var images=new Array();

$(function () {
    var _pageSize;
    function getVideoByName(pageIndex, pageSize) {
        $.ajax({
            url: "/ero",
            contentType : 'application/json',
            data:{
                "async":true,
                "pageIndex":pageIndex,
                "pageSize":pageSize,
                "keyword":$("#indexkeyword").val()
            },
            success: function(data){
                $("#mainContainer").html(data);
            },
            error : function() {
                toastr.error("error!");
            }
        });
    }
    $.tbpage("#mainContainer", function (pageIndex, pageSize) {
        getVideoByName(pageIndex, pageSize);
        _pageSize = pageSize;
    });

    $("#uploadSubmit").click(function () {
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");
        for (var i=0;i<videoMsg.size;i++) {
            if (Object.keys(videoMsg.get(i)).length != 0){
                vm=videoMsg.get(i);
                break;
            }
        }
        $.ajax({
            url: "/ero/submit",
            type: 'POST',
            contentType: "application/json; charset=utf-8",
            data:JSON.stringify({"title":$('#title').val(),
                "tags": $('#tags').val(),
                "object": $('#radioSelect input[name=\'object\']:checked').val(),
                "images": images.toLocaleString(),
                "videoUrl": videoUrl,
                "videoLength":vm["videoLength"],
                "videoQua":vm["videoQua"],
                "coverImage":$('#uploadCover').val()
            }),
            beforeSend: function(request) {
                request.setRequestHeader(csrfHeader, csrfToken); // 添加  CSRF Token
            },
            success: function(data){
                if (data.success) {
                    // 成功后，重定向
                    window.location = data.body;
                } else {
                    toastr.error("error!"+data.message);
                }

            },
            error : function() {
                toastr.error("error!");
            }
        });
    });
    $("#videoDelete").click(function () {
        if ($(".deleteDiv").css("display") == "inline") {
            $(".deleteDiv").css("display","none");
        }else {
            $(".deleteDiv").css("display","inline");
        }
    });
    $("#userComment").on("click","#sukida",function () {
        $.ajax({
            url:"/loversEdit",
            type:"POST",
            data:{"username":videouser,"num":1},
            success:function (data) {
                if(data){
                    toastr.success("愛が無事に伝えたよ");
                var html = "<a href='javascript:void(0)' id='kiraida' title='もう嫌だ' style='color: #888888' class='ml-4'>"+
                        "<i class='fa fa-thumbs-down fa-2x'></i>"+
                        "</a>";
                    $("#userComment").html(html)
                }else {
                    toastr.error("ごめん、後でもう一回伝えてね")
                }
            },
            error :function () {
                toastr.error("error!");
            }
        });
    });
    $("#userComment").on("click","#kiraida",function () {
        $.ajax({
            url:"/loversEdit",
            type:"POST",
            data:{"username":videouser,"num":-1},
            success:function (data) {
                if(data){
                    toastr.info("無事に振った!");
                    var html = "<a href='javascript:void(0)' id='sukida' title='好き' style='color: #BD362F' class='ml-4'>"+
                        "<i class='fa fa-heart fa-2x'></i>"+
                        "</a>";
                    $("#userComment").html(html)
                }else {
                    toastr.error("ごめん、後でもう一回伝えてね")
                }
            },
            error :function () {
                toastr.error("error!");
            }
        });
    });

});