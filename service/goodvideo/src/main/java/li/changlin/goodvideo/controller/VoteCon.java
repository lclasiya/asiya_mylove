package li.changlin.goodvideo.controller;

import li.changlin.common.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@CrossOrigin
public class VoteCon {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @PostMapping("/votes")
    //@PreAuthorize("hasAnyRole('ADMIN','USER')")  // 指定角色权限才能操作方法
    public ResponseEntity<Response> createVote(Integer videoId, String username) {
        try {
            stringRedisTemplate.opsForSet().add("videoid:"+videoId,username);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "点赞成功", null));
    }
    @PostMapping("/votesDel")
    //@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")  // 指定角色权限才能操作方法
    public ResponseEntity<Response> cancelVote(@RequestParam(value = "videoId") int videoId,@RequestParam(value = "username")String username) {
        try {
            stringRedisTemplate.opsForSet().remove("videoid:"+videoId,username);
        }catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "取消点赞成功", null));
    }
}
