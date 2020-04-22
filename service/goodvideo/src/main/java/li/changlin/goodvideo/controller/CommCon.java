package li.changlin.goodvideo.controller;

import li.changlin.common.response.Response;
import li.changlin.goodvideo.feignclient.UserFeignClient;
import li.changlin.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@Controller
@CrossOrigin
public class CommCon {
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    UserFeignClient ufc;

    @PostMapping("/comments")
    //@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")  // 指定角色权限才能操作方法
    public ResponseEntity<Response> createComment(Integer videoId, String commentContent,String username) {
        try {
            User user = ufc.getUser(username);
            String avatar = user.getAvatar();
            if (avatar.isEmpty()){
                avatar = "/js/cropper/vl_720P_1173.0k_61219871.png";
            }
            Timestamp timeObj = new Timestamp(System.currentTimeMillis());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String theTime = format.format(timeObj);
            stringRedisTemplate.opsForHash().put("user:comm_"+videoId,username+"&&"+avatar,commentContent+"&&"+theTime);
        }catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "处理成功", null));
    }
    @PostMapping("/commentsDel")
    //@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")  // 指定角色权限才能操作方法
    public ResponseEntity<Response> delete(Integer videoId,String username,String icon) {
        try {
            stringRedisTemplate.opsForHash().delete("user:comm_"+videoId,username+"&&"+icon);
        }catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "处理成功", null));
    }

}
