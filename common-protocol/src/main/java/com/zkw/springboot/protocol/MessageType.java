package com.zkw.springboot.protocol;

import java.io.Serializable;

/**
 * @author zhangkewei
 * @date 2020/12/16 15:39
 * @desc 定义了消息的类型
 */
public enum MessageType implements Serializable {
    //注册请求
    REGISTER,
    //登录请求
    LOGIN,
    //查看角色信息请求
    GET,
    //角色移动请求
    MOVE,
    //场景切换请求
    CHANGE_SCENES,
    //断开连接请求
    DISCONNECT,
    //操作成功
    SUCCESS,
    //操作失败
    ERROR,
    //刷新
    REFRESH;
}
