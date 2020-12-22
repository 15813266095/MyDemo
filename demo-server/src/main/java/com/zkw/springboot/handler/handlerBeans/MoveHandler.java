package com.zkw.springboot.handler.handlerBeans;

import com.zkw.springboot.annotation.handler;
import com.zkw.springboot.bean.User;
import com.zkw.springboot.handler.DataManager;
import com.zkw.springboot.protocol.Message;
import com.zkw.springboot.protocol.MessageType;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhangkewei
 * @date 2020/12/22 11:54
 * @desc 用于处理移动请求
 */
@Component
public class MoveHandler {

    @Autowired
    private DataManager dataManager;

    @handler(messageType = MessageType.MOVE)
    public void moveHandler(ChannelHandlerContext ctx, Message request) {
        User user = request.getUser();
        Message response = new Message();
        boolean f = user.move(request.getDirection(), dataManager.getMapInfoMap().get(user.getMapId()));
        if(f){
            response.setMessageType(MessageType.SUCCESS);
            response.setUser(user);
            response.setDescription("\n角色移动了，方向为 "+request.getDirection()+ " ，当前角色位置为："+user.getArea());
        }else{
            response.setMessageType(MessageType.ERROR);
            response.setUser(user);
            response.setDescription("有障碍物或在边界，无法移动");
        }
        ctx.writeAndFlush(response);
    }
}
