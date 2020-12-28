package com.zkw.springboot.service;

import com.zkw.springboot.bean.MapInfo;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

/**
 * Sse服务，可以由客户端主动向网页端发起请求，通过网页端的轮询访问实现
 */
public interface SseService {
    /**
     * 获取消息发送类
     * @return
     */
    SseEmitter getInfiniteMessages();

    /**
     * 向网页发送消息
     * @param mapInfoMap
     */
    void notifyListeners(Map<Integer, MapInfo> mapInfoMap);

    /**
     * 断开Sse服务
     * @param message
     */
    void disconnect(String message);
}
