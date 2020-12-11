package com.zkw.springboot.handler.impl;

import com.zkw.springboot.handler.IMessageHandler;
import com.zkw.springboot.protocol.Message;
import com.zkw.springboot.protocol.MessageType;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Service;

/**
 * 请求错误时执行的处理器
 */

@Service
public class ErrorMessageHandler implements IMessageHandler {
    @Override
    public MessageType getMessageType() {
        return MessageType.ERROR;
    }

    @Override
    public void operate(ChannelHandlerContext ctx, Message request) {
        Message response = new Message();
        response.setMessageType(MessageType.ERROR);
        response.setDescription("请求方式错误");
        ctx.writeAndFlush(response);
    }
}
