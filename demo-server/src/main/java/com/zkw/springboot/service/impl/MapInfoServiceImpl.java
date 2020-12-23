package com.zkw.springboot.service.impl;

import com.zkw.springboot.bean.MapInfo;
import com.zkw.springboot.bean.User;
import com.zkw.springboot.cache.MapInfoCache;
import com.zkw.springboot.cache.UserCache;
import com.zkw.springboot.protocol.Message;
import com.zkw.springboot.protocol.MessageType;
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
    private UserCache userCache;
    @Autowired
    private MapInfoCache mapInfoCache;
    @Autowired
    private BroadcastService broadcastService;

    @Override
    public void changeMap(ChannelHandlerContext ctx, Message request) {
        User user = request.getUser();
        Integer mapId = user.getMapId();//用户要去的地图id
        Integer oldMapId = request.getOldMapId();//用户原本所在的地图id

        if (mapId==oldMapId) {
            Message response = new Message();
            response.setUser(user);
            response.setMessageType(MessageType.ERROR);
            response.setDescription("已经在这地图里了");
            ctx.writeAndFlush(response);
            return;
        }

        MapInfo mapInfo = mapInfoCache.getMapInfoMap().get(mapId);
        mapInfo.enterUser(user);//新地图增加用户
        mapInfoCache.getMapInfoMap().get(oldMapId).exitUser(user);//旧地图删除用户

        user.setPositionX(mapInfo.getPositionX());
        user.setPositionY(mapInfo.getPositionY());

        Message messageToAll = new Message();
        messageToAll.setMessageType(MessageType.CHANGEMAP);
        messageToAll.setOldMapId(oldMapId);
        messageToAll.setUser(user);
        messageToAll.setDescription(user.getUsername()+"去地图"+user.getMapId()+"了");
        broadcastService.sendMessageToAll(user.getAccount(),messageToAll);

        Message response = new Message();
        response.setMessageType(MessageType.SUCCESS);
        response.setMapInfoMap(mapInfoCache.getMapInfoMap());
        response.setDescription("当前角色地图为：地图"+mapId);
        response.setUser(user);
        ctx.writeAndFlush(response);
    }
}
