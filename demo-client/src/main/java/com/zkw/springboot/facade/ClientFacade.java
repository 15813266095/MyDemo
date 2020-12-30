package com.zkw.springboot.facade;

import com.zkw.springboot.annotation.HandlerAnno;
import com.zkw.springboot.protocol.Message;
import com.zkw.springboot.protocol.MessageType;
import com.zkw.springboot.service.RequestService;
import com.zkw.springboot.service.ResponseService;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhangkewei
 * @date 2020/12/23 15:24
 * @desc 客户端的外观类，用于将任务分发到对应服务中
 */
@Component
public class ClientFacade {

    @Autowired
    ResponseService responseService;
    @Autowired
    RequestService requestService;

    /**
     * 分发到请求成功逻辑
     * @param ctx
     * @param response
     */
    @HandlerAnno(messageType = MessageType.SUCCESS)
    public void success(ChannelHandlerContext ctx, Message response){
        responseService.success(ctx,response);
    }

    /**
     * 分发到刷新逻辑
     * @param ctx
     * @param response
     */
    @HandlerAnno(messageType = MessageType.REFRESH)
    public void refresh(ChannelHandlerContext ctx, Message response){
        requestService.refresh(ctx,response);
    }

    /**
     * 分发到错误处理逻辑
     * @param ctx
     * @param response
     */
    @HandlerAnno(messageType = MessageType.ERROR)
    public void error(ChannelHandlerContext ctx, Message response){
        responseService.error(ctx,response);
    }

    /**
     * 分发到断开连接逻辑
     * @param ctx
     * @param response
     */
    @HandlerAnno(messageType = MessageType.DISCONNECT)
    public void disconnect(ChannelHandlerContext ctx, Message response){
        requestService.disconnect(ctx,response);
    }

    /**
     * 分发到切换地图逻辑
     * @param ctx
     * @param response
     */
    @HandlerAnno(messageType = MessageType.CHANGEMAP)
    public void changeMap(ChannelHandlerContext ctx, Message response){
        requestService.changeMap(ctx,response);
    }

}
