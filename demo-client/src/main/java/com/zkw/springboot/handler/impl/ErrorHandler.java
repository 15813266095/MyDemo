package com.zkw.springboot.handler.impl;

import com.zkw.springboot.handler.IMessageHandler;
import com.zkw.springboot.protocol.Message;
import com.zkw.springboot.protocol.MessageType;
import com.zkw.springboot.service.ClientService;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ErrorHandler implements IMessageHandler {

    @Autowired
    private ClientService clientService;

    @Override
    public MessageType getMessageType() {
        return MessageType.ERROR;
    }

    @Override
    public void operate(ChannelHandlerContext ctx, Message response) {
        log.error(response.getDescription());
        try {
            clientService.put(response);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
