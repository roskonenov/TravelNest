package bg.softuni.travelNest.config;

import bg.softuni.travelNest.service.JWTService;
import bg.softuni.travelNest.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Configuration
public class RestConfig {

    @Bean("imageRestClient")
    public RestClient imageRestClient(ImgurConfig imgurConfig){
        return RestClient.builder()
                .defaultHeader("Authorization", "Client-ID " + imgurConfig.getKey())
                .build();
    }

    @Bean("attractionsRestClient")
    public RestClient attractionsRestClient(AttractionApiConfig attractionApiConfig,
                                            ClientHttpRequestInterceptor requestInterceptor){
        return RestClient.builder()
                .baseUrl(attractionApiConfig.getBaseUrl())
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .requestInterceptor(requestInterceptor)
                .build();
    }

    @Bean
    public ClientHttpRequestInterceptor requestInterceptor(UserService userService, JWTService jwtService){
        return (r, b, e) -> {
            userService.getCurrentUser()
                    .ifPresent(tnud -> {
                        String token = jwtService.generateToken(tnud.getUuid().toString(),
                                Map.of(
                                        "username", tnud.getUsername(),
                                        "roles", tnud.getAuthorities()
                                                .stream()
                                                .map(GrantedAuthority::getAuthority)
                                                .toList()
                                ));
                        r.getHeaders().setBearerAuth(token);
                    });
            return e.execute(r, b);
        };
    }
}
