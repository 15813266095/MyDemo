package com.zkw.springboot.handler.impl;

import com.zkw.springboot.handler.IMessageHandler;
import com.zkw.springboot.protocol.Message;
import com.zkw.springboot.protocol.MessageType;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DisconnectMessageHandler implements IMessageHandler {
    @Override
    public MessageType getMessageType() {
        return MessageType.DISCONNECT;
    }

    @Override
    public void operate(ChannelHandlerContext ctx, Message request) {
        log.info("客户端断开连接");
        try {
            ctx.channel().close().sync();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
