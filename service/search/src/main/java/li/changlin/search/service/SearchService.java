package li.changlin.search.service;

import li.changlin.search.entity.EsVideo;
import li.changlin.user.entity.User;
import li.changlin.video.entity.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface SearchService {
    Optional<EsVideo> getEsVideoByVideoid(Integer id);

    List<EsVideo> findByUserName(String username);

    void  removeEsVideoById(Integer id);

    EsVideo saveEsVideo(EsVideo esvideo);

    void videoDataTask();

    Page<EsVideo> findEsVideo(String videolength, String object, String videoqua, String tags, Pageable pageable);

    void addEsVideoFromVideo(Integer id) throws IOException;

    EsVideo buildEsVideo(Video video);

    List<EsVideo> getHotVideos();

    List<User> getHotUsers();
}
