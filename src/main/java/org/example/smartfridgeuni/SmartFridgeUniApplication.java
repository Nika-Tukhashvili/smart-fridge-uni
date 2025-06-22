package org.example.smartfridgeuni;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SmartFridgeUniApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartFridgeUniApplication.class, args);
    }

}
