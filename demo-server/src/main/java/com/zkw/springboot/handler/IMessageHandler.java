package com.zkw.springboot.handler;

import com.zkw.springboot.protocol.Message;
import com.zkw.springboot.protocol.MessageType;
import io.netty.channel.ChannelHandlerContext;

public interface IMessageHandler {

    /**
     *获取消息类型
     * @return
     */
    MessageType getMessageType();

    /**
     *对消息进行操作
     */
    void operate(ChannelHandlerContext ctx, Message request);
}
