package com.zkw.springboot.handler.impl;

import com.zkw.springboot.handler.IMessageHandler;
import com.zkw.springboot.protocol.Message;
import com.zkw.springboot.protocol.MessageType;
import com.zkw.springboot.service.ClientService;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DisconnectHandler implements IMessageHandler {

    @Autowired
    ClientService clientService;

    @Override
    public MessageType getMessageType() {
        return MessageType.DISCONNECT;
    }

    @Override
    public void operate(ChannelHandlerContext ctx, Message response) {
        clientService.disconnect(response);
        try {
            ctx.channel().close().sync();
            log.info("断开连接");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
