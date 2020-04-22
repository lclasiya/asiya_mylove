package li.changlin.video.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitConfig {
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Bean
    public Queue DLQueue() {
        return new Queue("asiya.video.dl.queue",true);
    }
    @Bean
    DirectExchange DLExchange() {
        return new DirectExchange("asiya.video.dlexchange");
    }
    @Bean
    Binding DLBinding() {
        return BindingBuilder.bind(DLQueue()).to(DLExchange()).with("video.dl");
    }
    @Bean
    public Queue QueueSave() {
        Map<String, Object> map = new HashMap<>();
        map.put("x-dead-letter-exchange", "asiya.video.dlexchange");//设置死信交换机
        map.put("x-dead-letter-routing-key", "video.dl");
        return new Queue("asiya.video.save.queue",true,false,false,map);
    }
    @Bean
    public Queue QueueDelete() {
        return new Queue("asiya.video.delete.queue",true);
    }
    @Bean
    DirectExchange DirectExchange() {
        return new DirectExchange("asiya.video.exchange");
    }
    @Bean
    Binding BindingSave() {
        return BindingBuilder.bind(QueueSave()).to(DirectExchange()).with("video.save");
    }
    @Bean
    Binding BindingDelete() {
        return BindingBuilder.bind(QueueDelete()).to(DirectExchange()).with("video.delete");
    }
    @Bean
    public RabbitTemplate createRabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        //设置开启Mandatory,才能触发回调函数,无论消息推送结果怎么样都强制调用回调函数
        rabbitTemplate.setMandatory(true);

        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                System.out.println("ConfirmCallback:     "+"相关数据："+correlationData);
                System.out.println("ConfirmCallback:     "+"确认情况："+ack);
                System.out.println("ConfirmCallback:     "+"原因："+cause);
                if (!ack){
                    stringRedisTemplate.opsForSet().add("video_failed",correlationData.getId());
                }
            }
        });

        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                System.out.println("ReturnCallback:     "+"消息："+message);
                System.out.println("ReturnCallback:     "+"回应码："+replyCode);
                System.out.println("ReturnCallback:     "+"回应信息："+replyText);
                System.out.println("ReturnCallback:     "+"交换机："+exchange);
                System.out.println("ReturnCallback:     "+"路由键："+routingKey);
            }
        });
        return rabbitTemplate;
    }

}
