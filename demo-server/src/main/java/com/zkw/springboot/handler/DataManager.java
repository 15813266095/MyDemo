package com.zkw.springboot.handler;

import com.alibaba.excel.EasyExcel;
import com.zkw.springboot.MapInfoListener;
import com.zkw.springboot.bean.MapInfo;
import com.zkw.springboot.bean.User;
import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangkewei
 * @date 2020/12/16 15:31
 * @desc 管理在线角色和地图数据，包括角色的上线离线状态，地图数据的获取、更新
 */
@Component
public class DataManager {

    /**
     * 地图信息
     */
    private static ConcurrentHashMap<Integer, MapInfo> mapInfoMap;

    /**
     * 在线玩家信息
     */
    private static ConcurrentHashMap<String, User> connectedUser;

    /**
     * 目前已经连接的channel信息，k=角色账号，v=channnel
     */
    private static ConcurrentHashMap<String, Channel> concurrentMap;


    /**
     * 缓存地图信息
     * @return
     */
    public ConcurrentHashMap<Integer, MapInfo> getMapInfoMap() {
        return mapInfoMap;
    }

    /**
     * 缓存在线角色信息
     * @return
     */
    public ConcurrentHashMap<String, User> getConnectedUser() {
        return connectedUser;
    }

    /**
     * 缓存角色对应的channel信息
     * @return
     */
    public ConcurrentHashMap<String, Channel> getConcurrentMap() {
        return concurrentMap;
    }

    /**
     * 在构造时创建好信息对象
     */
    @PostConstruct
    public void init() {
        this.connectedUser = new ConcurrentHashMap<>();
        this.concurrentMap = new ConcurrentHashMap<>();
        mapInfoMap = new ConcurrentHashMap<>();
        String fileName = "demo-server/src/main/resources/maps/" + "地图信息.xlsx";
        MapInfoListener mapInfoListener = new MapInfoListener();
        EasyExcel.read(fileName, MapInfo.class, mapInfoListener).sheet().doRead();
        List<MapInfo> mapInfos = mapInfoListener.getMaps();
        for (MapInfo mapInfo : mapInfos) {
            mapInfoMap.put(mapInfo.getId(),mapInfo);
        }
    }
}
