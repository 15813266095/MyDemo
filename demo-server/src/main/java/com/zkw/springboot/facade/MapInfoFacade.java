package com.zkw.springboot.facade;

import com.zkw.springboot.annotation.HandlerAnno;
import com.zkw.springboot.bean.User;
import com.zkw.springboot.protocol.Message;
import com.zkw.springboot.protocol.MessageType;
import com.zkw.springboot.service.MapInfoService;
import com.zkw.springboot.threadManager.ThreadPoolManager;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;

/**
 * @author zhangkewei
 * @date 2020/12/23 11:42
 * @desc 地图外观层
 */
@Component
public class MapInfoFacade {
    @Autowired
    private MapInfoService mapInfoService;
    @Autowired
    private ThreadPoolManager threadPoolManager;

    @HandlerAnno(messageType = MessageType.CHANGEMAP)
    public void changeMap(ChannelHandlerContext ctx, Message request){
        User user = (User) request.map.get("user");
        int i = user.getMapId() % threadPoolManager.mapInfoThreadPoolCount + 1;
        Executor executor = threadPoolManager.getMapInfoExecutorMap().get(i);
        executor.execute(()->{
            if(mapInfoService.userExit(ctx, request)){
                mapInfoService.userEnter(ctx, request);
            }
        });
    }
}
