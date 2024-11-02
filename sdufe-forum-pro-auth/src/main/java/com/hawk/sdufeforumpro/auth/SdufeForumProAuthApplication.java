package com.hawk.sdufeforumpro.auth;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.hawk.sdufeforumpro.auth"})
@EnableDubbo
public class SdufeForumProAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(SdufeForumProAuthApplication.class, args);
    }

}
