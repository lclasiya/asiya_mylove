package li.changlin.search.feignclient;

import li.changlin.user.entity.User;
import li.changlin.video.entity.Video;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "oauth2",fallback = UserFeignFallback.class)
public interface UserFeignClient {
    @RequestMapping(value = "/getUser",method = RequestMethod.GET )
    @ResponseBody
    User getUser(@RequestParam(value = "username")String username);
}
