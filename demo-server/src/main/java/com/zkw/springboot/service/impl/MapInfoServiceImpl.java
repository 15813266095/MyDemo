package com.zkw.springboot.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.zkw.springboot.bean.MapInfo;
import com.zkw.springboot.bean.User;
import com.zkw.springboot.protocol.Message;
import com.zkw.springboot.protocol.MessageType;
import com.zkw.springboot.service.BroadcastService;
import com.zkw.springboot.service.MapInfoService;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author zhangkewei
 * @date 2020/12/23 11:41
 * @desc 地图服务
 */
@Service
@Slf4j
public class MapInfoServiceImpl implements MapInfoService {

    @Autowired
    private Cache<Integer, MapInfo> mapInfoCache;
    @Autowired
    private Cache<String, User> connectedUserCache;
    @Autowired
    private BroadcastService broadcastService;

    /**
     * 用户退出地图
     * @param ctx
     * @param request
     * @return
     */
    public boolean userExit(ChannelHandlerContext ctx, Message request){
        ConcurrentMap<Integer,MapInfo> mapInfoMap = mapInfoCache.asMap();
        User user = (User) request.map.get("user");
        Integer mapId = user.getMapId();//用户要去的地图id
        Integer oldMapId = (Integer) request.map.get("oldMapId");//用户原本所在的地图id
        if (mapId.equals(oldMapId)) {
            Message response = new Message();
            response.map.put("user",user);
            response.setMessageType(MessageType.ERROR);
            response.setDescription("已经在这地图里了");
            ctx.writeAndFlush(response);
            return false;
        }
        mapInfoMap.get(oldMapId).exitUser(user);//旧地图删除用户
        return true;
    }

    /**
     * 用户进入地图
     * @param ctx
     * @param request
     */
    public void userEnter(ChannelHandlerContext ctx, Message request){
        ConcurrentMap<Integer,MapInfo> mapInfoMap = mapInfoCache.asMap();
        User user = (User) request.map.get("user");
        Integer mapId = user.getMapId();//用户要去的地图id
        Integer oldMapId = (Integer) request.map.get("oldMapId");//用户原本所在的地图id
        MapInfo mapInfo = mapInfoMap.get(mapId);
        user.setPositionX(mapInfo.getPositionX());
        user.setPositionY(mapInfo.getPositionY());
        mapInfo.enterUser(user);//新地图增加用户
        connectedUserCache.asMap().put(user.getAccount(),user);
        broadcastService.changeMapToAll(user,oldMapId);

        Message response = new Message();
        response.setMessageType(MessageType.SUCCESS);
        ConcurrentMap<Integer, MapInfo> tempMap = new ConcurrentHashMap<>();
        tempMap.putAll(mapInfoMap);
        response.map.put("mapInfoMap", tempMap);
        response.setDescription("当前角色地图为：地图"+mapId);
        response.map.put("user",user);
        ctx.writeAndFlush(response);
    }

}
