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
     * 处理切换地图请求
     * @param ctx
     * @param request
     */
    void changeMap(ChannelHandlerContext ctx, Message request);
}
