package com.hawk.sdufeforumpro.user;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.hawk.sdufeforumpro.user")
@EnableDubbo
public class SdufeForumProUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(SdufeForumProUserApplication.class, args);
    }

}
