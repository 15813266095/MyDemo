package com.zkw.springboot.handler.impl;

import com.zkw.springboot.handler.IMessageHandler;
import com.zkw.springboot.protocol.Message;
import com.zkw.springboot.protocol.MessageType;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Service;

@Service
public class RegisterMessageHandler implements IMessageHandler {
    @Override
    public MessageType getMessageType() {
        return MessageType.REGISTER;
    }

    @Override
    public void operate(ChannelHandlerContext ctx, Message request) {

    }
}
