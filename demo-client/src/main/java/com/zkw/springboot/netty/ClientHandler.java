package com.zkw.springboot.netty;


import com.zkw.springboot.distribution.MessageHandlerManager;
import com.zkw.springboot.protocol.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhangkewei
 * @date 2020/12/22 16:40
 * @desc 自定义的handler
 */
@Slf4j
public class ClientHandler extends SimpleChannelInboundHandler<Message> {

    private MessageHandlerManager messageHandlerManager;

    public void setHandlerManager(MessageHandlerManager messageHandlerManager) {
        this.messageHandlerManager = messageHandlerManager;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        log.error("服务器异常，断开连接");
        ctx.channel().close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message response) throws Exception {
        log.info("接收到服务器响应，类型为："+response.getMessageType());
        if(response==null){
            log.info("服务器断开连接");
            channelHandlerContext.channel().close().sync();
            return;
        }
        messageHandlerManager.invokeMethod(response.getMessageType(),channelHandlerContext,response);
    }

}
