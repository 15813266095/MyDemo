package com.zkw.springboot.service;

import com.zkw.springboot.bean.User;
import com.zkw.springboot.netty.Client;
import com.zkw.springboot.protocol.Message;
import com.zkw.springboot.protocol.MessageType;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
@Slf4j
public class ClientService {
    @Autowired
    private Client client;
    private Channel channel;
    private BlockingQueue<Message> queue = new LinkedBlockingQueue<>(1);

    public void put(Message message) throws InterruptedException {
        queue.put(message);
    }

    public void start(String localhost, int port) {
        ChannelFuture channelFuture = client.start(localhost, port);
        this.channel=channelFuture.channel();
    }

    public boolean isActive(){
        return channel!=null;
    }

    public Message login(String account, String password){
        Message response = null;
        if(isActive()){
            User user = new User();
            user.setAccount(account);
            user.setPassword(password);

            Message request = new Message();
            request.setMessageType(MessageType.LOGIN);
            request.setUser(user);

            channel.writeAndFlush(request);
            try {
                response = queue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        return response;
    }

    public Message move(String direction,User user){
        if(isActive()){
            Message request = new Message();
            request.setDirection(direction);
            request.setMessageType(MessageType.MOVE);
            request.setUser(user);
            try {
                channel.writeAndFlush(request);
                Message response = queue.take();
                return response;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        Message response = new Message();
        response.setMessageType(MessageType.ERROR);
        return response;
    }

    public Message changeScenes(User user) {
        if(isActive()){
            Message request = new Message();
            request.setMessageType(MessageType.CHANGE_SCENES);
            request.setUser(user);
            try {
                channel.writeAndFlush(request);
                Message response = queue.take();
                return response;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        Message response = new Message();
        response.setMessageType(MessageType.ERROR);
        return response;
    }

    public void disconnect(User user) {
        if(isActive()){
            Message request = new Message();
            request.setUser(user);
            request.setMessageType(MessageType.DISCONNECT);
            try {
                channel.writeAndFlush(request).sync();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        log.info("断开连接");
    }

    public Message register(User user) {
        Message response = null;
        if(isActive()){
            Message request = new Message();
            user.setScenes("map1");
            user.setPositionX(0);
            user.setPositionY(0);
            request.setUser(user);
            request.setMessageType(MessageType.REGISTER);
            try {
                channel.writeAndFlush(request).sync();
                response = queue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return response;
    }
}
