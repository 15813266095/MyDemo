package com.zkw.springboot.handler.impl;

import com.zkw.springboot.handler.IMessageHandler;
import com.zkw.springboot.protocol.Message;
import com.zkw.springboot.protocol.MessageType;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Service;

@Service
public class LoginMessageHandler implements IMessageHandler {
    @Override
    public MessageType getMessageType() {
        return MessageType.LOGIN;
    }

    @Override
    public void operate(ChannelHandlerContext ctx, Message request) {

    }
}
