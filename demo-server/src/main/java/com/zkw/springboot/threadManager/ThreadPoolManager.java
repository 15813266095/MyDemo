package com.zkw.springboot.threadManager;

import com.zkw.springboot.protocol.ExecutorType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangkewei
 * @date 2020/12/28 15:19
 * @desc 管理线程池
 */
@Component
public class ThreadPoolManager {
    //管理所有线程池的对象
    private final Map<ExecutorType,Map> map = new HashMap<>();

    public Map<ExecutorType, Map> getMap() {
        return map;
    }

}
