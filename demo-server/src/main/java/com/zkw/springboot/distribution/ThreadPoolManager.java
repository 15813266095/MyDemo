package com.zkw.springboot.distribution;

import com.zkw.springboot.protocol.ExecutorType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * @author zhangkewei
 * @date 2020/12/28 15:19
 * @desc 管理所有线程池类型，以及其对应的数量
 */
@Component
public class ThreadPoolManager {

    /**
     * 管理所有线程池
     */
    private final Map<ExecutorType,Map<Integer, Executor>> map = new HashMap<>();

    /**
     * 管理所有线程池对应的数量
     */
    private final Map<ExecutorType,Integer> count = new HashMap<>();

    public Map<ExecutorType, Map<Integer, Executor>> getMap() {
        return map;
    }

    public Map<ExecutorType, Integer> getCount() {
        return count;
    }
}
