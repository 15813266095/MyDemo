package com.zkw.springboot.handler.impl;

import com.zkw.springboot.bean.User;
import com.zkw.springboot.handler.IMessageHandler;
import com.zkw.springboot.protocol.Message;
import com.zkw.springboot.protocol.MessageType;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Service;

/**
 * 场景切换时用的处理器，用于处理MessageType.CHANGE_SCENES请求
 */

@Service
public class ChangeScenesMessageHandler implements IMessageHandler {

    @Override
    public MessageType getMessageType() {
        return MessageType.CHANGE_SCENES;
    }

    @Override
    public void operate(ChannelHandlerContext ctx, Message request) {
        User user = request.getUser();
        Message response = new Message();
        response.setMessageType(MessageType.SUCCESS);
        user.setScenes("场景2");
        response.setDescription("当前角色场景为："+user.getScenes());
        ctx.writeAndFlush(response);
    }
}