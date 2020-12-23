package com.zkw.springboot.service.impl;

import com.zkw.springboot.bean.User;
import com.zkw.springboot.cache.MapInfoCache;
import com.zkw.springboot.cache.UserCache;
import com.zkw.springboot.protocol.Message;
import com.zkw.springboot.protocol.MessageType;
import com.zkw.springboot.service.BroadcastService;
import com.zkw.springboot.service.HeartbeatService;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhangkewei
 * @date 2020/12/23 12:06
 * @desc 心跳检测服务
 */
@Slf4j
@Service
public class HeartbeatServiceImpl implements HeartbeatService {

    @Autowired
    private UserCache userCache;
    @Autowired
    private MapInfoCache mapInfoCache;
    @Autowired
    private BroadcastService broadcastService;

    @Override
    public void disconnect(ChannelHandlerContext ctx) {
        if(!userCache.getUserChannelMap().containsValue(ctx.channel())){
            return;
        }

        String account=null;
        for (String s : userCache.getUserChannelMap().keySet()) {
            if(userCache.getUserChannelMap().get(s).equals(ctx.channel())){
                account=s;
            }
        }
        Message sendToAll = new Message();
        sendToAll.setMessageType(MessageType.REFRESH);
        User user = userCache.getConnectedUserMap().get(account);
        sendToAll.setDescription(user.getUsername()+"异常下线");
        sendToAll.setUser(user);
        broadcastService.sendMessageToAll(account,sendToAll);

        safeDisconnect(ctx);
        Message message = new Message();
        message.setMessageType(MessageType.DISCONNECT);
        message.setDescription("服务器读空闲超过50次，关闭连接");
        log.info("服务器读空闲超过50次，关闭连接");
        try {
            ctx.channel().writeAndFlush(message).sync();
            ctx.channel().close().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void safeDisconnect(ChannelHandlerContext ctx){
        String account = null;
        if(userCache.getUserChannelMap().size()==0){
            return;
        }
        for(String key: userCache.getUserChannelMap().keySet()){
            if(userCache.getUserChannelMap().get(key).equals(ctx.channel())){
                account=key;
            }
        }
        if(account!=null){
            User user = userCache.getConnectedUserMap().get(account);
            mapInfoCache.getMapInfoMap().get(user.getMapId()).exitUser(user);
            userCache.getConnectedUserMap().remove(account);
        }
        userCache.getUserChannelMap().remove(ctx.channel());
    }
}
