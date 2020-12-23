package com.zkw.springboot.service;

import com.zkw.springboot.protocol.Message;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author zhangkewei
 * @date 2020/12/23 11:40
 * @desc 用户服务
 */
public interface UserService {
    /**
     * 处理登录请求
     * @param ctx
     * @param request
     */
    void login(ChannelHandlerContext ctx, Message request);

    /**
     * 处理断开连接请求
     * @param ctx
     * @param request
     */
    void disconnect(ChannelHandlerContext ctx, Message request);

    /**
     * 处理角色移动请求
     * @param ctx
     * @param request
     */
    void move(ChannelHandlerContext ctx, Message request);

    /**
     * 角色注册请求
     * @param ctx
     * @param request
     */
    void register(ChannelHandlerContext ctx, Message request);

    /**
     * 获取角色信息
     * @param ctx
     * @param request
     */
    void get(ChannelHandlerContext ctx, Message request);
}
