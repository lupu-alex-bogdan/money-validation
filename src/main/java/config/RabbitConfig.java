package config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Value("${fanout.exchange}")
    private String fanoutExchange;
    @Value("${queue.name}")
    private String queueName;
    @Value("${fanout.exchange.dlx}")
    private String fanoutExchangeDlx;
    @Value("${queue.name.dlx}")
    private String queueNameDlx;
    @Value("${routing.key.dlx}")
    private String routingKeyDlx;
    @Value("${routing.key}")
    private String routingKey;

    @Bean(name = "queueName")
    Queue queue() {
        return QueueBuilder.durable(queueName)
                .deadLetterExchange(fanoutExchangeDlx)
                .deadLetterRoutingKey(routingKeyDlx)
                .ttl(1000)
                .build();
    }

    @Bean(name = "queueNameDlx")
    Queue queueDlx() {
        return QueueBuilder.durable(queueNameDlx)
//                .deadLetterRoutingKey(queueName)
//                .ttl(1000)
                .build();
    }

    @Bean(name = "fanoutExchange")
    FanoutExchange exchange() {
        return new FanoutExchange(fanoutExchange);
    }

    @Bean(name = "fanoutExchangeDlx")
    FanoutExchange exchangeDlx() {
        return new FanoutExchange(fanoutExchangeDlx);
    }

    @Bean(name = "binding")
    Binding binding(@Qualifier("queueName") Queue queue, @Qualifier("fanoutExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey).noargs();
    }

    @Bean(name = "bindingDlx")
    Binding deadLetterBind(@Qualifier("queueNameDlx") Queue queue, @Qualifier("fanoutExchangeDlx") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKeyDlx).noargs();
    }
}