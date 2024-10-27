package com.hawk.sdufeforumpro.file.config;

import com.hawk.sdufeforumpro.file.service.FileService;
import com.hawk.sdufeforumpro.file.service.MockFileServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * oss配置
 */
@Configuration
@EnableConfigurationProperties(OssProperties.class)
public class OssConfiguration {

    @Autowired
    private OssProperties properties;

    @Bean
    @ConditionalOnMissingBean
    @Profile({"dev", "test"})
    public FileService mockFileService() {
        return new MockFileServiceImpl();
    }
}
