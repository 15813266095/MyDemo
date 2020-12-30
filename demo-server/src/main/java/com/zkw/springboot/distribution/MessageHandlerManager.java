package com.zkw.springboot.distribution;

import com.zkw.springboot.bean.User;
import com.zkw.springboot.protocol.ExecutorType;
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
import java.util.concurrent.Executor;

/**
 * @author zhangkewei
 * @date 2020/12/22 11:09
 * @desc 分发管理器，负责将收到的请求按照类型，分发到对应的线程池，并执行对应的facade类中的方法
 */
@Slf4j
@Component
@Data
public class MessageHandlerManager {

    /**
     * 线程池管理，负责分配线程池
     */
    @Autowired
    private ThreadPoolManager threadPoolManager;

    /**
     * 保存对应操作类型的方法
     */
    private Map<MessageType, Method> methodMap = new HashMap<>();

    /**
     * 保存调用方法时要使用的对象
     */
    private Map<MessageType, Object> beanMap = new HashMap<>();

    /**
     * 处理所有请求，将请求分发到对应的外观类和对应的线程池
     * @param messageType
     * @param ctx
     * @param request
     */
    public void invokeMethod(MessageType messageType, ChannelHandlerContext ctx, Message request) {
        ExecutorType executorType = request.getMessageType().getExecutorType();
        Map<Integer, Executor> executorMap = threadPoolManager.getMap().get(executorType);
        User user = (User) request.map.get("user");
        int i = user.getAccount().hashCode() % threadPoolManager.getCount().get(executorType) + 1;
        Method method = methodMap.get(messageType);
        executorMap.get(i).execute(()->{
            try {
                method.invoke(beanMap.get(messageType), ctx, request);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }
}
