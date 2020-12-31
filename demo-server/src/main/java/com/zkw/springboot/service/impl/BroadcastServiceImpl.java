package com.zkw.springboot.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.zkw.springboot.bean.User;
import com.zkw.springboot.protocol.Message;
import com.zkw.springboot.protocol.MessageType;
import com.zkw.springboot.service.BroadcastService;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhangkewei
 * @date 2020/12/22 12:21
 * @desc 广播服务
 */
@Service
public class BroadcastServiceImpl implements BroadcastService {
    @Autowired
    private Cache<String, Channel> userChannelCache;

    /**
     * 将消息发送给传入账号用户以外的在线用户
     * @param account
     * @param message
     */
    private void sendMessageToAll(String account, Message message){
        userChannelCache.asMap().forEach((k, v) -> {
            if(account.equals(k)){
                return;
            }
            v.writeAndFlush(message);
        });
    }

    /**
     * 创建切换地图消息发送给其他在线用户
     * @param user
     * @param oldMapId
     */
    @Override
    public void changeMapToAll(User user, Integer oldMapId) {
        Message messageToAll = new Message();
        messageToAll.setMessageType(MessageType.CHANGEMAP);
        messageToAll.map.put("oldMapId",oldMapId);
        messageToAll.map.put("user",user);
        messageToAll.setDescription(user.getUsername()+"去地图"+user.getMapId()+"了");
        sendMessageToAll(user.getAccount(),messageToAll);
    }

    /**
     * 创建刷新消息发送给其他在线用户
     * @param user
     * @param description
     */
    @Override
    public void updateAll(User user, String description) {
        Message messageToAll = new Message();
        messageToAll.setMessageType(MessageType.REFRESH);
        messageToAll.map.put("user",user);
        messageToAll.setDescription(description);
        sendMessageToAll(user.getAccount(),messageToAll);
    }
}
