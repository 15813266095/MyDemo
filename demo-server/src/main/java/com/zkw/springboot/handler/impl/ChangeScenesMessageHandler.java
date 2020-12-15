package com.zkw.springboot.handler.impl;

import com.zkw.springboot.bean.Map;
import com.zkw.springboot.bean.User;
import com.zkw.springboot.dao.MapMapper;
import com.zkw.springboot.handler.IMessageHandler;
import com.zkw.springboot.protocol.Message;
import com.zkw.springboot.protocol.MessageType;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 场景切换时用的处理器，用于处理MessageType.CHANGE_SCENES请求
 */

@Service
public class ChangeScenesMessageHandler implements IMessageHandler {

    @Autowired
    MapMapper mapMapper;

    @Override
    public MessageType getMessageType() {
        return MessageType.CHANGE_SCENES;
    }

    @Override
    public void operate(ChannelHandlerContext ctx, Message request) {
        User user = request.getUser();
        Integer mapId = user.getMapId();
        Map map = mapMapper.selectByPrimaryKey(mapId);
        user.setPositionX(map.getPositionX());
        user.setPositionY(map.getPositionY());
        Message response = new Message();
        response.setMessageType(MessageType.SUCCESS);
        response.setDescription("当前角色地图为：地图"+mapId);
        response.setUser(user);
        ctx.writeAndFlush(response);
    }
}
