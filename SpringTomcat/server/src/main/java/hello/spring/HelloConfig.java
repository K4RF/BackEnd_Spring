package hello.spring;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

@Configurable
public class HelloConfig {
    @Bean
    public HelloController helloController(){
        return new HelloController();
    }
}
