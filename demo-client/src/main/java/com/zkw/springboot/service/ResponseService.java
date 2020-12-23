package com.zkw.springboot.service;

import com.zkw.springboot.protocol.Message;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author zhangkewei
 * @date 2020/12/23 15:29
 * @desc 响应服务，处理由客户端发起请求获得的响应（SUCCESS,ERROR）
 */
public interface ResponseService {

    void success(ChannelHandlerContext ctx, Message response);

    void error(ChannelHandlerContext ctx, Message response);

}
