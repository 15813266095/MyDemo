package com.zkw.springboot.service;

import com.zkw.springboot.bean.MapInfo;
import com.zkw.springboot.bean.User;
import com.zkw.springboot.protocol.Message;

import java.util.Map;

/**
 * @author zhangkewei
 * @date 2020/12/23 15:40
 * @desc 处理与网页端交互的service
 */
public interface ClientService {
    /**
     * 连接到服务器的方法
     */
    void start();

    /**
     * 登录请求
     * @param account
     * @param password
     * @return
     */
    Message login(String account, String password);

    /**
     * 注册请求
     * @param user
     * @return
     */
    Message register(User user);

    /**
     * 移动请求
     * @param direction
     * @param user
     * @return
     */
    Message move(String direction, User user);

    /**
     * 切换地图请求
     * @param user
     * @param mapid
     * @return
     */
    Message changeMap(User user, Integer mapid);

    /**
     * 断开连接请求
     * @param user
     */
    void disconnect(User user);

    /**
     * 用于将服务器的响应或请求放在service里的阻塞队列
     * @param message
     * @throws InterruptedException
     */
    void put(Message message) throws InterruptedException;

    /**
     * 设置地图信息
     * @param mapInfoMap
     */
    void setMapInfoMap(Map<Integer, MapInfo> mapInfoMap);

    /**
     * 获取地图信息
     * @return
     */
    Map<Integer, MapInfo> getMapInfoMap();
}
