package app;

import config.SwaggerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import producer.QueueProducer;
import resource.TransactionValidationResource;
import service.TransactionValidationService;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@ComponentScan(basePackageClasses = {SwaggerConfig.class,
        TransactionValidationResource.class,
        TransactionValidationService.class,
        QueueProducer.class})
public class MoneyValidationApplication {
    public static void main(String[] args) {
        SpringApplication.run(MoneyValidationApplication.class, args);
    }
}