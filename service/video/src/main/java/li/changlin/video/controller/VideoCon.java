package li.changlin.video.controller;

import li.changlin.common.exception.ConstraintViolationExceptionHandler;
import li.changlin.common.response.Response;
import li.changlin.user.entity.User;
import li.changlin.video.entity.Video;
import li.changlin.video.service.VideoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.security.Principal;
import java.util.concurrent.TimeUnit;

@Controller
public class VideoCon {
    @Autowired
    private VideoService vs;
    private Logger logger = LoggerFactory.getLogger(VideoCon.class);

    @GetMapping("/video/add")
    public String addero(){
        return "ero/eroAdd";
    }
    @PostMapping("/video/submit")
    @ResponseBody
    //@PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<Response> submitfile(@RequestBody Video video,Principal principal){
        String abc = principal.getName();
        try {
            video.setUserName(abc);
            vs.addVideo(video);
        } catch (ConstraintViolationException e)  {
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        logger.info("$$新增视频$$"+video.getId()+"$$"+video.getTitle()+"$$"+abc+"$$"+video.getVideoLength()+"$$"+
                video.getVideoQua()+"$$"+video.getTags()+"$$"+video.getObject());
        String redirectUrl = "http://localhost:8765/";
        return ResponseEntity.ok().body(new Response(true, "アップロード成功", redirectUrl));
    }
    /*@GetMapping("/video/delete/{videoID}")
    public String deleteVideo(@PathVariable("videoID")Integer videoID){
       // int userID = vs.getVideoById(videoID).getUser().getId();
        vs.removeVideo(videoID);
        return "redirect:/u";// + userID;
    }*/
    @GetMapping(value = "/video/{id}")
    @ResponseBody
    public Video getVideoById(@PathVariable("id")Integer id){
        return vs.getVideoById(id);
    }
    @GetMapping("/getPrinciple")
    @ResponseBody
    public Authentication getPrinciple(OAuth2Authentication oAuth2Authentication, Principal principal, Authentication authentication) {
        System.out.println(oAuth2Authentication.getUserAuthentication().getAuthorities().toString());
        System.out.println(oAuth2Authentication.toString());
        System.out.println("principal.toString() " + principal.toString());
        System.out.println("principal.getName() " + principal.getName());
        System.out.println("authentication: " + authentication.getAuthorities().toString());

        return authentication;
    }
}
