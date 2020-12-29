package com.zkw.springboot.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.zkw.springboot.bean.MapInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhangkewei
 * @date 2020/12/29 12:25
 * @desc 缓存配置类
 */
@Configuration
public class CacheConfig {
    @Bean
    public Cache<Integer, MapInfo> caffeineCache() {
        return Caffeine.newBuilder()
                // 初始的缓存空间大小
                .initialCapacity(100)
                // 缓存的最大条数
                .maximumSize(1000)
                .build();
    }
}
