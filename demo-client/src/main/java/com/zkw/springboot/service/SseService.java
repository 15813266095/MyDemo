package com.zkw.springboot.service;

import com.zkw.springboot.bean.MapInfo;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

public interface SseService {
    SseEmitter getInfiniteMessages();
    void notifyListeners(Map<Integer, MapInfo> mapInfoMap);
    void disconnect(String message);
}
