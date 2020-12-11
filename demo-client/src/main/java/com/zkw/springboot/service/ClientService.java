package com.zkw.springboot.service;

import com.zkw.springboot.bean.User;
import com.zkw.springboot.netty.Client;
import com.zkw.springboot.protocol.Message;
import com.zkw.springboot.protocol.MessageType;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService {
    @Autowired
    private Client client;
    private Channel channel;
    private Message response;

    public Message getResponse() {
        return response;
    }

    public void setResponse(Message response) {
        this.response = response;
    }

    public void start(String localhost, int port) {
        ChannelFuture channelFuture = client.start(localhost, port);
        this.channel=channelFuture.channel();
    }

    public boolean isActive(){
        return channel!=null;
    }

    public Message login(String account, String password) {
        Message request = null;
        if(isActive()){
            User user = new User();
            user.setAccount(account);
            user.setPassword(password);
            user.setUsername("zhangsan");
            user.setPosition_X(0);
            user.setPosition_Y(0);
            user.setScenes("地图一");

            request = new Message();
            request.setMessageType(MessageType.LOGIN);
            request.setUser(user);

            channel.writeAndFlush(request);
        }
        return request;
    }

    public Message move(String direction,User user){
        if(isActive()){
            Message request = new Message();
            request.setDirection(direction);
            request.setMessageType(MessageType.MOVE);
            request.setUser(user);
            try {
                channel.writeAndFlush(request).sync();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return response;
    }
}
