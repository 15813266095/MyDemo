package com.zkw.springboot.facade;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.ReadListener;
import com.zkw.springboot.annotation.HandlerAnno;
import com.zkw.springboot.annotation.ResourceAnno;
import com.zkw.springboot.annotation.ThreadPoolAnno;
import com.zkw.springboot.threadManager.ThreadPoolManager;
import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * @author zhangkewei
 * @date 2020/12/22 12:05
 * @desc 回调类，负责扫描容器中的类，将有注解的类的方法按照类型存入MethodMap中，同时把对应的对象存入BeanMap中。
 */
@Component
public class HandlerBeanPostProcessor implements BeanPostProcessor {

    //核心线程数
    @Value("${demoServer.threadPool.corePoolSize}")
    private int corePoolSize;
    //最大线程数
    @Value("${demoServer.threadPool.maxPoolSize}")
    private int maxPoolSize;
    //阻塞队列大小
    @Value("${demoServer.threadPool.queueCapacity}")
    private int queueCapacity;
    //空闲线程的存活时间
    @Value("${demoServer.threadPool.keepAliveSeconds}")
    private int keepAliveSeconds;
    @Autowired
    private MessageHandlerManager messageHandlerManager;
    @Autowired
    private ThreadPoolManager threadPoolManager;

    @SneakyThrows
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        if(bean.getClass().isAnnotationPresent(ThreadPoolAnno.class)){
            Field field = bean.getClass().getField("executorMap");
            field.setAccessible(true);
            Map<Integer, Executor> executorMap = (Map<Integer, Executor>) field.get(bean);
            Field value = bean.getClass().getField("threadPoolCount");
            Integer threadPoolCount = (Integer) value.get(bean);
            for (int i = 1; i <= threadPoolCount; i++) {
                ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
                executor.setCorePoolSize(corePoolSize);
                executor.setMaxPoolSize(maxPoolSize);
                executor.setQueueCapacity(queueCapacity);
                executor.setKeepAliveSeconds(keepAliveSeconds);
                executor.setThreadNamePrefix(beanName+"-threadPool" + i + "-");
                executor.initialize();
                executorMap.put(i, executor);
            }
            threadPoolManager.getMap().put(bean.getClass().getAnnotation(ThreadPoolAnno.class).pooltype(),executorMap);
        }

        Method[] methods = bean.getClass().getDeclaredMethods();
        for (Method method : methods) {
            /**
             * 根据标记了HandlerAnno注解的方法构造成Map
             */
            if(method.isAnnotationPresent(HandlerAnno.class)){
                messageHandlerManager.getMethodMap().put(method.getAnnotation(HandlerAnno.class).messageType(),method);
                messageHandlerManager.getBeanMap().put(method.getAnnotation(HandlerAnno.class).messageType(),bean);
            }

            /**
             * 给资源类读取资源
             */
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
