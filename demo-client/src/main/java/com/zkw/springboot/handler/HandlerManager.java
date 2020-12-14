package com.zkw.springboot.handler;

import com.zkw.springboot.protocol.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
