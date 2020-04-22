package li.changlin.video.service;

import com.github.pagehelper.Page;
import li.changlin.video.entity.Video;

public interface VideoService {
    void addVideo(Video video);

    void updateById(Video video);

    void removeVideo(Integer id);

    Video getVideoById(Integer id);

    Page<Video> listAllWithPage();
}
