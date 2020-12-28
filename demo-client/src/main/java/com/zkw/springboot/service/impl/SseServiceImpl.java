package com.zkw.springboot.service.impl;

import com.zkw.springboot.bean.MapInfo;
import com.zkw.springboot.service.SseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zhangkewei
 * @date 2020/12/17 14:39
 * @desc Sse工具，实现从服务端向客户端发送信息
 */
@Service
@Slf4j
public class SseServiceImpl implements SseService {
    private final SseEmitter emitter;

    @Autowired
    public SseServiceImpl() {
        emitter = new SseEmitter(0l);
    }

    @Override
    public SseEmitter getInfiniteMessages() {
        return emitter;
    }

    @Override
    public void notifyListeners(Map<Integer, MapInfo> mapInfoMap) {
        try {
            emitter.send(mapInfoMap);
        } catch (IOException e) {
            log.error("发送消息失败", e);
        }
    }

    @Override
    public void disconnect(String message){
        try {
            List<String> list = new ArrayList<>();
            list.add("disconnect");
            list.add(message);
            emitter.send(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
