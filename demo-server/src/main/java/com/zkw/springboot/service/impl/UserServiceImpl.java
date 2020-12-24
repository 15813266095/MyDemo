package com.zkw.springboot.service.impl;

import com.zkw.springboot.bean.MapInfo;
import com.zkw.springboot.bean.User;
import com.zkw.springboot.cache.MapInfoCache;
import com.zkw.springboot.dao.UserMapper;
import com.zkw.springboot.cache.UserCache;
import com.zkw.springboot.protocol.Message;
import com.zkw.springboot.protocol.MessageType;
import com.zkw.springboot.service.BroadcastService;
import com.zkw.springboot.service.UserService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhangkewei
 * @date 2020/12/23 11:40
 * @desc 用户服务
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserCache userCache;
    @Autowired
    private MapInfoCache mapInfoCache;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BroadcastService broadcastService;

    @Override
    public void login(ChannelHandlerContext ctx, Message request) {
        User user = request.getUser();
        User user1 = userMapper.selectByPrimaryKey(user.getAccount());
        Message response = new Message();

        /**
         * 用户已经登录过的处理
         */
        if(userCache.getConnectedUserMap().containsKey(user.getAccount())){
            User connectUser = userCache.getConnectedUserMap().get(user.getAccount());
            mapInfoCache.getMapInfoMap().get(connectUser.getMapId()).exitUser(connectUser);
            userCache.getConnectedUserMap().remove(connectUser.getAccount());
            Channel channel = userCache.getUserChannelMap().remove(user.getAccount());

            Message response1 = new Message();
            response1.setUser(connectUser);
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
            messageToAll.setUser(user1);
            messageToAll.setDescription(user1.getUsername()+"下线了");
            broadcastService.sendMessageToAll(user.getAccount(), messageToAll);

            response.setMessageType(MessageType.ERROR);
            response.setDescription("该账号已经在线，请重新登录");
        }

        /**
         * 用户登录的处理
         */
        else if(user1!=null&&user!=null&&user.getPassword().equals(user1.getPassword())){
            mapInfoCache.getMapInfoMap().get(user1.getMapId()).enterUser(user1);
            userCache.getConnectedUserMap().put(user1.getAccount(),user1);
            userCache.getUserChannelMap().put(user.getAccount(), ctx.channel());

            Message messageToAll = new Message();
            messageToAll.setMessageType(MessageType.REFRESH);
            messageToAll.setUser(user1);
            messageToAll.setDescription(user1.getUsername()+"上线了");
            broadcastService.sendMessageToAll(user.getAccount(), messageToAll);

            response.setMapInfoMap(mapInfoCache.getMapInfoMap());
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

    @Override
    public void disconnect(ChannelHandlerContext ctx, Message request) {
        User user = request.getUser();
        userMapper.updateByPrimaryKeySelective(user);
        mapInfoCache.getMapInfoMap().get(user.getMapId()).exitUser(user);//将角色从地图里删除
        userCache.getConnectedUserMap().remove(user.getAccount());

        Message messageToAll = new Message();
        messageToAll.setMessageType(MessageType.REFRESH);
        messageToAll.setUser(user);
        messageToAll.setDescription(user.getUsername()+"下线了");
        broadcastService.sendMessageToAll(user.getAccount(), messageToAll);

        userCache.getUserChannelMap().remove(user.getAccount());
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
        User user = request.getUser();
        Message response = new Message();
        boolean f = move(user,request.getDirection(), mapInfoCache.getMapInfoMap().get(user.getMapId()));
        if(f){
            response.setMessageType(MessageType.SUCCESS);
            response.setUser(user);
            response.setDescription("\n角色移动了，方向为 "+request.getDirection()+ " ，当前角色位置为："+user.getArea());
        }else{
            response.setMessageType(MessageType.ERROR);
            response.setUser(user);
            response.setDescription("有障碍物或在边界，无法移动");
        }
        ctx.writeAndFlush(response);
    }

    @Override
    public void register(ChannelHandlerContext ctx, Message request) {
        User user = userMapper.selectByPrimaryKey(request.getUser().getAccount());
        Message response = new Message();
        if(user!=null){
            response.setMessageType(MessageType.ERROR);
            response.setDescription("账号重复");
        }else{
            userMapper.insertSelective(request.getUser());
            mapInfoCache.getMapInfoMap().get(request.getUser().getMapId()).enterUser(request.getUser());
            userCache.getConnectedUserMap().put(user.getAccount(),user);
            userCache.getUserChannelMap().put(user.getAccount(), ctx.channel());

            Message messageToAll = new Message();
            messageToAll.setMessageType(MessageType.REFRESH);
            messageToAll.setUser(user);
            messageToAll.setDescription(user.getUsername()+"上线了");
            broadcastService.sendMessageToAll(user.getAccount(), messageToAll);

            response.setMapInfoMap(mapInfoCache.getMapInfoMap());
            response.setMessageType(MessageType.SUCCESS);
            response.setUser(request.getUser());
            response.setDescription("注册成功！自动登录");
        }
        ctx.writeAndFlush(response);
    }

    @Override
    public void get(ChannelHandlerContext ctx, Message request) {
        User user = userMapper.selectByPrimaryKey(request.getUser().getAccount());
        Message response = new Message();
        response.setMessageType(MessageType.SUCCESS);
        response.setUser(user);
        response.setDescription("\n角色名为：" +user.getUsername()+
                "\n当前角色位置为："+user.getArea()+
                "\n当前角色地图在：map"+user.getMapId());
        ctx.writeAndFlush(response);
    }

    public boolean move(User user,String direction, MapInfo mapInfo){
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
