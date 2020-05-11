package li.changlin.oauth2.controller;

import java.security.Principal;

import li.changlin.oauth2.mapper.UsersMapper;
import li.changlin.oauth2.service.UsersService;
import li.changlin.oauth2.utils.ZookeeperDistributedLock;
import li.changlin.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class UserController {
    @Autowired
    UsersService us;
    @Autowired
    UsersMapper um;
    int a;

    @RequestMapping("/oauth2user")
    public Principal user(Principal principal) {
        System.out.println(principal);
        return principal;
    }

    /*@Value("${zookeeperTest}")
    private String zookeeperTest;
    @RequestMapping("/config")
    public String getconfig(){
        return zookeeperTest;
    }*/
    /*
        悲观锁
     */
    @RequestMapping("/getNum")
    @Transactional
    public void getnum(){
        User userById = um.selectForLock(9);
        userById.setLovers(userById.getLovers()+1);
        us.updateById(userById);
        a++;
        System.out.println("目前存储"+a+"个");
    }
    @RequestMapping("/getZkNum")
    public void getzknum(){
        ZookeeperDistributedLock zookeeperDistributedLock = new ZookeeperDistributedLock("/getzknum");
        try {
            zookeeperDistributedLock.lock();
            User userById = um.selectForLock(9);
            userById.setLovers(userById.getLovers()+1);
            us.updateById(userById);
            a++;
            System.out.println("目前存储"+a+"个");
        } finally {
            zookeeperDistributedLock.unlock();
        }

    }

}
