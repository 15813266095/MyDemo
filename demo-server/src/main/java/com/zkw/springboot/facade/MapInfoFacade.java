package com.zkw.springboot.facade;

import com.zkw.springboot.annotation.HandlerAnno;
import com.zkw.springboot.protocol.Message;
import com.zkw.springboot.protocol.MessageType;
import com.zkw.springboot.service.MapInfoService;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhangkewei
 * @date 2020/12/23 11:42
 * @desc 地图外观
 */
@Component
public class MapInfoFacade {
    @Autowired
    private MapInfoService mapInfoService;

    /**
     * 分发到切换地图逻辑
     * @param ctx
     * @param request
     */
    @HandlerAnno(messageType = MessageType.CHANGEMAP)
    public void changeMap(ChannelHandlerContext ctx, Message request){
        if (mapInfoService.userExit(ctx, request)) {
            mapInfoService.userEnter(ctx, request);
        }
    }
}
