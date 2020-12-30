package com.zkw.springboot.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.zkw.springboot.bean.Equipment;
import com.zkw.springboot.bean.MapInfo;
import com.zkw.springboot.bean.User;
import io.netty.channel.Channel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author zhangkewei
 * @date 2020/12/29 12:25
 * @desc 缓存配置类
 */
@Configuration
public class CacheConfig {

    /**
     * 缓存地图信息
     * @return
     */
    @Bean
    public Cache<Integer, MapInfo> mapInfoCache() {
        return Caffeine.newBuilder()
                // 初始的缓存空间大小
                .initialCapacity(100)
                // 缓存的最大条数
                .maximumSize(1000)
                .build();
    }

    /**
     * 缓存用户的channel
     * @return
     */
    @Bean
    public Cache<String, User> connectedUserCache() {
        return Caffeine.newBuilder()
                //1小时没有操作的用户缓存自动过期
                .expireAfterWrite(1, TimeUnit.HOURS)
                // 初始的缓存空间大小
                .initialCapacity(100)
                // 缓存的最大条数
                .maximumSize(1000)
                .build();
    }

    /**
     * 缓存在线玩家信息
     * @return
     */
    @Bean
    public Cache<String, Channel> userChannelCache() {
        return Caffeine.newBuilder()
                //1小时没有操作的用户缓存自动过期
                .expireAfterWrite(1, TimeUnit.HOURS)
                // 初始的缓存空间大小
                .initialCapacity(100)
                // 缓存的最大条数
                .maximumSize(1000)
                .build();
    }

    /**
     * 缓存装备信息
     * @return
     */
    @Bean
    public Cache<Integer, Equipment> equipmentCache() {
        return Caffeine.newBuilder()
                // 初始的缓存空间大小
                .initialCapacity(100)
                // 缓存的最大条数
                .maximumSize(1000)
                .build();
    }
}
