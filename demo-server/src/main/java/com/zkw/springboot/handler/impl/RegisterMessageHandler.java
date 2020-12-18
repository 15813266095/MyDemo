package com.zkw.springboot.handler.impl;

import com.zkw.springboot.bean.User;
import com.zkw.springboot.dao.UserMapper;
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
 * @desc 用于处理注册请求
 */
@Component
public class RegisterMessageHandler implements IMessageHandler {
    @Autowired
    UserMapper userMapper;

    @Autowired
    DataManager dataManager;

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
            dataManager.getMapInfoMap().get(request.getUser().getMapId()).addUser(request.getUser());
            dataManager.getConnectedUser().put(user.getAccount(),user);
            dataManager.getConcurrentMap().put(user.getAccount(), ctx.channel());

            Message messageToAll = new Message();
            messageToAll.setMessageType(MessageType.REFRESH);
            messageToAll.setUser(user);
            messageToAll.setDescription(user.getUsername()+"上线了");
            sendMessageToAll(user.getAccount(), messageToAll);

            response.setMapInfoMap(dataManager.getMapInfoMap());
            response.setMessageType(MessageType.SUCCESS);
            response.setUser(request.getUser());
            response.setDescription("注册成功！自动登录");
            ctx.writeAndFlush(response);
        }
    }

    public void sendMessageToAll(String account,Message message){
        dataManager.getConcurrentMap().forEach((k, v) -> {
            if(!account.equals(k)){
                v.writeAndFlush(message);
            }
        });
    }
}
