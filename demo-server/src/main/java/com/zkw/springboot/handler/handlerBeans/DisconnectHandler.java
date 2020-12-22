package com.zkw.springboot.handler.handlerBeans;

import com.zkw.springboot.annotation.handler;
import com.zkw.springboot.bean.User;
import com.zkw.springboot.dao.UserMapper;
import com.zkw.springboot.handler.DataManager;
import com.zkw.springboot.protocol.Message;
import com.zkw.springboot.protocol.MessageType;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhangkewei
 * @date 2020/12/22 11:56
 * @desc 用于处理断开连接请求
 */
@Slf4j
@Component
public class DisconnectHandler {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DataManager dataManager;
    @Autowired
    private BroadcastHandler broadcastHandler;

    @handler(messageType = MessageType.DISCONNECT)
    public void disconnectHandler(ChannelHandlerContext ctx, Message request) {
        User user = request.getUser();
        userMapper.updateByPrimaryKeySelective(user);
        dataManager.getMapInfoMap().get(user.getMapId()).removeUser(user);//将角色从地图里删除
        dataManager.getConnectedUser().remove(user.getAccount());

        Message messageToAll = new Message();
        messageToAll.setMessageType(MessageType.REFRESH);
        messageToAll.setUser(user);
        messageToAll.setDescription(user.getUsername()+"下线了");
        broadcastHandler.sendMessageToAll(user.getAccount(), messageToAll);

        dataManager.getConcurrentMap().remove(user.getAccount());
        log.info("玩家数据保存成功");
        log.info("客户端断开连接");
        try {
            ctx.channel().close().sync();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
