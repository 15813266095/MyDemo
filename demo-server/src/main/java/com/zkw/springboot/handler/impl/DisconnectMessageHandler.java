package com.zkw.springboot.handler.impl;

import com.zkw.springboot.bean.User;
import com.zkw.springboot.dao.UserMapper;
import com.zkw.springboot.handler.IMessageHandler;
import com.zkw.springboot.protocol.Message;
import com.zkw.springboot.protocol.MessageType;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DisconnectMessageHandler implements IMessageHandler {

    @Autowired
    private UserMapper userMapper;

    @Override
    public MessageType getMessageType() {
        return MessageType.DISCONNECT;
    }

    @Override
    public void operate(ChannelHandlerContext ctx, Message request) {
        User user = request.getUser();
        userMapper.updateByPrimaryKeySelective(user);
        log.info("玩家数据保存成功");
        log.info("客户端断开连接");
        try {
            ctx.channel().close().sync();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
