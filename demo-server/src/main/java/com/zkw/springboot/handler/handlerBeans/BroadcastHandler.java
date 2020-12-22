package com.zkw.springboot.handler.handlerBeans;

import com.zkw.springboot.handler.DataManager;
import com.zkw.springboot.protocol.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhangkewei
 * @date 2020/12/22 12:21
 * @desc 广播功能
 */
@Component
public class BroadcastHandler {
    @Autowired
    DataManager dataManager;

    public void sendMessageToAll(String account, Message message){
        dataManager.getConcurrentMap().forEach((k, v) -> {
            if(!account.equals(k)){
                v.writeAndFlush(message);
            }
        });
    }
}
