package com.zkw.springboot.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.zkw.springboot.bean.MapInfo;
import com.zkw.springboot.bean.User;
import com.zkw.springboot.cache.UseCache;
import com.zkw.springboot.dao.EquipmentMapper;
import com.zkw.springboot.dao.UserMapper;
import com.zkw.springboot.protocol.Message;
import com.zkw.springboot.protocol.MessageType;
import com.zkw.springboot.resource.EquipmentResource;
import com.zkw.springboot.service.BroadcastService;
import com.zkw.springboot.service.UserService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author zhangkewei
 * @date 2020/12/23 11:40
 * @desc 用户服务
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UseCache useCache;
    @Autowired
    private Cache<Integer, MapInfo> caffeineCache;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BroadcastService broadcastService;
    @Autowired
    private EquipmentResource equipmentResource;
    @Autowired
    private EquipmentMapper equipmentMapper;

    @Override
    public void login(ChannelHandlerContext ctx, Message request) {
        User user = (User) request.map.get("user");
        User user1 = userMapper.selectByPrimaryKey(user.getAccount());
        Message response = new Message();
        ConcurrentMap<Integer,MapInfo> mapInfoMap = caffeineCache.asMap();
        /**
         * 用户已经登录过的处理
         */
        if(useCache.getConnectedUserMap().containsKey(user.getAccount())){
            User connectUser = useCache.getConnectedUserMap().get(user.getAccount());
            mapInfoMap.get(connectUser.getMapId()).exitUser(connectUser);
            useCache.getConnectedUserMap().remove(connectUser.getAccount());
            Channel channel = useCache.getUserChannelMap().remove(user.getAccount());

            Message response1 = new Message();
            response1.map.put("user",connectUser);
            response1.setMessageType(MessageType.DISCONNECT);
            response1.setDescription("其他人尝试登录你的账号，请重新登录");
            try {
                channel.writeAndFlush(response1).sync();
                channel.close().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Message messageToAll = new Message();
            messageToAll.setMessageType(MessageType.REFRESH);
            messageToAll.map.put("user",user1);
            messageToAll.setDescription(user1.getUsername()+"异常下线了");
            broadcastService.sendMessageToAll(user.getAccount(), messageToAll);

            response.setMessageType(MessageType.ERROR);
            response.setDescription("该账号已经在线，请重新登录");
        }

        /**
         * 用户登录的处理
         */
        else if(user1!=null&&user.getPassword().equals(user1.getPassword())){
            mapInfoMap.get(user1.getMapId()).enterUser(user1);
            useCache.getConnectedUserMap().put(user1.getAccount(),user1);
            useCache.getUserChannelMap().put(user.getAccount(), ctx.channel());

            Message messageToAll = new Message();
            messageToAll.setMessageType(MessageType.REFRESH);
            messageToAll.map.put("user",user1);
            messageToAll.setDescription(user1.getUsername()+"上线了");
            broadcastService.sendMessageToAll(user.getAccount(), messageToAll);

            Integer equipmentId = equipmentMapper.findEquipmentByUserId(user1.getAccount());
            if(equipmentId!=null){
                user1.setEquipmentName(equipmentResource.getEquipmentMap().get(equipmentId).getName());
            }else {
                user1.setEquipmentName("无");
            }
            ConcurrentMap<Integer, MapInfo> tempMap = new ConcurrentHashMap<>();
            tempMap.putAll(mapInfoMap);
            response.map.put("mapInfoMap", tempMap);
            response.map.put("user",user1);
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

    @Override
    public void disconnect(ChannelHandlerContext ctx, Message request) {
        ConcurrentMap<Integer, MapInfo> mapInfoMap = caffeineCache.asMap();
        User user = (User) request.map.get("user");
        userMapper.updateByPrimaryKeySelective(user);
        mapInfoMap.get(user.getMapId()).exitUser(user);//将角色从地图里删除
        useCache.getConnectedUserMap().remove(user.getAccount());

        Message messageToAll = new Message();
        messageToAll.setMessageType(MessageType.REFRESH);
        messageToAll.map.put("user",user);
        messageToAll.setDescription(user.getUsername()+"下线了");
        broadcastService.sendMessageToAll(user.getAccount(), messageToAll);

        useCache.getUserChannelMap().remove(user.getAccount());
        log.info("玩家数据保存成功");
        log.info("客户端断开连接");
        try {
            ctx.channel().close().sync();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void move(ChannelHandlerContext ctx, Message request) {
        ConcurrentMap<Integer, MapInfo> mapInfoMap = caffeineCache.asMap();
        User user = (User) request.map.get("user");
        Message response = new Message();
        boolean f = move(user,(String) request.map.get("direction"), mapInfoMap.get(user.getMapId()));
        if(f){
            response.setMessageType(MessageType.SUCCESS);
            response.map.put("user",user);
            response.setDescription("\n角色移动了，方向为 " + request.map.get("direction") + " ，当前角色位置为："+user.getArea());
        }else{
            response.setMessageType(MessageType.ERROR);
            response.map.put("user",user);
            response.setDescription("有障碍物或在边界，无法移动");
        }
        ctx.writeAndFlush(response);
    }

    @Override
    public void register(ChannelHandlerContext ctx, Message request) {
        ConcurrentMap<Integer, MapInfo> mapInfoMap = caffeineCache.asMap();
        User user = (User) request.map.get("user");
        Message response = new Message();
        if(userMapper.selectByPrimaryKey(user.getAccount())!=null){
            response.setMessageType(MessageType.ERROR);
            response.setDescription("账号重复");
        }else{
            mapInfoMap.get(user.getMapId()).enterUser(user);
            useCache.getConnectedUserMap().put(user.getAccount(),user);
            useCache.getUserChannelMap().put(user.getAccount(), ctx.channel());

            Message messageToAll = new Message();
            messageToAll.setMessageType(MessageType.REFRESH);
            messageToAll.map.put("user",user);
            messageToAll.setDescription(user.getUsername()+"上线了");
            broadcastService.sendMessageToAll(user.getAccount(), messageToAll);

            ConcurrentMap<Integer, MapInfo> tempMap = new ConcurrentHashMap<>();
            tempMap.putAll(mapInfoMap);
            response.map.put("mapInfoMap", tempMap);
            response.setMessageType(MessageType.SUCCESS);
            response.map.put("user",user);
            response.setDescription("注册成功！自动登录");

            userMapper.insertSelective(user);
        }
        ctx.writeAndFlush(response);
    }

    @Override
    public void get(ChannelHandlerContext ctx, Message request) {
        User user = userMapper.selectByPrimaryKey(((User)request.map.get("user")).getAccount());
        Message response = new Message();
        response.setMessageType(MessageType.SUCCESS);
        response.map.put("user",user);
        response.setDescription("\n角色名为：" +user.getUsername()+
                "\n当前角色位置为："+user.getArea()+
                "\n当前角色地图在：map"+user.getMapId());
        ctx.writeAndFlush(response);
    }

    private boolean move(User user,String direction, MapInfo mapInfo){
        int[][] path = mapInfo.getPaths();
        Integer positionY = user.getPositionY();
        Integer positionX = user.getPositionX();
        switch (direction){
            case "forward":
                if(positionY+1>=path.length||path[positionY+1][positionX]==1){
                    return false;
                }
                user.setPositionY(positionY+1);
                break;
            case "backward":
                if(positionY-1<0||path[positionY-1][positionX]==1){
                    return false;
                }
                user.setPositionY(positionY-1);
                break;
            case "right":
                if(positionX+1>=path[0].length||path[positionY][positionX+1]==1){
                    return false;
                }
                user.setPositionX(positionX+1);
                break;
            case "left":
                if(positionX-1<0||path[positionY][positionX-1]==1){
                    return false;
                }
                user.setPositionX(positionX-1);
        }
        return true;
    }
}
