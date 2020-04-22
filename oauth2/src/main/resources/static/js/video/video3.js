$(function () {
    $(".video-content-container").on("click","#submitVote", function () {
        // 获取 CSRF Token
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");

        $.ajax({
            url: '/votes',
            type: 'POST',
            data:{"videoId":videoId},
            beforeSend: function(request) {
                request.setRequestHeader(csrfHeader, csrfToken); // 添加  CSRF Token
            },
            success: function(data){
                if (data.success) {
                    $.ajax({
                       url: '/video/' + videoId,
                       data:{"async":true},
                       success: function (data) {
                           $("#abc").html(data);
                       }
                    })
                } else {
                    toastr.error(data.message);
                }
            },
            error : function() {
                toastr.error("error!");
            }
        });
    });
    $(".video-content-container").on("click","#cancelVote", function () {
        // 获取 CSRF Token
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");

        $.ajax({
            url: '/votes/'+ $(this).attr('voteId')+ '?videoId='+ videoId,
            type: 'DELETE',
            beforeSend: function(request) {
                request.setRequestHeader(csrfHeader, csrfToken); // 添加  CSRF Token
            },
            success: function(data){
                if (data.success) {
                    $.ajax({
                        url: '/video/' + videoId,
                        data:{"async":true},
                        success: function (data) {
                            $("#abc").html(data);
                        }
                    })
                } else {
                    toastr.error(data.message);
                }
            },
            error : function() {
                toastr.error("error!");
            }
        });
    });
    $(".video-content-container").on("click","#submitComment", function () {
        // 获取 CSRF Token
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");

        $.ajax({
            url: '/comments',
            type: 'POST',
            data:{"videoId":videoId, "commentContent":$('#commentContent').val()},
            beforeSend: function(request) {
                request.setRequestHeader(csrfHeader, csrfToken); // 添加  CSRF Token
            },
            success: function(data){
                if (data.success) {
                    $('#commentContent').val('');
                    getCommnet(videoId);
                    $.ajax({
                        url: '/video/' + videoId,
                        data:{"async":true},
                        success: function (data) {
                            $("#abc").html(data);
                        }
                    })
                } else {
                    toastr.error(data.message);
                }
            },
            error : function() {
                toastr.error("error!");
            }
        });
    });
    $(".video-content-container").on("click",".video-delete-comment", function () {
        // 获取 CSRF Token
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");

        $.ajax({
            url: '/comments/'+$(this).attr("commentId")+'?videoId='+videoId,
            type: 'DELETE',
            beforeSend: function(request) {
                request.setRequestHeader(csrfHeader, csrfToken); // 添加  CSRF Token
            },
            success: function(data){
                if (data.success) {
                    getCommnet(videoId);
                    $.ajax({
                        url: '/video/' + videoId,
                        data:{"async":true},
                        success: function (data) {
                            $("#abc").html(data);
                        }
                    })
                } else {
                    toastr.error(data.message);
                }
            },
            error : function() {
                toastr.error("error!");
            }
        });
    });

    getCommnet(videoId);
    function getCommnet(videoId) {
        // 获取 CSRF Token
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");

        $.ajax({
            url: '/comments',
            type: 'GET',
            data:{"videoId":videoId},
            beforeSend: function(request) {
                request.setRequestHeader(csrfHeader, csrfToken); // 添加  CSRF Token
            },
            success: function(data){
                $("#mainContainer").html(data);

            },
            error : function() {
                toastr.error("error!");
            }
        });
    }
});