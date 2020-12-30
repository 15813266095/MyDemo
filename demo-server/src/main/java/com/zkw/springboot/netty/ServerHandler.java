package com.zkw.springboot.netty;

import com.zkw.springboot.distribution.MessageHandlerManager;
import com.zkw.springboot.protocol.Message;
import com.zkw.springboot.service.HeartbeatService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;


/**
 * @author zhangkewei
 * @date 2020/12/16 15:31
 * @desc 接收客户端请求并分发
 */
@Slf4j
public class ServerHandler extends SimpleChannelInboundHandler<Message> {

    /**
     * 心跳服务
     */
    private HeartbeatService heartbeatService;

    /**
     * 分发管理器
     */
    private MessageHandlerManager messageHandlerManager;

    /**
     * 读空闲计数
     */
    private int readIdleTimes = 0;

    /**
     * 读空闲的最大次数
     */
    private static final int Max_readIdleTimes = 50;

    public ServerHandler(HeartbeatService heartbeatService, MessageHandlerManager messageHandlerManager) {
        this.heartbeatService = heartbeatService;
        this.messageHandlerManager = messageHandlerManager;
    }

    /**
     * 捕获异常，打印错误
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        //cause.printStackTrace();
        heartbeatService.safeDisconnect(ctx);
        log.error("客户端关闭，断开连接");
        ctx.channel().close();
    }

    /**
     * netty接收请求，将请求分发到对应的facade执行
     * @param ctx
     * @param request
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message request) {
        log.info("收到客户端消息，消息类型为"+request.getMessageType());
        messageHandlerManager.invokeMethod(request.getMessageType(),ctx,request);
        readIdleTimes=0;//重置读空闲的计数
    }

    /**
     * 心跳检测，每10秒记录一次读空闲次数，当读空闲超过50次，发起关闭连接请求
     * @param ctx
     * @param evt
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        IdleStateEvent event = (IdleStateEvent)evt;
        if(event.state() == IdleState.READER_IDLE){
            readIdleTimes++; // 读空闲的计数加1
            log.info(ctx.channel().remoteAddress() + "读空闲，累计次数" + readIdleTimes);
        }
        if(readIdleTimes >= Max_readIdleTimes){
            heartbeatService.disconnect(ctx);
            readIdleTimes=0;
        }
    }

}
