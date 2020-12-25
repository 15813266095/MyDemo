package com.zkw.springboot.service.impl;

import com.zkw.springboot.bean.MapInfo;
import com.zkw.springboot.bean.User;
import com.zkw.springboot.protocol.Message;
import com.zkw.springboot.service.ClientService;
import com.zkw.springboot.service.RequestService;
import com.zkw.springboot.service.SseService;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author zhangkewei
 * @date 2020/12/23 16:19
 * @desc 处理服务端的请求
 */
@Slf4j
@Service
public class RequestServiceImpl implements RequestService {

    @Autowired
    ClientService clientService;
    @Autowired
    SseService sseService;

    @Override
    public void refresh(ChannelHandlerContext ctx, Message request) {
        log.info(request.getDescription());
        Map<Integer, MapInfo> mapInfoMap = clientService.getMapInfoMap();
        User user = (User) request.map.get("user");
        if(!mapInfoMap.get(user.getMapId()).getUsermap().containsKey(user.getAccount())){
            mapInfoMap.get(user.getMapId()).enterUser(user);
        }else {
            mapInfoMap.get(user.getMapId()).exitUser(user);
        }
        clientService.setMapInfoMap(mapInfoMap);
        sseService.notifyListeners(mapInfoMap);
    }

    @Override
    public void disconnect(ChannelHandlerContext ctx, Message request) {
        log.info(request.getDescription());
        clientService.setMapInfoMap((Map)request.map.get("mapInfoMap"));
        sseService.disconnect(request.getDescription());
    }

    @Override
    public void changeMap(ChannelHandlerContext ctx, Message request) {
        Map<Integer, MapInfo> mapInfoMap = clientService.getMapInfoMap();
        User user = (User) request.map.get("user");
        if(request.map.get("oldMapId")!=null){
            mapInfoMap.get(request.map.get("oldMapId")).exitUser(user);
        }
        mapInfoMap.get(user.getMapId()).enterUser(user);
        clientService.setMapInfoMap(mapInfoMap);
        sseService.notifyListeners(mapInfoMap);
    }
}
