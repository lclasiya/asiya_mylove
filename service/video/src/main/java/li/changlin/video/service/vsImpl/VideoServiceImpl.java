package li.changlin.video.service.vsImpl;

import com.github.pagehelper.Page;
import li.changlin.video.entity.Video;
import li.changlin.video.mapper.VideoMapper;
import li.changlin.video.service.VideoService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideoMapper vm;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    @Transactional
    public void addVideo(Video video) {
        Map<String,Object> map=new HashMap<>();
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String sessionId = attributes.getRequest().getRequestedSessionId();
        System.out.println(sessionId);
        vm.addVideo(video);
        System.out.println(video.getId());
        map.put("sessionId",sessionId);
        map.put("videoId",video.getId());
        rabbitTemplate.convertAndSend("asiya.video.exchange","video.save",map,new CorrelationData(video.getId().toString()));

    }

    @Override
    public void updateById(Video video) {
         vm.updateById(video);
    }

    @Override
    public void removeVideo(Integer id) {
        Map<String,Object> map=new HashMap<>();
        map.put("videoId",id);
        vm.removeVideo(id);
        rabbitTemplate.convertAndSend("asiya.video.exchange","video.delete",map);
    }

    @Override
    public Video getVideoById(Integer id) {

        return vm.getVideoById(id);
    }

    @Override
    public Page<Video> listAllWithPage() {
        return vm.listAllWithPage();
    }
}
