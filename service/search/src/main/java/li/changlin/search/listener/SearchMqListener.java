package li.changlin.search.listener;

import com.rabbitmq.client.Channel;
import li.changlin.search.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class SearchMqListener {
    @Autowired
    private SearchService ss;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    public static String sessionId;
    private Logger logger = LoggerFactory.getLogger(SearchMqListener.class);

    @RabbitListener(queues = "asiya.video.save.queue")
    public void listenCreate(Map map,Channel channel,@Header(AmqpHeaders.DELIVERY_TAG) long tag/*,
                             @Header(AmqpHeaders.REDELIVERED) boolean reDelivered*/)throws IOException{
        try {
            //int a = 1/0;
            sessionId = (String) map.get("sessionId");
            Integer videoId = (Integer) map.get("videoId");
            if (stringRedisTemplate.opsForValue().get("mideng:"+videoId)==null){
                stringRedisTemplate.opsForValue().set("mideng:"+videoId,"1");
                ss.addEsVideoFromVideo(videoId);
            }
            channel.basicAck(tag,false);
        } catch (Exception e) {
            e.printStackTrace();
            channel.basicReject(tag,false);
            /*if (reDelivered){
                channel.basicReject(tag,false);
            }else {
                channel.basicNack(tag,false,true);
            }*/
        }
    }
    @RabbitListener(queues = "asiya.video.dl.queue")
    public void listenDL(Channel channel, Message message, @Header(AmqpHeaders.DELIVERY_TAG) long tag)throws IOException{
        Map map = (HashMap)SerializationUtils.deserialize(message.getBody());
        logger.info("上传失败需要处理的视频--->  ID: ["+map.get("videoId")+"]");
        channel.basicAck(tag,false);
    }

    @RabbitListener(queues = "asiya.video.delete.queue")
    public void listenDelete(Map map,Channel channel,@Header(AmqpHeaders.DELIVERY_TAG) long tag){
        try {
            Integer videoId = (Integer) map.get("videoId");
            ss.removeEsVideoById(videoId);
            channel.basicAck(tag,false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}