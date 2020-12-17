package com.zkw.springboot.netty;

import com.zkw.springboot.bean.User;
import com.zkw.springboot.handler.DataManager;
import com.zkw.springboot.handler.HandlerManager;
import com.zkw.springboot.protocol.Message;
import com.zkw.springboot.protocol.MessageType;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangkewei
 * @date 2020/12/16 15:31
 * @desc 接收客户端请求并分发
 */
@Slf4j
public class ServerHandler extends SimpleChannelInboundHandler<Message> {

    private HandlerManager handlerManager;
    private int readIdleTimes = 0;
    public static ConcurrentHashMap<String,Channel> concurrentMap = new ConcurrentHashMap<>();
    public ServerHandler(HandlerManager handlerManager) {
        this.handlerManager = handlerManager;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws InterruptedException {
        //cause.printStackTrace();
        safeDisconnect(ctx);
        log.error("客户端关闭，断开连接");
        ctx.channel().close().sync();
    }

    /**
     * 接收到请求，将请求分发到对应的handler执行
     * @param ctx
     * @param request
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message request) throws Exception {
        log.info("收到客户端消息，消息类型为"+request.getMessageType());
        handlerManager.getMessageTypeIMessageHandlerMap().get(request.getMessageType()).operate(ctx, request);
        readIdleTimes=0;//重置读空闲的计数
    }

    /**
     * 心跳检测，每10秒记录一次读空闲次数，当读空闲超过三次，关闭连接
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        IdleStateEvent event = (IdleStateEvent)evt;
        if(event.state() == IdleState.READER_IDLE){
            readIdleTimes ++; // 读空闲的计数加1
            log.info(ctx.channel().remoteAddress() + "读空闲，累计次数"+readIdleTimes);
        }
        if(readIdleTimes >= 50){
            log.info("服务器读空闲超过3次，关闭连接");
            Message message = new Message();
            message.setMessageType(MessageType.DISCONNECT);
            message.setDescription("服务器读空闲超过10次，关闭连接");
            ctx.channel().writeAndFlush(message);
            safeDisconnect(ctx);
            ctx.channel().close().sync();
        }
    }

    public void safeDisconnect(ChannelHandlerContext ctx){
        String account=null;
        for(String key: concurrentMap.keySet()){
            if(concurrentMap.get(key).equals(ctx.channel())){
                account=key;
            }
        }
        if(account!=null){
            User user = DataManager.getConnectedUser().get(account);
            System.out.println(account);
            DataManager.getMapInfoMap().get(user.getMapId()).removeUser(user);
            DataManager.getConnectedUser().remove(account);
        }
        concurrentMap.remove(ctx.channel());
    }


}
