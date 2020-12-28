package com.zkw.springboot.service;

import com.zkw.springboot.protocol.Message;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author zhangkewei
 * @date 2020/12/23 11:41
 * @desc 地图服务
 */
public interface MapInfoService {
    /**
     * 处理退出地图的请求
     * @param ctx
     * @param request
     * @return
     */
    boolean userExit(ChannelHandlerContext ctx, Message request);

    /**
     * 处理进入地图的请求
     * @param ctx
     * @param request
     */
    void userEnter(ChannelHandlerContext ctx, Message request);
}
