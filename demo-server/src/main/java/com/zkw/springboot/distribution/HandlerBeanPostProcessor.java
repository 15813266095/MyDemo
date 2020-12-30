package com.zkw.springboot.distribution;

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

    /**
     * 所有spring中注册的类在构造后，都会执行这一方法
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @SneakyThrows
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        /**
         * 该类标注了线程池注解
         */
        if(bean.getClass().isAnnotationPresent(ThreadPoolAnno.class)){
            /**
             * 获取线程池的map
             */
            Field field = bean.getClass().getDeclaredField("executorMap");
            field.setAccessible(true);
            Map<Integer, Executor> executorMap = (Map<Integer, Executor>) field.get(bean);
            /**
             * 获取线程池的数量
             */
            Field value = bean.getClass().getDeclaredField("threadPoolCount");
            value.setAccessible(true);
            Integer threadPoolCount = (Integer) value.get(bean);
            /**
             * 构造线程池，放入map中
             */
            for (int i = 1; i <= threadPoolCount; i++) {
                ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
                executor.setCorePoolSize(corePoolSize);
                executor.setMaxPoolSize(maxPoolSize);
                executor.setQueueCapacity(queueCapacity);
                executor.setKeepAliveSeconds(keepAliveSeconds);
                executor.setThreadNamePrefix(beanName+"Pool-" + i + "-");
                executor.initialize();
                executorMap.put(i, executor);
            }
            /**
             * 将线程池的map和数量保存在线程池管理器中
             */
            threadPoolManager.getMap().put(bean.getClass().getAnnotation(ThreadPoolAnno.class).executorType(),executorMap);
            threadPoolManager.getCount().put(bean.getClass().getAnnotation(ThreadPoolAnno.class).executorType(),threadPoolCount);
        }

        /**
         * 扫描所有方法
         */
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
             * 读取资源，构造成资源对象
             */
            if(method.isAnnotationPresent(ResourceAnno.class)){
                /**
                 * 获取文件路径
                 */
                Field field= bean.getClass().getDeclaredField("fileName");
                field.setAccessible(true);
                String fileName = (String)field.get(bean);
                /**
                 * 创建读取资源的监听对象
                 */
                Class<?> bean1 = method.getAnnotation(ResourceAnno.class).bean();
                ReadListener newListener = (ReadListener)method.getAnnotation(ResourceAnno.class).listener().newInstance();
                /**
                 * 读取资源
                 */
                EasyExcel.read(fileName, bean1, newListener).sheet().doRead();
                /**
                 * 获取方法，将读取好的资源设置到Resource对象中
                 */
                Object list = newListener.getClass().getMethod("getList").invoke(newListener);
                method.setAccessible(true);
                method.invoke(bean, list);
            }
        }
        return bean;
    }
}
