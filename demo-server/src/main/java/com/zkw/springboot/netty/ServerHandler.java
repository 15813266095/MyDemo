package com.zkw.springboot.netty;

import com.zkw.springboot.handler.HandlerManager;
import com.zkw.springboot.protocol.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerHandler extends SimpleChannelInboundHandler<Message> {

    private HandlerManager handlerManager;

    private int readIdleTimes = 0;//读空闲的计数

    public ServerHandler(HandlerManager handlerManager) {
        this.handlerManager = handlerManager;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        //cause.printStackTrace();
        log.error("客户端关闭，断开连接");
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message request) throws Exception {
        log.info("收到客户端消息，消息类型为"+request.getMessageType());
        handlerManager.getMessageTypeIMessageHandlerMap().get(request.getMessageType()).operate(ctx, request);
        readIdleTimes=0;//重置读空闲的计数
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        IdleStateEvent event = (IdleStateEvent)evt;
        if(event.state() == IdleState.READER_IDLE){
            readIdleTimes ++; // 读空闲的计数加1
            log.info(ctx.channel().remoteAddress() + "读空闲，累计次数"+readIdleTimes);
        }
//        if(readIdleTimes >= 3){
//            log.info("服务器读空闲超过3次，关闭连接");
//            Message message = new Message();
//            message.setMessageType(MessageType.DISCONNECT);
//            message.setDescription("服务器读空闲超过3次，关闭连接");
//            ctx.channel().writeAndFlush(message);
//            ctx.channel().close().sync();
//        }
    }
}
