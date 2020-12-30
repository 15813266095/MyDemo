package com.zkw.springboot.protocol;

import java.io.Serializable;

/**
 * @author zhangkewei
 * @date 2020/12/16 15:39
 * @desc 定义了消息的类型
 */
public enum MessageType implements Serializable {
    //注册请求
    REGISTER(ExecutorType.USER),
    //登录请求
    LOGIN(ExecutorType.USER),
    //查看角色信息请求
    GET(ExecutorType.USER),
    //角色移动请求
    MOVE(ExecutorType.USER),
    //场景切换请求
    CHANGEMAP(ExecutorType.MAP),
    //断开连接请求
    DISCONNECT(ExecutorType.USER),
    //操作成功
    SUCCESS(ExecutorType.USER),
    //操作失败
    ERROR(ExecutorType.USER),
    //刷新
    REFRESH(ExecutorType.USER);

    private ExecutorType executorType;

    MessageType(ExecutorType executorType) {
        this.executorType = executorType;
    }

    public ExecutorType getExecutorType() {
        return executorType;
    }
}
