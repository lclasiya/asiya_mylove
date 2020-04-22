$(function () {
    $(".video-content-container").on("click","#submitVote", function () {
        $.ajax({
            url: 'http://localhost:8766/votes',
            type: 'POST',
            data:{"videoId":videoID,"username":username},
            success: function(data){
                if (data.success) {
                    $.ajax({
                       url: '/video/' + videoID,
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
        $.ajax({
            url: 'http://localhost:8766/votesDel',
            data:{"videoId":videoID,"username":username},
            type: 'POST',
            success: function(data){
                if (data.success) {
                    $.ajax({
                        url: '/video/' + videoID,
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
        $.ajax({
            url: 'http://localhost:8766/comments',
            type: 'POST',
            data:{"videoId":videoID, "commentContent":$('#commentContent').val(),"username":username},
            success: function(data){
                if (data.success) {
                    $('#commentContent').val('');
                    getComment(videoID);
                    $.ajax({
                        url: '/video/' + videoID,
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
        $.ajax({
            url: 'http://localhost:8766/commentsDel',
            type: 'POST',
            data:{"videoId":videoID,"username":username,"icon":$(this).attr("icon")},
            success: function(data){
                if (data.success) {
                    getComment(videoID);
                    $.ajax({
                        url: '/video/' + videoID,
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
    getComment(videoID);
    function getComment(videoID) {
        $.ajax({
            url: '/listcomments',
            type: 'GET',
            cache:false,
            data:{"videoId":videoID},
            success: function(data){
                $("#mainContainer").html(data);
            },
            error : function() {
                toastr.error("コメント読み込み失敗");
            }
        });
    }
});