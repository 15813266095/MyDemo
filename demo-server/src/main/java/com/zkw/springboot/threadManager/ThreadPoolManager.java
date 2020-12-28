package com.zkw.springboot.threadManager;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * @author zhangkewei
 * @date 2020/12/28 15:19
 * @desc 管理线程池
 */
@Component
public class ThreadPoolManager {
    //用户线程池数量
    @Value("${demoServer.threadPool.userThreadPoolCount}")
    public int userThreadPoolCount;
    //地图线程池数量
    @Value("${demoServer.threadPool.mapInfoThreadPoolCount}")
    public int mapInfoThreadPoolCount;

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

    //用户线程池的管理map
    private final Map<Integer, Executor> userExecutorMap = new HashMap<>();
    //地图线程池的管理map
    private final Map<Integer, Executor> mapInfoExecutorMap = new HashMap<>();

    public Map<Integer, Executor> getUserExecutorMap() {
        return userExecutorMap;
    }

    public Map<Integer, Executor> getMapInfoExecutorMap() {
        return mapInfoExecutorMap;
    }

    @PostConstruct
    private void init(){
        for (int i = 1; i <= userThreadPoolCount; i++) {
            ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
            executor.setCorePoolSize(corePoolSize);
            executor.setMaxPoolSize(maxPoolSize);
            executor.setQueueCapacity(queueCapacity);
            executor.setKeepAliveSeconds(keepAliveSeconds);
            executor.setThreadNamePrefix("user"+i+"-threadPool-");
            executor.initialize();
            userExecutorMap.put(i,executor);
        }
        for (int i = 1; i <= mapInfoThreadPoolCount; i++) {
            ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
            executor.setCorePoolSize(corePoolSize);
            executor.setMaxPoolSize(maxPoolSize);
            executor.setQueueCapacity(queueCapacity);
            executor.setKeepAliveSeconds(keepAliveSeconds);
            executor.setThreadNamePrefix("mapInfo"+i+"-threadPool-");
            executor.initialize();
            mapInfoExecutorMap.put(i,executor);
        }
    }
}
