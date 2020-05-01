package li.changlin.search.service.SearchServiceImpl;

import li.changlin.common.utils.VideoAnalysis;
import li.changlin.search.entity.EsVideo;
import li.changlin.search.feignclient.UserFeignClient;
import li.changlin.search.feignclient.VideoFeignClient;
import li.changlin.search.mapper.SearchMapper;
import li.changlin.search.service.SearchService;
import li.changlin.user.entity.User;
import li.changlin.video.entity.Video;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private SearchMapper sm;
    @Autowired
    private VideoFeignClient vfc;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    UserFeignClient ufc;
    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;

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

    @Override
    public List<EsVideo> getHotVideos() {
        Page<EsVideo> esVideoPage=null;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.Direction.DESC, "readSize", "commentSize", "voteSize", "createTime");
        esVideoPage = sm.findAll(pageRequest);
        List<EsVideo> esVideos = esVideoPage.getContent();
        return esVideos;
    }

    @Override
    public List<User> getHotUsers() {
        List<User> userList = new ArrayList<>();
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchAllQuery())
                .withSearchType(SearchType.QUERY_THEN_FETCH)
                .withIndices("video").withTypes("docs")
                .addAggregation(AggregationBuilders.terms("userList").field("userName").order(Terms.Order.count(false)).size(3))
                .build();
        Aggregations aggregations = elasticsearchTemplate.query(searchQuery, new ResultsExtractor<Aggregations>() {
            @Override
            public Aggregations extract(SearchResponse response) {
                return response.getAggregations();
            }
        });
        StringTerms modelTerms =  (StringTerms)aggregations.asMap().get("userList");
        Iterator<StringTerms.Bucket> modelBucketIt = modelTerms.getBuckets().iterator();
        while (modelBucketIt.hasNext()) {
            StringTerms.Bucket eachBucket = modelBucketIt.next();
            userList.add(ufc.getUser(eachBucket.getKey().toString()));
        }
        return userList;
    }
}
