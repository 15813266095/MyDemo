package com.zkw.springboot.handler.impl;

import com.zkw.springboot.bean.User;
import com.zkw.springboot.dao.UserMapper;
import com.zkw.springboot.handler.IMessageHandler;
import com.zkw.springboot.protocol.Message;
import com.zkw.springboot.protocol.MessageType;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 获取角色信息时执行的处理器，用于处理MessageType.GET请求
 */

@Service
public class GetMessageHandler implements IMessageHandler {

    @Autowired
    UserMapper userMapper;

    @Override
    public MessageType getMessageType() {
        return MessageType.GET;
    }

    @Override
    public void operate(ChannelHandlerContext ctx, Message request) {
        User user = userMapper.selectByPrimaryKey(request.getUser().getAccount());
        Message response = new Message();
        response.setMessageType(MessageType.SUCCESS);
        response.setUser(user);
        response.setDescription("\n角色名为：" +user.getUsername()+
                "\n当前角色位置为："+user.getArea()+
                "\n当前角色地图在："+user.getScenes());
        ctx.writeAndFlush(response);
    }
}
