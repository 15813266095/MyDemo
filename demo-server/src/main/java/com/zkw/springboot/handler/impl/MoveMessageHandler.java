package com.zkw.springboot.handler.impl;

import com.zkw.springboot.bean.User;
import com.zkw.springboot.dao.MapMapper;
import com.zkw.springboot.handler.IMessageHandler;
import com.zkw.springboot.protocol.Message;
import com.zkw.springboot.protocol.MessageType;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 角色移动时执行的处理器，用来处理MessageType.MOVE请求
 */

@Service
public class MoveMessageHandler implements IMessageHandler {

    @Autowired
    MapMapper mapMapper;

    @Override
    public MessageType getMessageType() {
        return MessageType.MOVE;
    }

    @Override
    public void operate(ChannelHandlerContext ctx, Message request) {
        User user = request.getUser();
        Message response = new Message();
        boolean f = user.move(request.getDirection(), mapMapper.selectByPrimaryKey(user.getMapId()));
        if(f){
            response.setMessageType(MessageType.SUCCESS);
            response.setUser(user);
            response.setDescription("\n角色移动了，方向为"+request.getDirection()+ "，当前角色位置为："+user.getArea());
        }else{
            response.setMessageType(MessageType.ERROR);
            response.setUser(user);
            response.setDescription("有障碍物或在边界，无法移动");
        }
        ctx.writeAndFlush(response);
    }
}
