package com.zkw.springboot.service.impl;

import com.zkw.springboot.bean.MapInfo;
import com.zkw.springboot.bean.User;
import com.zkw.springboot.netty.Client;
import com.zkw.springboot.protocol.Message;
import com.zkw.springboot.protocol.MessageType;
import com.zkw.springboot.service.ClientService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 用于发送请求和接收请求的Service
 */

@Service
@Slf4j
public class ClientServiceImpl implements ClientService {
    @Autowired
    private Client client;

    @Value("${democlient.hostname}")
    private String localhost;
    @Value("${democlient.port}")
    private int port;

    private Channel channel;
    private BlockingQueue<Message> queue = new LinkedBlockingQueue<>(1);
    private Map<Integer,MapInfo> mapInfoMap;

    @Override
    public void setMapInfoMap(Map<Integer, MapInfo> mapInfoMap) {
        this.mapInfoMap = mapInfoMap;
    }

    @Override
    public Map<Integer, MapInfo> getMapInfoMap() {
        return mapInfoMap;
    }

    @Override
    public void put(Message message) throws InterruptedException {
        queue.put(message);
    }

    public void start() {
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
                response = queue.poll(10, TimeUnit.SECONDS);
                mapInfoMap =response.getMapInfoMap();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        return response;
    }

    public Message move(String direction, User user){
        if(isActive()){
            Message request = new Message();
            request.setDirection(direction);
            request.setMessageType(MessageType.MOVE);
            request.setUser(user);
            try {
                channel.writeAndFlush(request);
                Message response = queue.poll(3, TimeUnit.SECONDS);
                response.setMapInfoMap(mapInfoMap);
                return response;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        Message response = new Message();
        response.setMessageType(MessageType.ERROR);
        response.setMapInfoMap(mapInfoMap);
        return response;
    }

    public Message changeMap(User user, Integer mapid) {
        Integer oldMapId = user.getMapId();
        user.setMapId(mapid);
        if(isActive()){
            Message request = new Message();
            request.setMessageType(MessageType.CHANGEMAP);
            request.setUser(user);
            request.setOldMapId(oldMapId);
            try {
                channel.writeAndFlush(request);
                Message response = queue.poll(3, TimeUnit.SECONDS);
                if(response.getMessageType()==MessageType.ERROR){
                    response.setMapInfoMap(mapInfoMap);
                    return response;
                }
                mapInfoMap=response.getMapInfoMap();
                return response;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        Message response = new Message();
        response.setMessageType(MessageType.ERROR);
        response.setMapInfoMap(mapInfoMap);
        return response;
    }

    public void disconnect(User user) {
        if(isActive()){
            Message request = new Message();
            request.setUser(user);
            request.setMessageType(MessageType.DISCONNECT);
            try {
                channel.writeAndFlush(request).sync();
                channel.close();
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
            user.setMapId(1);
            user.setPositionX(0);
            user.setPositionY(0);
            request.setUser(user);
            request.setMessageType(MessageType.REGISTER);
            try {
                channel.writeAndFlush(request).sync();
                response = queue.poll(3, TimeUnit.SECONDS);
                mapInfoMap =response.getMapInfoMap();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return response;
    }
}
