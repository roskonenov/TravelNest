package bg.softuni.travelNest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import java.net.http.HttpResponse;

@Configuration
public class RestConfig {

    @Bean("imageRestClient")
    public RestClient imageRestClient(ImgurConfig imgurConfig){
        return RestClient.builder()
                .defaultHeader("Authorization", "Client-ID " + imgurConfig.getKey())
                .build();
    }
}
