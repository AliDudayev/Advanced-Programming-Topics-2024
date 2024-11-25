package fact.it.recordservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(){
//        System.out.println("Creating WebClient bean");
        return WebClient.builder().build();
    }
}