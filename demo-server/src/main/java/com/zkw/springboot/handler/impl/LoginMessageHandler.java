package com.zkw.springboot.handler.impl;

import com.zkw.springboot.bean.Map;
import com.zkw.springboot.bean.User;
import com.zkw.springboot.dao.MapMapper;
import com.zkw.springboot.dao.UserMapper;
import com.zkw.springboot.handler.IMessageHandler;
import com.zkw.springboot.protocol.Message;
import com.zkw.springboot.protocol.MessageType;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginMessageHandler implements IMessageHandler {

    @Autowired
    UserMapper userMapper;
    @Autowired
    MapMapper mapMapper;

    @Override
    public MessageType getMessageType() {
        return MessageType.LOGIN;
    }

    @Override
    public void operate(ChannelHandlerContext ctx, Message request) {
        User user = request.getUser();
        User user1 = userMapper.selectByPrimaryKey(user.getAccount());
        Message response = new Message();
        if(user1!=null&&user!=null&&user.getPassword().equals(user1.getPassword())){
            List<Map> maps = mapMapper.findAll();
            response.setMapList(maps);
            response.setUser(user1);
            response.setMessageType(MessageType.SUCCESS);
            response.setDescription("登录成功!");

        }else{
            response.setMessageType(MessageType.ERROR);
            response.setDescription("密码错误，登陆失败");
        }
        ctx.writeAndFlush(response);
    }
}
