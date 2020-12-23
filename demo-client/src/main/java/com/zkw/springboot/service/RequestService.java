package com.zkw.springboot.service;

import com.zkw.springboot.protocol.Message;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author zhangkewei
 * @date 2020/12/23 15:29
 * @desc 请求服务，处理由服务器主动发起的请求（CHANGEMAP,DISCONNECT,REFRESH）
 */
public interface RequestService {
    void disconnect(ChannelHandlerContext ctx, Message response);

    void changeMap(ChannelHandlerContext ctx, Message response);

    void refresh(ChannelHandlerContext ctx, Message response);
}
