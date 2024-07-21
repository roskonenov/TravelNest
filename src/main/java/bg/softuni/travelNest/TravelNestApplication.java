package bg.softuni.travelNest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TravelNestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TravelNestApplication.class, args);
    }

}
