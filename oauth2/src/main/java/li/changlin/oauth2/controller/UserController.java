package li.changlin.oauth2.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class UserController {
    @Value("${zookeeperTest}")
    private String zookeeperTest;

    @RequestMapping("/oauth2user")
    public Principal user(Principal principal) {
        System.out.println(principal);
        return principal;
    }
    @RequestMapping("/config")
    public String getconfig(){
        return zookeeperTest;
    }

    
}
