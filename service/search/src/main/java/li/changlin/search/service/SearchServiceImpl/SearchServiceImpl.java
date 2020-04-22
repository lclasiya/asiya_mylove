package li.changlin.search.service.SearchServiceImpl;

import li.changlin.common.utils.VideoAnalysis;
import li.changlin.search.entity.EsVideo;
import li.changlin.search.feignclient.VideoFeignClient;
import li.changlin.search.mapper.SearchMapper;
import li.changlin.search.service.SearchService;
import li.changlin.video.entity.Video;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private SearchMapper sm;
    @Autowired
    private VideoFeignClient vfc;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Optional<EsVideo> getEsVideoByVideoid(Integer id) {
        return sm.findById(id);
    }

    @Override
    public List<EsVideo> findByUserName(String username) {
        return sm.findByUserNameOrderByCreateTimeDesc(username);
    }

    @Override
    public void removeEsVideoById(Integer id) {
        sm.deleteById(id);
    }

    @Override
    public EsVideo saveEsVideo(EsVideo esvideo) {
        return sm.save(esvideo);
    }

    @Override
    public void videoDataTask() {
        Iterable<EsVideo> all = sm.findAll();
        String sql = "update video set read_size=?,vote_size=?,comment_size=? where id=?;";
        for (EsVideo esVideo : all) {
            jdbcTemplate.update(sql, esVideo.getReadSize(), esVideo.getVoteSize(), esVideo.getCommentSize(), esVideo.getVideoID());
        }
    }

    @Override
    public Page<EsVideo> findEsVideo(String videolength, String object, String videoqua, String tags, Pageable pageable) {
        return sm.findByNagasaLikeAndObjectLikeAndGashitsuLikeAndTagsLike(videolength,object,videoqua,tags,pageable);
    }

    @Override
    public void addEsVideoFromVideo(Integer id) throws IOException {
        Video video = vfc.getVideoFrom(id);
        EsVideo esVideo = buildEsVideo(video);
        sm.save(esVideo);
    }

    @Override
    public EsVideo buildEsVideo(Video video) {
        int abc = Integer.parseInt(video.getVideoQua());
        long def = VideoAnalysis.length;
        EsVideo esVideo = new EsVideo();
        esVideo.setCoverImage(video.getCoverImage());
        esVideo.setCreateTime(video.getCreateTime());
        esVideo.setVideoQua(video.getVideoQua());
        esVideo.setVideoLength(video.getVideoLength());
        esVideo.setObject(video.getObject());
        esVideo.setTags(video.getTags());
        esVideo.setTitle(video.getTitle());
        esVideo.setImages(video.getImages());
        esVideo.setImagesList(Arrays.asList(video.getImages().split(",")));
        esVideo.setUserName(video.getUserName());
        esVideo.setVideoID(video.getId());
        esVideo.setVideoUrl(video.getVideoUrl());
        if (abc<361){
            esVideo.setGashitsu("dim");
        }else if (abc>360 && abc<601){
            esVideo.setGashitsu("fuzzy");
        }else {
            esVideo.setGashitsu("clear");
        }
        if (def<=600){
            esVideo.setNagasa("short");
        }else if (def>600 && def<1200){
            esVideo.setNagasa("middle");
        }else {
            esVideo.setNagasa("long");
        }

        return esVideo;

    }
}
