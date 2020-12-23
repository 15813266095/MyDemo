package com.zkw.springboot.service;

import com.zkw.springboot.protocol.Message;

/**
 * @author zhangkewei
 * @date 2020/12/22 12:21
 * @desc 广播服务
 */
public interface BroadcastService{
    /**
     * 将消息发送给除了自己的所有连接用户，由服务器主动发起
     * @param account
     * @param message
     */
    void sendMessageToAll(String account, Message message);
}
