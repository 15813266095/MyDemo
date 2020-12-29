package com.zkw.springboot.service.impl;

import com.zkw.springboot.protocol.Message;
import com.zkw.springboot.cache.UseCache;
import com.zkw.springboot.service.BroadcastService;
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
    UseCache useCache;

    /**
     * 将消息发送给传入账号用户以外的在线用户
     * @param account
     * @param message
     */
    public void sendMessageToAll(String account, Message message){
        useCache.getUserChannelMap().forEach((k, v) -> {
            if(account.equals(k)){
                return;
            }
            v.writeAndFlush(message);
        });
    }
}
