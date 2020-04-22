package li.changlin.oauth2.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @RequestMapping("/oauth2user")
    public Principal user(Principal principal) {
        System.out.println(principal);
        return principal;
    }
    
}
