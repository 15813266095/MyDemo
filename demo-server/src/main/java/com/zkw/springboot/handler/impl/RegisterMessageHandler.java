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
public class RegisterMessageHandler implements IMessageHandler {
    @Autowired
    UserMapper userMapper;

    @Autowired
    MapMapper mapMapper;

    @Override
    public MessageType getMessageType() {
        return MessageType.REGISTER;
    }

    @Override
    public void operate(ChannelHandlerContext ctx, Message request) {
        User user = userMapper.selectByPrimaryKey(request.getUser().getAccount());
        Message response = new Message();
        if(user!=null){
            response.setMessageType(MessageType.ERROR);
            response.setDescription("账号重复");
            ctx.writeAndFlush(response);
        }else{
            userMapper.insertSelective(request.getUser());
            List<Map> maps = mapMapper.findAll();
            response.setMapList(maps);
            response.setMessageType(MessageType.SUCCESS);
            response.setUser(request.getUser());
            response.setDescription("注册成功！自动登录");
            ctx.writeAndFlush(response);
        }
    }
}
