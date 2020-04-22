package li.changlin.search.task;

import li.changlin.search.entity.EsVideo;
import li.changlin.search.mapper.SearchMapper;
import li.changlin.search.service.SearchService;
import li.changlin.video.entity.Video;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Set;

@Configuration
public class VideoDataTask extends QuartzJobBean {
    @Autowired
    SearchService ss;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    JdbcTemplate jdbcTemplate;
    private static final String VIDEO_IDENTITY ="VideoTaskQuartz";

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        ss.videoDataTask();
        if (stringRedisTemplate.hasKey("video_failed")) {
            Set<String> video_failed = stringRedisTemplate.opsForSet().members("video_failed");
            String sql = "select * from video where id=?;";
            for (String videoId : video_failed){
                Video video = jdbcTemplate.queryForObject(sql, Video.class, videoId);
                EsVideo esVideo = ss.buildEsVideo(video);
                ss.saveEsVideo(esVideo);
                stringRedisTemplate.opsForSet().remove("video_failed",videoId);
            }
        }
    }
    @Bean
    public JobDetail quartzDetail(){
        return JobBuilder.newJob(VideoDataTask.class).withIdentity(VIDEO_IDENTITY).storeDurably().build();
    }

    @Bean
    public Trigger quartzTrigger(){
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
       //         .withIntervalInSeconds(5)
                .withIntervalInHours(2)
                .repeatForever();
        return TriggerBuilder.newTrigger().forJob(quartzDetail())
                .withIdentity(VIDEO_IDENTITY)
                .withSchedule(scheduleBuilder)
                .build();
    }
}
