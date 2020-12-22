package com.zkw.springboot.handler.handlerBeans;

import com.zkw.springboot.annotation.handler;
import com.zkw.springboot.bean.User;
import com.zkw.springboot.dao.UserMapper;
import com.zkw.springboot.protocol.Message;
import com.zkw.springboot.protocol.MessageType;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhangkewei
 * @date 2020/12/22 11:55
 * @desc 用于处理获取用户信息请求
 */
@Component
public class GetHandler {

    @Autowired
    private UserMapper userMapper;

    @handler(messageType = MessageType.GET)
    public void getHandler(ChannelHandlerContext ctx, Message request) {
        User user = userMapper.selectByPrimaryKey(request.getUser().getAccount());
        Message response = new Message();
        response.setMessageType(MessageType.SUCCESS);
        response.setUser(user);
        response.setDescription("\n角色名为：" +user.getUsername()+
                "\n当前角色位置为："+user.getArea()+
                "\n当前角色地图在：map"+user.getMapId());
        ctx.writeAndFlush(response);
    }
}
