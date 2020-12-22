package com.zkw.springboot.handler.handlerBeans;

import com.zkw.springboot.annotation.handler;
import com.zkw.springboot.protocol.Message;
import com.zkw.springboot.protocol.MessageType;
import com.zkw.springboot.service.ClientService;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhangkewei
 * @date 2020/12/17 12:19
 * @desc 用于接收服务器主动传来的消息并实时刷新到页面
 */
@Slf4j
@Component
public class RefreshHandler {

    @Autowired
    ClientService clientService;

    @handler(messageType = MessageType.REFRESH)
    public void refreshHandler(ChannelHandlerContext ctx, Message response) {
        log.info(response.getDescription());
        try {
            clientService.setInfo(response);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
