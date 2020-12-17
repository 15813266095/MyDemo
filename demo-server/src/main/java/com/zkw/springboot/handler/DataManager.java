package com.zkw.springboot.handler;

import com.zkw.springboot.bean.MapInfo;
import com.zkw.springboot.bean.User;
import com.zkw.springboot.dao.MapMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private MapMapper mapMapper;
    private static ConcurrentHashMap<Integer, MapInfo> mapInfoMap;
    private static ConcurrentHashMap<String, User> connectedUser;

    public static ConcurrentHashMap<Integer, MapInfo> getMapInfoMap() {
        return mapInfoMap;
    }

    public static ConcurrentHashMap<String, User> getConnectedUser() {
        return connectedUser;
    }

    /**
     * 在构造时创建好地图信息和玩家信息对象
     */
    @PostConstruct
    public void init() {
        this.mapInfoMap = new ConcurrentHashMap<>();
        List<MapInfo> mapInfos = mapMapper.findAll();
        for (MapInfo mapInfo : mapInfos) {
            mapInfoMap.put(mapInfo.getId(),mapInfo);
        }
        this.connectedUser = new ConcurrentHashMap<>();
    }
}
