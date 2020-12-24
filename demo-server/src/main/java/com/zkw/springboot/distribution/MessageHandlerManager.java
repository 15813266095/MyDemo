package com.zkw.springboot.distribution;

import com.zkw.springboot.protocol.MessageType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
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

    private Map<MessageType, Method> methodMap = new HashMap<>();
    private Map<MessageType, Object> beanMap = new HashMap<>();

    public void invokeMethod(MessageType messageType, Object... args) throws InvocationTargetException, IllegalAccessException {
        Method method = methodMap.get(messageType);
        Parameter[] parameters = method.getParameters();
        if(parameters.length!=args.length){
            log.error("参数错误，运行失败");
            return;
        }
        method.invoke(beanMap.get(messageType),args);
    }
}
