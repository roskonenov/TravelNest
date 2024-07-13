package bg.softuni.travelNest.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "imgur.api")
@Getter
@Setter
@NoArgsConstructor
public class ImgurConfig {

    private String key;

    private String url;
}
