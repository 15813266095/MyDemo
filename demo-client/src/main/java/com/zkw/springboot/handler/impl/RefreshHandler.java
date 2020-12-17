package com.zkw.springboot.handler.impl;

import com.zkw.springboot.handler.IMessageHandler;
import com.zkw.springboot.protocol.Message;
import com.zkw.springboot.protocol.MessageType;
import com.zkw.springboot.service.ClientService;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhangkewei
 * @date 2020/12/17 12:19
 * @desc
 */
@Slf4j
@Component
public class RefreshHandler implements IMessageHandler {

    @Autowired
    ClientService clientService;

    @Override
    public MessageType getMessageType() {
        return MessageType.REFRESH;
    }

    @Override
    public void operate(ChannelHandlerContext ctx, Message response) {
        log.info(response.getDescription());
        try {
            clientService.setInfo(response);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
