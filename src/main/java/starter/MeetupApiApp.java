package starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@ComponentScan(basePackages = {"starter.controller", "starter.service", "starter.repository", "starter.config"})
@EnableSwagger2
public class MeetupApiApp {
    public static void main(String[] args) {
        SpringApplication.run(MeetupApiApp.class, args);
    }
}

