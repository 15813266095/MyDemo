package com.zkw.springboot.facade;

import com.zkw.springboot.protocol.Message;
import com.zkw.springboot.protocol.MessageType;
import com.zkw.springboot.threadManager.ThreadPoolManager;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangkewei
 * @date 2020/12/22 11:09
 * @desc 分发管理器，负责将收到的请求按照类型分发到不同的facade类中的方法里
 */
@Slf4j
@Component
@Data
public class MessageHandlerManager {

    @Autowired
    private ThreadPoolManager threadPoolManager;
    private Map<MessageType, Method> methodMap = new HashMap<>();
    private Map<MessageType, Object> beanMap = new HashMap<>();

    public void invokeMethod(MessageType messageType, ChannelHandlerContext ctx, Message request) {
        threadPoolManager.getMap().get(request.getMessageType().getPoolType());
        Method method = methodMap.get(messageType);
        try {
            method.invoke(beanMap.get(messageType), ctx, request);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
