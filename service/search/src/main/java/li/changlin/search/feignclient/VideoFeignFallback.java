package li.changlin.search.feignclient;

import li.changlin.video.entity.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoFeignFallback implements VideoFeignClient{
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Override
    public Video getVideoFrom(Integer id) {
        System.out.println("服务被降级，使用jdbc获取video");
        String sql = "select * from video where id=?;";
        List<Video> videoList = jdbcTemplate.query(sql,new Object[]{id} ,new BeanPropertyRowMapper<>(Video.class));
        return videoList.get(0);
    }

}
