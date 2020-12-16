package com.zkw.springboot.handler;

import com.zkw.springboot.protocol.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangkewei
 * @date 2020/12/16 15:31
 * @desc 进行请求分发处理
 */
@Component
public class HandlerManager {

    private Map<MessageType, IMessageHandler> messageTypeIMessageHandlerMap=new HashMap<>();

    public Map<MessageType, IMessageHandler> getMessageTypeIMessageHandlerMap() {
        return messageTypeIMessageHandlerMap;
    }

    @Autowired
    public void setIMessageHandler(List<IMessageHandler> iMessageHandlers) {
        for (IMessageHandler iMessageHandler : iMessageHandlers) {
            messageTypeIMessageHandlerMap.put(iMessageHandler.getMessageType(), iMessageHandler);
        }
    }
}
