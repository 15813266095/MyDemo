package com.zkw.springboot.service;

import com.zkw.springboot.bean.MapInfo;
import com.zkw.springboot.bean.User;
import com.zkw.springboot.protocol.Message;
import java.util.Map;

/**
 * @author zhangkewei
 * @date 2020/12/23 15:40
 * @desc 负责将网页端的请求发送给服务端，将服务端的响应渲染在网页端，同时也会渲染一些服务端主动发起的请求
 */
public interface ClientService {
    void start();
    Message login(String account, String password);
    Message register(User user);
    Message move(String direction, User user);
    Message changeMap(User user, Integer mapid);
    void disconnect(User user);
    void put(Message message) throws InterruptedException;
    void setMapInfoMap(Map<Integer, MapInfo> mapInfoMap);
    Map<Integer, MapInfo> getMapInfoMap();
}
