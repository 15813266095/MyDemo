package com.zkw.springboot.resource;

import com.zkw.springboot.bean.User;
import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangkewei
 * @date 2020/12/16 15:31
 * @desc 暂时缓存角色数据，包括在线角色数据和角色对应的channel
 */
@Component
public class UserManager {

    /**
     * 在线玩家信息
     */
    private ConcurrentHashMap<String, User> connectedUserMap;

    /**
     * 目前已经连接的channel信息，k=角色账号，v=channnel
     */
    private ConcurrentHashMap<String, Channel> userChannelMap;


    public ConcurrentHashMap<String, User> getConnectedUserMap() {
        return connectedUserMap;
    }
    public ConcurrentHashMap<String, Channel> getUserChannelMap() {
        return userChannelMap;
    }

    /**
     * 在构造时创建好信息对象
     */
    @PostConstruct
    public void init() {
        this.connectedUserMap = new ConcurrentHashMap<>();
        this.userChannelMap = new ConcurrentHashMap<>();
    }
}
