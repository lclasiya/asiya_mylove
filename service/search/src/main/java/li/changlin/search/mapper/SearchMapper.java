package li.changlin.search.mapper;

import li.changlin.search.entity.EsVideo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface SearchMapper extends ElasticsearchRepository<EsVideo,Integer> {
    Page<EsVideo> findByNagasaLikeAndObjectLikeAndGashitsuLikeAndTagsLike
            (String videolength, String object, String videoqua, String tags, Pageable pageable);
    List<EsVideo> findByUserNameOrderByCreateTimeDesc(String username);
}
