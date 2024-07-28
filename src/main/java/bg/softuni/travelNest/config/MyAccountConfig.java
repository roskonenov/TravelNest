package bg.softuni.travelNest.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
@Getter
@Setter
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "my.account.redirect")
public class MyAccountConfig {

    private String baseUrl;
}