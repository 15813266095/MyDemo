package com.zkw.springboot.handler.impl;

import com.zkw.springboot.bean.User;
import com.zkw.springboot.dao.UserMapper;
import com.zkw.springboot.handler.IMessageHandler;
import com.zkw.springboot.handler.DataManager;
import com.zkw.springboot.protocol.Message;
import com.zkw.springboot.protocol.MessageType;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhangkewei
 * @date 2020/12/16 15:31
 * @desc 用于处理登录请求
 */
@Component
public class LoginMessageHandler implements IMessageHandler {

    @Autowired
    UserMapper userMapper;
    @Autowired
    DataManager dataManager;

    @Override
    public MessageType getMessageType() {
        return MessageType.LOGIN;
    }

    @Override
    public void operate(ChannelHandlerContext ctx, Message request) {
        User user = request.getUser();
        User user1 = userMapper.selectByPrimaryKey(user.getAccount());
        Message response = new Message();
        if(dataManager.getConnectedUser().containsKey(user.getAccount())){
            response.setMessageType(MessageType.ERROR);
            response.setDescription("该账号已经在线");
        } else if(user1!=null&&user!=null&&user.getPassword().equals(user1.getPassword())){
            dataManager.getMapInfoMap().get(user1.getMapId()).addUser(user1);
            dataManager.getConnectedUser().put(user1.getAccount(),user1);
            response.setMapInfoMap(dataManager.getMapInfoMap());
            response.setUser(user1);
            response.setMessageType(MessageType.SUCCESS);
            response.setDescription("登录成功!");
        } else{
            response.setMessageType(MessageType.ERROR);
            response.setDescription("登陆失败,密码错误或账号不存在");
        }
        ctx.writeAndFlush(response);
    }
}
