package li.changlin.search.listener;

import com.rabbitmq.client.Channel;
import li.changlin.search.service.SearchService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class SearchMqListener {
    @Autowired
    private SearchService ss;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public static String sessionId;

    @RabbitListener(queues = "asiya.video.save.queue")
    public void listenCreate(Map map,Channel channel,@Header(AmqpHeaders.DELIVERY_TAG) long tag,
                             @Header(AmqpHeaders.REDELIVERED) boolean reDelivered)throws IOException{
        try {
            sessionId = (String) map.get("sessionId");
            Integer videoId = (Integer) map.get("videoId");
            if (stringRedisTemplate.opsForValue().get("mideng:"+videoId)==null){
                ss.addEsVideoFromVideo(videoId);
                stringRedisTemplate.opsForValue().set("mideng:"+videoId,"1");
            }
            channel.basicAck(tag,false);
        } catch (Exception e) {
            e.printStackTrace();
            if (reDelivered){
                channel.basicReject(tag,false);
            }else {
                channel.basicNack(tag,false,true);
            }
        }
    }
    @RabbitListener(queues = "asiya.video.dl.queue")
    public void listenDL(Channel channel, Message message, @Header(AmqpHeaders.DELIVERY_TAG) long tag)throws IOException{
        System.out.println("需要处理的异常消息："+ new String(message.getBody()));
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