package config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Value("${exchange.name}")
    private String exchangeName;
    @Value("${queue.name}")
    private String queueName;
    @Value("${exchange.name.dlx}")
    private String exchangeNameDlx;
    @Value("${queue.name.dlx}")
    private String queueNameDlx;
    @Value("${routing.key.dlx}")
    private String routingKeyDlx;
    @Value("${routing.key}")
    private String routingKey;

    @Bean(name = "queueName")
    Queue queue() {
        return QueueBuilder.durable(queueName)
                .deadLetterExchange(exchangeNameDlx)
                .deadLetterRoutingKey(routingKeyDlx)
                .ttl(15000)
                .build();
    }

    @Bean(name = "queueNameDlx")
    Queue queueDlx() {
        return QueueBuilder.durable(queueNameDlx)
                .deadLetterExchange(exchangeName)
                .deadLetterRoutingKey(queueName)
                .ttl(15000)
                .build();
    }

    @Bean(name = "exchange")
    Exchange exchange() {
        return new DirectExchange(exchangeName);
    }

    @Bean(name = "exchangeDlx")
    Exchange exchangeDlx() {
        return new DirectExchange(exchangeNameDlx);
    }

    @Bean(name = "binding")
    Binding binding(@Qualifier("queueName") Queue queue, @Qualifier("exchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey).noargs();
    }

    @Bean(name = "bindingDlx")
    Binding deadLetterBind(@Qualifier("queueNameDlx") Queue queue, @Qualifier("exchangeDlx") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKeyDlx).noargs();
    }
}