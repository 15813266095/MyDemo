package com.zkw.springboot.handler.impl;

import com.zkw.springboot.dao.UserMapper;
import com.zkw.springboot.handler.IMessageHandler;
import com.zkw.springboot.protocol.Message;
import com.zkw.springboot.protocol.MessageType;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterMessageHandler implements IMessageHandler {
    @Autowired
    UserMapper userMapper;

    @Override
    public MessageType getMessageType() {
        return MessageType.REGISTER;
    }

    @Override
    public void operate(ChannelHandlerContext ctx, Message request) {
        int insert = userMapper.insert(request.getUser());
        Message response = new Message();
        response.setMessageType(MessageType.SUCCESS);
        response.setUser(request.getUser());
        response.setDescription("注册成功！自动登录");
        ctx.writeAndFlush(response);
    }
}
