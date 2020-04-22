package li.changlin.goodvideo.feignclient;

import li.changlin.user.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient(value = "oauth2")
public interface UserFeignClient {
    @RequestMapping(value = "/getUser",method = RequestMethod.GET )
    @ResponseBody
    User getUser(@RequestParam(value = "username")String username);
}