package com.zkw.springboot.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.zkw.springboot.bean.MapInfo;
import com.zkw.springboot.bean.User;
import com.zkw.springboot.cache.UseCache;
import com.zkw.springboot.protocol.Message;
import com.zkw.springboot.protocol.MessageType;
import com.zkw.springboot.service.BroadcastService;
import com.zkw.springboot.service.HeartbeatService;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentMap;

/**
 * @author zhangkewei
 * @date 2020/12/23 12:06
 * @desc 心跳检测服务
 */
@Slf4j
@Service
public class HeartbeatServiceImpl implements HeartbeatService {

    @Autowired
    private UseCache useCache;
    @Autowired
    private Cache<Integer, MapInfo> caffeineCache;
    @Autowired
    private BroadcastService broadcastService;

    @Override
    public void disconnect(ChannelHandlerContext ctx) {
        if(!useCache.getUserChannelMap().containsValue(ctx.channel())){
            return;
        }

        String account = null;
        for (String s : useCache.getUserChannelMap().keySet()) {
            if(useCache.getUserChannelMap().get(s).equals(ctx.channel())){
                account=s;
            }
        }
        Message sendToAll = new Message();
        sendToAll.setMessageType(MessageType.REFRESH);
        User user = useCache.getConnectedUserMap().get(account);
        sendToAll.setDescription(user.getUsername()+"异常下线");
        sendToAll.map.put("user",user);
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
        ConcurrentMap<Integer, MapInfo> mapInfoMap = caffeineCache.asMap();
        String account = null;
        if(useCache.getUserChannelMap().size()==0){
            return;
        }
        for(String key: useCache.getUserChannelMap().keySet()){
            if(useCache.getUserChannelMap().get(key).equals(ctx.channel())){
                account=key;
            }
        }
        if(account!=null){
            User user = useCache.getConnectedUserMap().get(account);
            mapInfoMap.get(user.getMapId()).exitUser(user);
            useCache.getConnectedUserMap().remove(account);
        }
        useCache.getUserChannelMap().remove(ctx.channel());
    }
}
