package li.changlin.search.feignclient;

import li.changlin.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserFeignFallback implements UserFeignClient{
    @Override
    public User getUser(String username) {
        System.out.println("User服务降级启动");
        return null;
    }
}
