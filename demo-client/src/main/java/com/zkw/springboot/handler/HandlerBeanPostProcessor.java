package com.zkw.springboot.handler;

import com.zkw.springboot.annotation.handler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author zhangkewei
 * @date 2020/12/22 16:40
 * @desc 回调类，在启动时将对应的处理器加载到map中
 */
@Component
public class HandlerBeanPostProcessor implements BeanPostProcessor {

    @Autowired
    private MessageHandlerManager messageHandlerManager;

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Method[] methods = bean.getClass().getMethods();
        for (Method method : methods) {
            if(method.isAnnotationPresent(handler.class)){
                messageHandlerManager.getMethodMap().put(method.getAnnotation(handler.class).messageType(),method);
                messageHandlerManager.getBeanMap().put(method.getAnnotation(handler.class).messageType(),bean);
            }
        }
        return bean;
    }
}
