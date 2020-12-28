package com.zkw.springboot.service;

import com.zkw.springboot.protocol.Message;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author zhangkewei
 * @date 2020/12/23 15:29
 * @desc 响应服务，处理由客户端发起请求获得的响应（SUCCESS,ERROR）
 */
public interface ResponseService {
    /**
     * 成功请求
     * @param ctx
     * @param response
     */
    void success(ChannelHandlerContext ctx, Message response);

    /**
     * 失败请求
     * @param ctx
     * @param response
     */
    void error(ChannelHandlerContext ctx, Message response);

}
