package li.changlin.oauth2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FirstCon {
    @GetMapping("/")
    public String first() {
        return "checkAge";
    }

    @GetMapping("/under18")
    public String under18() {
        return "under18";
    }
}