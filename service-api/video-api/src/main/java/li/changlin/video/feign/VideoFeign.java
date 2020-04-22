package li.changlin.video.feign;


import li.changlin.video.entity.Video;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

public interface VideoFeign {

    @RequestMapping(value = "/video/{id}",method = RequestMethod.GET)
    @ResponseBody
    Video getVideoFrom(@PathVariable("id")Integer id);

}
