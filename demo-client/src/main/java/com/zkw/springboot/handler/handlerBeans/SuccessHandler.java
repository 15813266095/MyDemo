package com.zkw.springboot.handler.handlerBeans;

import com.zkw.springboot.annotation.handler;
import com.zkw.springboot.protocol.Message;
import com.zkw.springboot.protocol.MessageType;
import com.zkw.springboot.service.ClientService;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SuccessHandler{

    @Autowired
    ClientService clientService;

    @handler(messageType = MessageType.SUCCESS)
    public void successHandler(ChannelHandlerContext ctx, Message response) {
        log.info(response.getDescription());
        try {
            clientService.put(response);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
