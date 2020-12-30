package com.zkw.springboot.protocol;

import java.io.Serializable;

/**
 * 定义了请求要使用的线程池类型
 */
public enum ExecutorType implements Serializable {
    USER, MAP;
}
