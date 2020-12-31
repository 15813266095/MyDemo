package com.zkw.springboot.protocol;

import java.io.Serializable;

/**
 * 定义了请求要使用的线程池类型
 */
public enum ExecutorType implements Serializable {
    /**
     * 用户操作类型
     */
    USER,

    /**
     * 地图操作类型
     */
    MAP,

    /**
     * 服务器发送给客户端的消息类型
     */
    CLIENT;
}
