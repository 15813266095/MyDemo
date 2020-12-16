package com.zkw.springboot.handler.impl;

import com.zkw.springboot.handler.IMessageHandler;
import com.zkw.springboot.protocol.Message;
import com.zkw.springboot.protocol.MessageType;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

/**
 * @author zhangkewei
 * @date 2020/12/16 15:31
 * @desc 用于处理错误请求
 */
@Component
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
