package com.gp.webdriverapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement(proxyTargetClass = true)
public class WebDriverApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebDriverApiApplication.class, args);
    }

}
