package com.zkw.springboot.netty;


import com.zkw.springboot.protocol.Message;
import com.zkw.springboot.service.ClientService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class ClientHandler extends SimpleChannelInboundHandler<Message> {
    @Autowired
    private ClientService clientService;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("服务器异常，断开连接");
        ctx.close().sync();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message response) throws Exception {
        log.info("接收到服务器响应，类型为："+response.getMessageType());
        if(response==null){
            log.info("服务器断开连接");
            channelHandlerContext.channel().close().sync();
            return;
        }
        switch (response.getMessageType()){
            case SUCCESS:
                log.info(response.getDescription());
                clientService.setResponse(response);
                break;
            case ERROR:
                log.error(response.getDescription());
                clientService.setResponse(response);
                break;
            case DISCONNECT:
                log.info("服务器断开连接");
                clientService.setResponse(response);
                channelHandlerContext.channel().close().sync();
        }

    }

}
