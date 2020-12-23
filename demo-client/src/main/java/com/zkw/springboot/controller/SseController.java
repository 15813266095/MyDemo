package com.zkw.springboot.controller;

import com.zkw.springboot.service.impl.SseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @author zhangkewei
 * @date 2020/12/17 14:39
 * @desc
 */
@RestController
public class SseController {

    private final SseServiceImpl sseService;

    @Autowired
    public SseController(SseServiceImpl sseService) {
        this.sseService = sseService;
    }

    @RequestMapping(path = "/sse", method = RequestMethod.GET)
    public SseEmitter getInfiniteMessages() {
        return sseService.getInfiniteMessages();
    }
}
