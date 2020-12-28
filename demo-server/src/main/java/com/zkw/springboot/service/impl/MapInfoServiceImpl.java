package com.zkw.springboot.service.impl;

import com.zkw.springboot.bean.MapInfo;
import com.zkw.springboot.bean.User;
import com.zkw.springboot.protocol.Message;
import com.zkw.springboot.protocol.MessageType;
import com.zkw.springboot.resource.MapInfoManager;
import com.zkw.springboot.service.BroadcastService;
import com.zkw.springboot.service.MapInfoService;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhangkewei
 * @date 2020/12/23 11:41
 * @desc 地图服务
 */
@Service
public class MapInfoServiceImpl implements MapInfoService {

    @Autowired
    private MapInfoManager mapInfoManager;
    @Autowired
    private BroadcastService broadcastService;

    public boolean userExit(ChannelHandlerContext ctx, Message request){
        User user = (User) request.map.get("user");
        Integer mapId = user.getMapId();//用户要去的地图id
        Integer oldMapId = (Integer) request.map.get("oldMapId");//用户原本所在的地图id
        if (mapId==oldMapId) {
            Message response = new Message();
            response.map.put("user",user);
            response.setMessageType(MessageType.ERROR);
            response.setDescription("已经在这地图里了");
            ctx.writeAndFlush(response);
            return false;
        }
        mapInfoManager.getMapInfoMap().get(oldMapId).exitUser(user);//旧地图删除用户
        return true;
    }

    public void userEnter(ChannelHandlerContext ctx, Message request){
        User user = (User) request.map.get("user");
        Integer mapId = user.getMapId();//用户要去的地图id
        Integer oldMapId = (Integer) request.map.get("oldMapId");//用户原本所在的地图id
        MapInfo mapInfo = mapInfoManager.getMapInfoMap().get(mapId);
        user.setPositionX(mapInfo.getPositionX());
        user.setPositionY(mapInfo.getPositionY());
        mapInfo.enterUser(user);//新地图增加用户

        Message messageToAll = new Message();
        messageToAll.setMessageType(MessageType.CHANGEMAP);
        messageToAll.map.put("oldMapId",oldMapId);
        messageToAll.map.put("user",user);
        messageToAll.setDescription(user.getUsername()+"去地图"+user.getMapId()+"了");
        broadcastService.sendMessageToAll(user.getAccount(),messageToAll);

        Message response = new Message();
        response.setMessageType(MessageType.SUCCESS);
        response.map.put("mapInfoMap", mapInfoManager.getMapInfoMap());
        response.setDescription("当前角色地图为：地图"+mapId);
        response.map.put("user",user);
        ctx.writeAndFlush(response);
    }

//    @Override
//    public void changeMap(ChannelHandlerContext ctx, Message request) {
//        User user = (User) request.map.get("user");
//        Integer mapId = user.getMapId();//用户要去的地图id
//        Integer oldMapId = (Integer) request.map.get("oldMapId");//用户原本所在的地图id
//
//        if (mapId==oldMapId) {
//            Message response = new Message();
//            response.map.put("user",user);
//            response.setMessageType(MessageType.ERROR);
//            response.setDescription("已经在这地图里了");
//            ctx.writeAndFlush(response);
//            return;
//        }
//
//        MapInfo mapInfo = mapInfoCache.getMapInfoMap().get(mapId);
//        mapInfo.enterUser(user);//新地图增加用户
//        mapInfoCache.getMapInfoMap().get(oldMapId).exitUser(user);//旧地图删除用户
//
//        user.setPositionX(mapInfo.getPositionX());
//        user.setPositionY(mapInfo.getPositionY());
//
//        Message messageToAll = new Message();
//        messageToAll.setMessageType(MessageType.CHANGEMAP);
//        messageToAll.map.put("oldMapId",oldMapId);
//        messageToAll.map.put("user",user);
//        messageToAll.setDescription(user.getUsername()+"去地图"+user.getMapId()+"了");
//        broadcastService.sendMessageToAll(user.getAccount(),messageToAll);
//
//        Message response = new Message();
//        response.setMessageType(MessageType.SUCCESS);
//        response.map.put("mapInfoMap",mapInfoCache.getMapInfoMap());
//        response.setDescription("当前角色地图为：地图"+mapId);
//        response.map.put("user",user);
//        ctx.writeAndFlush(response);
//    }
}
