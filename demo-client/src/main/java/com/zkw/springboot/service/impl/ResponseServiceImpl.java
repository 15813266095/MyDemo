package com.zkw.springboot.service.impl;

import com.zkw.springboot.protocol.Message;
import com.zkw.springboot.service.ClientService;
import com.zkw.springboot.service.ResponseService;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhangkewei
 * @date 2020/12/23 15:31
 * @desc
 */

@Service
@Slf4j
public class ResponseServiceImpl implements ResponseService {

    @Autowired
    ClientService clientService;

    @Override
    public void success(ChannelHandlerContext ctx, Message response) {
        log.info(response.getDescription());
        try {
            clientService.put(response);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void error(ChannelHandlerContext ctx, Message response) {
        log.error(response.getDescription());
        try {
            clientService.put(response);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
