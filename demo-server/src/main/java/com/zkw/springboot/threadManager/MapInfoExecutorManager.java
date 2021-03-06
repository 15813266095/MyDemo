package com.zkw.springboot.threadManager;

import com.zkw.springboot.annotation.ThreadPoolAnno;
import com.zkw.springboot.protocol.ExecutorType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * @author zhangkewei
 * @date 2020/12/29 17:43
 * @desc 地图线程池的管理map
 */
@Component
@ThreadPoolAnno(executorType = ExecutorType.MAP)
public class MapInfoExecutorManager {

    /**
     * 地图线程池数量
     */
    @Value("${demoServer.threadPool.mapInfoThreadPoolCount}")
    private int threadPoolCount;

    /**
     * 地图线程池的管理map
     */
    private Map<Integer, Executor> executorMap = new HashMap<>();

}
