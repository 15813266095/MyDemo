package com.zkw.springboot.handler;

import com.zkw.springboot.bean.MapInfo;
import com.zkw.springboot.bean.User;
import com.zkw.springboot.dao.MapMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhangkewei
 * @date 2020/12/16 15:31
 * @desc 管理在线角色和地图数据，包括角色的上线离线状态，地图数据的获取、更新
 */
@Component
public class DataManager {
    @Autowired
    private MapMapper mapMapper;
    private Map<Integer, MapInfo> mapInfoMap;
    private Map<String, User> connectedUser;

    public Map<Integer, MapInfo> getMapInfoMap() {
        return mapInfoMap;
    }

    public Map<String, User> getConnectedUser() {
        return connectedUser;
    }

    @PostConstruct
    public void init() {
        List<MapInfo> mapInfos = mapMapper.findAll();
        this.mapInfoMap = mapInfos.stream().collect(Collectors.toMap(mapInfo -> mapInfo.getId(), mapInfo -> mapInfo));
        this.connectedUser = new HashMap<>();
    }
}
