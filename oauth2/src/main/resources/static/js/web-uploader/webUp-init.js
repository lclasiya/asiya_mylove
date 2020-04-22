
$(function () {
    var $list = $('#thelist'),
        $btn = $('#ctlBtn'),
        state = 'pending',
        uploader = null;

    uploader = WebUploader.create({

        // 不压缩image
        resize: false,

        // 为true 文件则自动上传
        auto: false,

        swf: '/js/web-uploader/Uploader.swf',

        // 文件接收服务端。就是上传文件走的url
        server: '/video/upload',
        threads:'30',
        fileNumLimit:'10',
        fileSingleSizeLimit: 10000 * 1024 * 1024,
        // 选择文件的按钮。可选。
        pick: '#picker',

        accept: {
            title: 'Images',
            extensions: 'gif,jpg,jpeg,bmp,png,3gp,mp4,rmvb,mov,avi,m4v',
            mimeTypes: 'image/*,video/*'
        }
    });

    // 当有文件添加进来的时候
    uploader.on( 'fileQueued', function( file ) {
        $li = $(
            '<div id="' + file.id + '" class="file-item thumbnail">' +
            '<img>' +
            '<div class="info">' + file.name + '</div>' +
            '<p class="state">スタンバイ</p>'+
            '</div>'
        );
            $img = $li.find('img');


        // $list为容器jQuery实例
        $list.append( $li );
        uploader.makeThumb( file, function( error, src ) {
            if ( error ) {
                return;
            }
            $img.attr( 'src', src );
        }, 100, 100 ); //100x100为缩略图尺寸
    });

    // 文件上传过程中创建进度条实时显示。
    // 进度条我引用了bootStrap.css来进行展示,webuploader.css是不带的
    uploader.on( 'uploadProgress', function( file, percentage ) {
        var $li = $( '#'+file.id ),
            $percent = $li.find('.progress .progress-bar');

        // 避免重复创建
        if ( !$percent.length ) {
            $percent = $('<div class="progress">' +
                '<div class="progress-bar bg-warning progress-bar-striped"  style="width: 0%">' +
                '</div>' +
                '</div>').appendTo( $li ).find('.progress-bar');
        }

        $li.find('p.state').text('アップロード中');

        $percent.css( 'width', percentage * 100 + '%' );
    });

    // 上传成功
    uploader.on( 'uploadSuccess', function( file,response ) {
        $( '#'+file.id ).find('p.state').text('アップロード済み');
        videoUrl += response["videoUrl"];
        videoMsg.set(a++,response["videoMsg"]);
        if (response["imageUrl"] != "") {
            images.push(response["imageUrl"])
        }
    });

    // 上传失败
    uploader.on( 'uploadError', function( file ) {
        $( '#'+file.id ).find('p.state').text('アップロード失敗');
    });

    uploader.on( 'uploadComplete', function( file ) {
        $( '#'+file.id ).find('.progress').fadeOut();
    });

    uploader.on( 'all', function( type ) {
        if ( type === 'startUpload' ) {
            state = 'uploading';
        } else if ( type === 'stopUpload' ) {
            state = 'paused';
        } else if ( type === 'uploadFinished' ) {
            state = 'done';
        }

        if ( state === 'uploading' ) {
            $btn.text('一旦停止');
        } else {
            $btn.text('アップロード開始');
        }
    });
    $btn.on( 'click', function() {
        if ( state === 'uploading' ) {
            uploader.stop(true);
        } else {
            uploader.upload();
        }
    });

});
