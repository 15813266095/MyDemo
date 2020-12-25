package com.zkw.springboot.distribution;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.ReadListener;
import com.zkw.springboot.annotation.HandlerAnno;
import com.zkw.springboot.annotation.ResourceAnno;
import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author zhangkewei
 * @date 2020/12/22 12:05
 * @desc 回调类，负责扫描容器中的类，将有注解的类的方法按照类型存入MethodMap中，同时把对应的对象存入BeanMap中，便于invoke
 */
@Component
public class HandlerBeanPostProcessor implements BeanPostProcessor {

    @Autowired
    private MessageHandlerManager messageHandlerManager;

    @SneakyThrows
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Method[] methods = bean.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if(method.isAnnotationPresent(HandlerAnno.class)){
                messageHandlerManager.getMethodMap().put(method.getAnnotation(HandlerAnno.class).messageType(),method);
                messageHandlerManager.getBeanMap().put(method.getAnnotation(HandlerAnno.class).messageType(),bean);
            }
            if(method.isAnnotationPresent(ResourceAnno.class)){
                Field field= bean.getClass().getDeclaredField("fileName");
                field.setAccessible(true);
                String fileName = (String)field.get(bean);
                Class<?> bean1 = method.getAnnotation(ResourceAnno.class).bean();
                ReadListener newListener = (ReadListener)method.getAnnotation(ResourceAnno.class).listener().newInstance();
                EasyExcel.read(fileName, bean1, newListener).sheet().doRead();
                Object list = newListener.getClass().getMethod("getList").invoke(newListener);
                method.setAccessible(true);
                method.invoke(bean, list);
            }
        }
        return bean;
    }
}
