package hello;

import hello.order.*;
import hello.order.gauge.StockConfigV1;
import hello.order.gauge.StockConfigV2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

//@Import(OrderConfigV0.class)
//@Import(OrderConfigV1.class)
//@Import(OrderConfigV2.class)
//@Import(OrderConfigV3.class)
//@Import({OrderServiceV4.class, StockConfigV1.class})
@Import({OrderServiceV4.class, StockConfigV2.class})

@SpringBootApplication
public class ActuatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActuatorApplication.class, args);
    }


    @Bean
    public InMemoryHttpExchangeRepository httpExchangeRepository() {
        return new InMemoryHttpExchangeRepository();
    }
}
