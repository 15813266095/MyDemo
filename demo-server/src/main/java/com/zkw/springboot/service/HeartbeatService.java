package com.zkw.springboot.service;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author zhangkewei
 * @date 2020/12/23 12:06
 * @desc 心跳检测服务
 */
public interface HeartbeatService{
    /**
     * 心跳检测超时，服务器主动发送断开连接请求
     * @param ctx
     */
    void disconnect(ChannelHandlerContext ctx);

    /**
     * 服务器发送断开连接请求，同时也要处理服务器内部自身的数据
     * @param ctx
     */
    void safeDisconnect(ChannelHandlerContext ctx);
}
