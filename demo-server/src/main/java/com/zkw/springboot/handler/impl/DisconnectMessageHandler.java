package com.zkw.springboot.handler.impl;

import com.zkw.springboot.bean.User;
import com.zkw.springboot.dao.UserMapper;
import com.zkw.springboot.handler.DataManager;
import com.zkw.springboot.handler.IMessageHandler;
import com.zkw.springboot.netty.ServerHandler;
import com.zkw.springboot.protocol.Message;
import com.zkw.springboot.protocol.MessageType;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhangkewei
 * @date 2020/12/16 15:31
 * @desc 用于处理断开连接请求
 */
@Slf4j
@Component
public class DisconnectMessageHandler implements IMessageHandler {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DataManager dataManager;

    @Override
    public MessageType getMessageType() {
        return MessageType.DISCONNECT;
    }

    @Override
    public void operate(ChannelHandlerContext ctx, Message request) {
        User user = request.getUser();
        userMapper.updateByPrimaryKeySelective(user);
        dataManager.getMapInfoMap().get(user.getMapId()).removeUser(user);//将角色从地图里删除
        dataManager.getConnectedUser().remove(user.getAccount());

        Message messageToAll = new Message();
        messageToAll.setMessageType(MessageType.REFRESH);
        messageToAll.setUser(user);
        messageToAll.setDescription(user.getUsername()+"下线了");
        sendMessageToAll(user.getAccount(), messageToAll);

        ServerHandler.concurrentMap.remove(user.getAccount());
        log.info("玩家数据保存成功");
        log.info("客户端断开连接");
        try {
            ctx.channel().close().sync();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void sendMessageToAll(String account,Message message){
        ServerHandler.concurrentMap.forEach((k, v) -> {
            if(!account.equals(k)){
                v.writeAndFlush(message);
            }
        });
    }
}
