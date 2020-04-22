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
        $(".deleteDiv").css("display","block");
    });
    $("#coverSubmit").click(function () {
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");
        var data = new FormData($('#coverForm')[0]);
        files = $('#coverImage').prop('files');
        var url = URL.createObjectURL(files[0]);

            $.ajax({
            url:"/coverImage",
            type:'POST',
            data:data,
            cache: false,
            processData: false,
            contentType: false,
            beforeSend: function(request) {
                request.setRequestHeader(csrfHeader, csrfToken); // 添加  CSRF Token
            },
            success:function (data) {
                $("#uploadCover").val(data);
                $("#coverSubmit").html("提出済");
                $("#coverPreview img").attr("src",url)
            },
            error : function() {
                toastr.error("error!");
            }
        });
        return false;
    })

});