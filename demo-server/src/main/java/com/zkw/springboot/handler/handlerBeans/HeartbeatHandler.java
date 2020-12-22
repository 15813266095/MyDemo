package com.zkw.springboot.handler.handlerBeans;

import com.zkw.springboot.bean.User;
import com.zkw.springboot.handler.DataManager;
import com.zkw.springboot.protocol.Message;
import com.zkw.springboot.protocol.MessageType;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhangkewei
 * @date 2020/12/22 15:17
 * @desc
 */
@Component
@Slf4j
public class HeartbeatHandler {

    @Autowired
    BroadcastHandler broadcastHandler;

    @Autowired
    DataManager dataManager;

    public void disconnect(ChannelHandlerContext ctx) throws InterruptedException {
        if(!dataManager.getConcurrentMap().containsValue(ctx.channel())){
            return;
        }

        String account=null;
        for (String s : dataManager.getConcurrentMap().keySet()) {
            if(dataManager.getConcurrentMap().get(s).equals(ctx.channel())){
                account=s;
            }
        }
        Message sendToAll = new Message();
        sendToAll.setMessageType(MessageType.REFRESH);
        User user = dataManager.getConnectedUser().get(account);
        sendToAll.setDescription(user.getUsername()+"异常下线");
        sendToAll.setUser(user);
        broadcastHandler.sendMessageToAll(account,sendToAll);

        safeDisconnect(ctx);
        Message message = new Message();
        message.setMessageType(MessageType.DISCONNECT);
        message.setDescription("服务器读空闲超过50次，关闭连接");
        log.info("服务器读空闲超过50次，关闭连接");
        ctx.channel().writeAndFlush(message).sync();
        ctx.channel().close().sync();
    }

    public void safeDisconnect(ChannelHandlerContext ctx){
        String account = null;
        if(dataManager.getConcurrentMap().size()==0){
            return;
        }
        for(String key: dataManager.getConcurrentMap().keySet()){
            if(dataManager.getConcurrentMap().get(key).equals(ctx.channel())){
                account=key;
            }
        }
        if(account!=null){
            User user = dataManager.getConnectedUser().get(account);
            dataManager.getMapInfoMap().get(user.getMapId()).removeUser(user);
            dataManager.getConnectedUser().remove(account);
        }
        dataManager.getConcurrentMap().remove(ctx.channel());
    }
}
