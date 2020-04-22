package li.changlin.search.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class RedisLock {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    public boolean getLock(String lockKey,String value ,String expire, long timeout){
        long startTime = System.currentTimeMillis();
        try{
            while (true){
                String script = "if redis.call('setNx',KEYS[1],ARGV[1]) then " +
                        "if redis.call('get',KEYS[1]) == ARGV[1] then " +
                        "return redis.call('expire',KEYS[1],ARGV[2]) else return 0 end end";
                RedisScript<Long> redisScript = new DefaultRedisScript<>(script,Long.class);
                Long execute = stringRedisTemplate.execute(redisScript,Collections.singletonList(lockKey), value, expire);
                if (1l == execute){
                    return true;
                } else {
                    long endTime = System.currentTimeMillis();
                    if ((endTime - startTime) > timeout){
                        return false;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public boolean releaseLock(String lockKey,String value){
        String script = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
        RedisScript<Long> redisScript = new DefaultRedisScript<>(script,Long.class);
        Long execute = stringRedisTemplate.execute(redisScript, Collections.singletonList(lockKey), value);
        if (1l == execute){
            return true;
        }
        return false;
    }
}
