package com.zkw.springboot.handler.impl;

import com.zkw.springboot.bean.User;
import com.zkw.springboot.dao.UserMapper;
import com.zkw.springboot.handler.DataManager;
import com.zkw.springboot.handler.IMessageHandler;
import com.zkw.springboot.protocol.Message;
import com.zkw.springboot.protocol.MessageType;
import io.netty.channel.Channel;
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

        /**
         * 用户已经登录过的处理
         */
        if(dataManager.getConnectedUser().containsKey(user.getAccount())){
            User connectUser = dataManager.getConnectedUser().get(user.getAccount());
            dataManager.getMapInfoMap().get(connectUser.getMapId()).removeUser(connectUser);
            dataManager.getConnectedUser().remove(connectUser.getAccount());

            Channel channel = dataManager.getConcurrentMap().remove(user.getAccount());
            Message response1 = new Message();
            response1.setUser(connectUser);
            response1.setMessageType(MessageType.DISCONNECT);
            response1.setDescription("账号已经在线，不能重复登录");
            try {
                channel.writeAndFlush(response1).sync();
                channel.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Message messageToAll = new Message();
            messageToAll.setMessageType(MessageType.REFRESH);
            messageToAll.setUser(user1);
            messageToAll.setDescription(user1.getUsername()+"下线了");
            sendMessageToAll(user.getAccount(), messageToAll);

            response.setMessageType(MessageType.ERROR);
            response.setDescription("该账号已经在线，请重新登录");
        }
        /**
         * 用户登录的处理
         */
        else if(user1!=null&&user!=null&&user.getPassword().equals(user1.getPassword())){
            dataManager.getMapInfoMap().get(user1.getMapId()).addUser(user1);
            dataManager.getConnectedUser().put(user1.getAccount(),user1);
            dataManager.getConcurrentMap().put(user.getAccount(), ctx.channel());

            Message messageToAll = new Message();
            messageToAll.setMessageType(MessageType.REFRESH);
            messageToAll.setUser(user1);
            messageToAll.setDescription(user1.getUsername()+"上线了");
            sendMessageToAll(user.getAccount(), messageToAll);

            response.setMapInfoMap(dataManager.getMapInfoMap());
            response.setUser(user1);
            response.setMessageType(MessageType.SUCCESS);
            response.setDescription("登录成功!");
        }
        /**
         * 登录出错
         */
        else{
            response.setMessageType(MessageType.ERROR);
            response.setDescription("登陆失败,密码错误或账号不存在");
        }
        ctx.writeAndFlush(response);
    }

    /**
     * 将消息发送给所有连接的角色
     * @param message
     */
    public void sendMessageToAll(String account,Message message){
        dataManager.getConcurrentMap().forEach((k, v) -> {
            if(!account.equals(k)){
                v.writeAndFlush(message);
            }
        });
    }
}
