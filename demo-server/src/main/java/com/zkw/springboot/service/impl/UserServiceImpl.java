package com.zkw.springboot.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.zkw.springboot.bean.Equipment;
import com.zkw.springboot.bean.MapInfo;
import com.zkw.springboot.bean.User;
import com.zkw.springboot.dao.EquipmentMapper;
import com.zkw.springboot.dao.UserMapper;
import com.zkw.springboot.protocol.Message;
import com.zkw.springboot.protocol.MessageType;
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
    private Cache<Integer, MapInfo> mapInfoCache;
    @Autowired
    private Cache<String, User> connectedUserCache;
    @Autowired
    private Cache<String, Channel> userChannelCache;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BroadcastService broadcastService;
    @Autowired
    private Cache<Integer, Equipment> equipmentCache;
    @Autowired
    private EquipmentMapper equipmentMapper;

    /**
     * 用户登录的逻辑实现
     * @param ctx
     * @param request
     */
    @Override
    public void login(ChannelHandlerContext ctx, Message request) {
        User user = (User) request.map.get("user");
        User user1 = userMapper.selectByPrimaryKey(user.getAccount());
        Message response = new Message();
        ConcurrentMap<Integer, MapInfo> mapInfoMap = mapInfoCache.asMap();
        ConcurrentMap<String, User> connectedUserMap = connectedUserCache.asMap();
        ConcurrentMap<String, Channel> userChannelMap = userChannelCache.asMap();
        /**
         * 用户已经登录过的处理
         */
        if(connectedUserMap.containsKey(user.getAccount())){
            User connectUser = connectedUserMap.get(user.getAccount());
            mapInfoMap.get(connectUser.getMapId()).exitUser(connectUser);
            connectedUserMap.remove(connectUser.getAccount());
            Channel channel = userChannelMap.remove(user.getAccount());

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
            connectedUserMap.put(user1.getAccount(),user1);
            userChannelMap.put(user.getAccount(), ctx.channel());

            Message messageToAll = new Message();
            messageToAll.setMessageType(MessageType.REFRESH);
            messageToAll.map.put("user",user1);
            messageToAll.setDescription(user1.getUsername()+"上线了");
            broadcastService.sendMessageToAll(user.getAccount(), messageToAll);

            Integer equipmentId = equipmentMapper.findEquipmentByUserId(user1.getAccount());
            if(equipmentId!=null){
                user1.setEquipmentName(equipmentCache.asMap().get(equipmentId).getName());
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

    /**
     * 用户断开连接的逻辑实现
     * @param ctx
     * @param request
     */
    @Override
    public void disconnect(ChannelHandlerContext ctx, Message request) {
        ConcurrentMap<Integer, MapInfo> mapInfoMap = mapInfoCache.asMap();
        ConcurrentMap<String, User> connectedUserMap = connectedUserCache.asMap();
        ConcurrentMap<String, Channel> userChannelMap = userChannelCache.asMap();

        User user = (User) request.map.get("user");
        userMapper.updateByPrimaryKeySelective(user);
        mapInfoMap.get(user.getMapId()).exitUser(user);//将角色从地图里删除
        connectedUserMap.remove(user.getAccount());

        Message messageToAll = new Message();
        messageToAll.setMessageType(MessageType.REFRESH);
        messageToAll.map.put("user",user);
        messageToAll.setDescription(user.getUsername()+"下线了");
        broadcastService.sendMessageToAll(user.getAccount(), messageToAll);

        userChannelMap.remove(user.getAccount());
        log.info("玩家数据保存成功");
        log.info("客户端断开连接");
        try {
            ctx.channel().close().sync();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 用户移动的逻辑实现
     * @param ctx
     * @param request
     */
    @Override
    public void move(ChannelHandlerContext ctx, Message request) {
        ConcurrentMap<Integer, MapInfo> mapInfoMap = mapInfoCache.asMap();
        ConcurrentMap<String, User> connectedUserMap = connectedUserCache.asMap();
        User user = (User) request.map.get("user");
        Message response = new Message();
        boolean f = move(user,(String) request.map.get("direction"), mapInfoMap.get(user.getMapId()));
        connectedUserMap.put(user.getAccount(),user);
        if(f){
            response.setMessageType(MessageType.SUCCESS);
            response.map.put("user",user);
            response.setDescription("\n角色移动了，方向为 " + request.map.get("direction") + "<br>当前角色位置为："+user.getArea());
        }else{
            response.setMessageType(MessageType.ERROR);
            response.map.put("user",user);
            response.setDescription("有障碍物或在边界，无法移动");
        }
        ctx.writeAndFlush(response);
    }

    /**
     * 用户注册的逻辑实现
     * @param ctx
     * @param request
     */
    @Override
    public void register(ChannelHandlerContext ctx, Message request) {
        ConcurrentMap<Integer, MapInfo> mapInfoMap = mapInfoCache.asMap();
        ConcurrentMap<String, User> connectedUserMap = connectedUserCache.asMap();
        ConcurrentMap<String, Channel> userChannelMap = userChannelCache.asMap();
        User user = (User) request.map.get("user");
        Message response = new Message();
        if(userMapper.selectByPrimaryKey(user.getAccount())!=null){
            response.setMessageType(MessageType.ERROR);
            response.setDescription("账号重复");
        }else{
            mapInfoMap.get(user.getMapId()).enterUser(user);
            connectedUserMap.put(user.getAccount(),user);
            userChannelMap.put(user.getAccount(), ctx.channel());

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

    /**
     * 获取用户信息的逻辑实现
     * @param ctx
     * @param request
     */
    @Override
    public void get(ChannelHandlerContext ctx, Message request) {
        ConcurrentMap<String, User> connectedUserMap = connectedUserCache.asMap();
        ConcurrentMap<Integer, MapInfo> mapInfoMap = mapInfoCache.asMap();
        String account = ((User) request.map.get("user")).getAccount();
        User user;
        if(connectedUserMap.containsKey(account)){
            user = connectedUserMap.get(account);
        }else {
            user = userMapper.selectByPrimaryKey(account);
        }
        Message response = new Message();
        response.setMessageType(MessageType.SUCCESS);
        response.map.put("user",user);
        ConcurrentMap<Integer, MapInfo> tempMap = new ConcurrentHashMap<>();
        tempMap.putAll(mapInfoMap);
        response.map.put("mapInfoMap", tempMap);
        response.setDescription("角色账号为：" +user.getAccount()+
                "<br>"+user.getArea()+
                "<br>当前角色地图在：地图"+user.getMapId());
        ctx.writeAndFlush(response);
    }

    /**
     * 用户移动的逻辑实现
     * @param user
     * @param direction
     * @param mapInfo
     * @return
     */
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
