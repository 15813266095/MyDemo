package com.zkw.springboot.facade;

import com.zkw.springboot.annotation.HandlerAnno;
import com.zkw.springboot.bean.User;
import com.zkw.springboot.protocol.Message;
import com.zkw.springboot.protocol.MessageType;
import com.zkw.springboot.service.UserService;
import com.zkw.springboot.threadManager.ThreadPoolManager;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;

/**
 * @author zhangkewei
 * @date 2020/12/23 11:42
 * @desc 用户外观
 */
@Component
public class UserFacade {

    @Autowired
    private UserService userService;
    @Autowired
    private ThreadPoolManager threadPoolManager;

    @HandlerAnno(messageType = MessageType.LOGIN)
    public void login(ChannelHandlerContext ctx, Message request){
        User user = (User) request.map.get("user");
        int i = user.getAccount().hashCode() % threadPoolManager.userThreadPoolCount + 1;
        Executor executor = threadPoolManager.getUserExecutorMap().get(i);
        executor.execute(()->{ userService.login(ctx, request); });
    }

    @HandlerAnno(messageType = MessageType.DISCONNECT)
    public void disconnect(ChannelHandlerContext ctx, Message request){
        User user = (User) request.map.get("user");
        int i = user.getAccount().hashCode() % threadPoolManager.userThreadPoolCount + 1;
        Executor executor = threadPoolManager.getUserExecutorMap().get(i);
        executor.execute(()->{ userService.disconnect(ctx, request); });
    }

    @HandlerAnno(messageType = MessageType.MOVE)
    public void move(ChannelHandlerContext ctx, Message request){
        User user = (User) request.map.get("user");
        int i = user.getAccount().hashCode() % threadPoolManager.userThreadPoolCount + 1;
        Executor executor = threadPoolManager.getUserExecutorMap().get(i);
        executor.execute(()->{ userService.move(ctx, request); });
    }

    @HandlerAnno(messageType = MessageType.REGISTER)
    public void register(ChannelHandlerContext ctx, Message request){
        User user = (User) request.map.get("user");
        int i = user.getAccount().hashCode() % threadPoolManager.userThreadPoolCount + 1;
        Executor executor = threadPoolManager.getUserExecutorMap().get(i);
        executor.execute(()->{ userService.register(ctx, request); });
    }

    @HandlerAnno(messageType = MessageType.GET)
    public void get(ChannelHandlerContext ctx, Message request){
        User user = (User) request.map.get("user");
        int i = user.getAccount().hashCode() % threadPoolManager.userThreadPoolCount + 1;
        Executor executor = threadPoolManager.getUserExecutorMap().get(i);
        executor.execute(()->{ userService.get(ctx, request); });
    }
}
