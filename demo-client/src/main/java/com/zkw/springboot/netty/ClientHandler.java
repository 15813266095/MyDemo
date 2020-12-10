package com.zkw.springboot.netty;


import com.zkw.springboot.protocol.Message;
import com.zkw.springboot.protocol.MessageType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientHandler extends SimpleChannelInboundHandler<Message> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("服务器异常，断开连接");
        ctx.close().sync();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message response) throws Exception {
        if(response==null||!response.isActive()){
            log.info("长时间未响应,服务器断开连接");
            channelHandlerContext.channel().close().sync();
        }else{
            if(response.getMessageType()== MessageType.ERROR){
                log.error(response.getDescription());
            }else {
                log.info(response.getDescription());
            }
        }
    }
}
