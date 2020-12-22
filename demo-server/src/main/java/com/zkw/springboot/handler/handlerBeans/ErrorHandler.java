package com.zkw.springboot.handler.handlerBeans;

import com.zkw.springboot.annotation.handler;
import com.zkw.springboot.protocol.Message;
import com.zkw.springboot.protocol.MessageType;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

/**
 * @author zhangkewei
 * @date 2020/12/22 11:55
 * @desc 用于处理错误请求
 */
@Component
public class ErrorHandler {

    @handler(messageType = MessageType.ERROR)
    public void errorHandler(ChannelHandlerContext ctx, Message request) {
        Message response = new Message();
        response.setMessageType(MessageType.ERROR);
        response.setDescription("请求方式错误");
        ctx.writeAndFlush(response);
    }
}
