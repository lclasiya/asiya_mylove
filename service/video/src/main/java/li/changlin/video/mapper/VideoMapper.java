package li.changlin.video.mapper;

import com.github.pagehelper.Page;
import li.changlin.user.entity.User;
import li.changlin.video.entity.Video;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoMapper {
    void addVideo(Video video);

    void updateById(Video video);

    void removeVideo(Integer id);

    Video getVideoById(Integer id);

    Page<Video> listAllWithPage();
}
