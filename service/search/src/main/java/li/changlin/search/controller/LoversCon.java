package li.changlin.search.controller;

import li.changlin.search.config.RedisLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

@Controller
public class LoversCon {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    RedisLock redisLock;

    @PostMapping("/loversEdit")
    @ResponseBody
    public Boolean loversEdit(String username,int num){
        String value = UUID.randomUUID().toString();
        boolean delLock;
        try {
            boolean getLock = redisLock.getLock("videoLock",value,"20",10000);
            if (getLock){
                System.out.println("获取锁成功，开始处理业务");
                String sql = "update user set lovers=lovers+? where username=?;";
                jdbcTemplate.update(sql,num,username);
            }else {
                System.out.println("获取锁失败");
            }
        }finally {
            delLock = redisLock.releaseLock("videoLock", value);
            if (delLock){
                System.out.println("释放锁成功");
            } else {
                System.out.println("释放锁失败");
            }
        }
        return delLock;
    }
}
