package li.changlin.search.feignclient;

import li.changlin.search.config.FeignRequestInterceptor;
import li.changlin.search.listener.SearchMqListener;
import li.changlin.video.entity.Video;
import li.changlin.video.feign.VideoFeign;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "video",configuration = FeignRequestInterceptor.class)
public interface VideoFeignClient /*extends VideoFeign*/ {
    @RequestMapping(value = "/video/{id}",method = RequestMethod.GET )
    @ResponseBody
    Video getVideoFrom(@PathVariable("id")Integer id);
}
