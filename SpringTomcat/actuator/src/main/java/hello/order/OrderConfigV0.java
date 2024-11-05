package hello.order;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderConfigV0 {
    @Bean
    OrderService orderService() {
        return new OrderServiceV0();
    }
    
}
