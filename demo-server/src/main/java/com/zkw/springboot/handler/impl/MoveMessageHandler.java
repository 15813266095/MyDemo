package com.zkw.springboot.handler.impl;

import com.zkw.springboot.bean.User;
import com.zkw.springboot.handler.IMessageHandler;
import com.zkw.springboot.protocol.Message;
import com.zkw.springboot.protocol.MessageType;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Service;

/**
 * 角色移动时执行的处理器，用来处理MessageType.MOVE请求
 */

@Service
public class MoveMessageHandler implements IMessageHandler {

    @Override
    public MessageType getMessageType() {
        return MessageType.MOVE;
    }

    @Override
    public void operate(ChannelHandlerContext ctx, Message request) {
        User user = request.getUser();
        Message response = new Message();
        response.setMessageType(MessageType.SUCCESS);
        user.move(request.getDirection());
        response.setUser(user);
        response.setDescription("\n角色移动了，方向为"+request.getDirection()+ "，当前角色位置为："+user.getArea());
        ctx.writeAndFlush(response);
    }
}
