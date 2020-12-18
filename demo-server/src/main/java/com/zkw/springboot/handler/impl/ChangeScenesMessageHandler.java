package com.zkw.springboot.handler.impl;

import com.zkw.springboot.bean.MapInfo;
import com.zkw.springboot.bean.User;
import com.zkw.springboot.handler.DataManager;
import com.zkw.springboot.handler.IMessageHandler;
import com.zkw.springboot.protocol.Message;
import com.zkw.springboot.protocol.MessageType;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhangkewei
 * @date 2020/12/16 15:31
 * @desc 用于处理地图切换请求
 */
@Component
public class ChangeScenesMessageHandler implements IMessageHandler {

    @Autowired
    DataManager dataManager;

    @Override
    public MessageType getMessageType() {
        return MessageType.CHANGE_SCENES;
    }

    @Override
    public void operate(ChannelHandlerContext ctx, Message request) {
        User user = request.getUser();
        Integer mapId = user.getMapId();//用户要去的地图id
        Integer oldMapId = request.getOldMapId();//用户原本所在的地图id

        if (mapId.equals(oldMapId)) {
            Message response = new Message();
            response.setUser(user);
            response.setMessageType(MessageType.ERROR);
            response.setDescription("已经在这地图里了");
            ctx.writeAndFlush(response);
            return;
        }

        MapInfo mapInfo = dataManager.getMapInfoMap().get(mapId);
        mapInfo.addUser(user);//新地图增加用户
        dataManager.getMapInfoMap().get(oldMapId).removeUser(user);//旧地图删除用户

        user.setPositionX(mapInfo.getPositionX());
        user.setPositionY(mapInfo.getPositionY());

        Message messageToAll = new Message();
        messageToAll.setMessageType(MessageType.REFRESH);
        messageToAll.setOldMapId(oldMapId);
        messageToAll.setUser(user);
        messageToAll.setDescription(user.getUsername()+"去地图"+user.getMapId()+"了");
        sendMessageToAll(user.getAccount(),messageToAll);

        Message response = new Message();
        response.setMessageType(MessageType.SUCCESS);
        response.setMapInfoMap(dataManager.getMapInfoMap());
        response.setDescription("当前角色地图为：地图"+mapId);
        response.setUser(user);
        ctx.writeAndFlush(response);
    }

    public void sendMessageToAll(String account,Message message){
        dataManager.getConcurrentMap().forEach((k, v) -> {
            if(!account.equals(k)){
                v.writeAndFlush(message);
            }
        });
    }
}
