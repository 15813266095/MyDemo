package com.zkw.springboot.netty;

import com.zkw.springboot.protocal.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerHandler extends SimpleChannelInboundHandler<Message> {

    int readIdleTimes = 0;//读空闲的计数

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("客户端关闭，断开连接");
        ctx.close().sync();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message message) throws Exception {
        if(message.isActive()) {
            log.info("\n收到客户端信息：\n" + message.toString());
            ctx.writeAndFlush(message);
            readIdleTimes=0;//重置读空闲的计数
        }else {
            log.info("客户端断开连接");
            ctx.channel().close().sync();
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        IdleStateEvent event = (IdleStateEvent)evt;
        switch (event.state()){
            case READER_IDLE:
                readIdleTimes ++; // 读空闲的计数加1
                log.info(ctx.channel().remoteAddress() + "读空闲，累计次数"+readIdleTimes);
                break;
            case WRITER_IDLE:
                // 不处理
                break;
            case ALL_IDLE:
                // 不处理
                break;
        }
        if(readIdleTimes >= 3){
            log.info("服务器读空闲超过3次，关闭连接");
            Message message = new Message();
            message.setActive(false);
            ctx.channel().writeAndFlush(message);
            ctx.channel().close().sync();
        }
    }
}
