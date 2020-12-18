package com.zkw.springboot.service;

import com.zkw.springboot.bean.MapInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

/**
 * @author zhangkewei
 * @date 2020/12/17 14:39
 * @desc
 */
@Component
@Slf4j
public class SseService {
    private final SseEmitter emitter;

    @Autowired
    public SseService() {
        emitter = new SseEmitter(0l);
    }

    public SseEmitter getInfiniteMessages() {
        return emitter;
    }

    public void notifyListeners(Map<Integer, MapInfo> mapInfoMap) {
        try {
            emitter.send(mapInfoMap);
        } catch (IOException e) {
            log.error("发送消息失败", e);
        }
    }

}
