package li.changlin.oauth2.controller;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class ZookeeperConfigController {

    /*@Value("${name}")
    private String serverPort;

    @RequestMapping("/config")
    public String getConfig(){
        return serverPort;
    }*/
}

