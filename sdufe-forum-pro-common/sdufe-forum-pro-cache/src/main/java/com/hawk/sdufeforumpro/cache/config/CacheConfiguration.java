package com.hawk.sdufeforumpro.cache.config;

import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.springframework.context.annotation.Configuration;

/**
 * 缓存配置
 */
@Configuration
@EnableMethodCache(basePackages = "com.hawk.sdufeforumpro")
public class CacheConfiguration {
}
