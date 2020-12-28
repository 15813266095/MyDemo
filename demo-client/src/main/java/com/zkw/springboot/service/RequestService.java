package com.zkw.springboot.service;

import com.zkw.springboot.protocol.Message;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author zhangkewei
 * @date 2020/12/23 15:29
 * @desc 请求服务，处理由服务器主动发起的请求（CHANGEMAP,DISCONNECT,REFRESH）
 */
public interface RequestService {
    /**
     * 断开连接请求
     * @param ctx
     * @param response
     */
    void disconnect(ChannelHandlerContext ctx, Message response);

    /**
     * 切换地图请求
     * @param ctx
     * @param response
     */
    void changeMap(ChannelHandlerContext ctx, Message response);

    /**
     * 刷新资源请求
     * @param ctx
     * @param response
     */
    void refresh(ChannelHandlerContext ctx, Message response);
}
