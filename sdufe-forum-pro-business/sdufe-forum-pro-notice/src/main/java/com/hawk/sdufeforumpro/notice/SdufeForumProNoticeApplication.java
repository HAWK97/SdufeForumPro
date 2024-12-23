package com.hawk.sdufeforumpro.notice;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class SdufeForumProNoticeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SdufeForumProNoticeApplication.class, args);
    }

}
