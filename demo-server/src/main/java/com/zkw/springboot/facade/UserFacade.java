package com.zkw.springboot.facade;

import com.zkw.springboot.annotation.HandlerAnno;
import com.zkw.springboot.protocol.Message;
import com.zkw.springboot.protocol.MessageType;
import com.zkw.springboot.service.UserService;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhangkewei
 * @date 2020/12/23 11:42
 * @desc 用户外观
 */
@Component
public class UserFacade {

    @Autowired
    private UserService userService;

    @HandlerAnno(messageType = MessageType.LOGIN)
    public void login(ChannelHandlerContext ctx, Message request){
        userService.login(ctx, request);
    }

    @HandlerAnno(messageType = MessageType.DISCONNECT)
    public void disconnect(ChannelHandlerContext ctx, Message request){
        userService.disconnect(ctx, request);
    }

    @HandlerAnno(messageType = MessageType.MOVE)
    public void move(ChannelHandlerContext ctx, Message request){
        userService.move(ctx, request);
    }

    @HandlerAnno(messageType = MessageType.REGISTER)
    public void register(ChannelHandlerContext ctx, Message request){
        userService.register(ctx, request);
    }

    @HandlerAnno(messageType = MessageType.GET)
    public void get(ChannelHandlerContext ctx, Message request){
        userService.get(ctx, request);
    }
}
