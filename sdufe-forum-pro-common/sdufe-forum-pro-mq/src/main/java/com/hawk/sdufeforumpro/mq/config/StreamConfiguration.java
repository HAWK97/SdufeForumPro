package com.hawk.sdufeforumpro.mq.config;

import com.hawk.sdufeforumpro.mq.producer.StreamProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * stream配置
 */
@Configuration
public class StreamConfiguration {

    @Bean
    public StreamProducer streamProducer() {
        StreamProducer streamProducer = new StreamProducer();
        return streamProducer;
    }
}
