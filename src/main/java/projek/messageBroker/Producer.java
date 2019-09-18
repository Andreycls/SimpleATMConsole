package projek.messageBroker;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Producer {
    private RabbitTemplate rabbitTemplate;

    @Autowired
    public Producer(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate=rabbitTemplate;
    }

    @Value("${projek.rabbitmq.exchange}")
    private String exchange;

    @Value("${projek.rabbitmq.routingkey}")
    private String routingkey;

    @Value("${projek.rabbitmq.queue}")
    private String queueName;

    public void produceMessage(String msg){
        rabbitTemplate.convertAndSend(exchange,routingkey,msg);
        System.out.println("Send msg = "+msg);
    }


}
