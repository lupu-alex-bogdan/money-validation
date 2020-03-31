package rabbit;


import com.fasterxml.jackson.databind.ObjectMapper;
import model.Transaction;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class QueueProducer {

    @Value("${fanout.exchange}")
    private String fanoutExchange;

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public QueueProducer(RabbitTemplate rabbitTemplate) {
        super();
        this.rabbitTemplate = rabbitTemplate;
    }

    @Autowired
    private ObjectMapper objectMapper;

    public void produce(Transaction transaction) throws Exception {
        System.out.println("Storing notification...");
        rabbitTemplate.setExchange(fanoutExchange);
        rabbitTemplate.convertAndSend(objectMapper.writeValueAsString(transaction));
        System.out.println("Notification stored in queue successfully");
    }
}
