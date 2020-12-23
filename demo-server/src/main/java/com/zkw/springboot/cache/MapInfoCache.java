package com.zkw.springboot.cache;

import com.alibaba.excel.EasyExcel;
import com.zkw.springboot.listener.MapInfoListener;
import com.zkw.springboot.bean.MapInfo;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangkewei
 * @date 2020/12/23 12:50
 * @desc 配置中读取的地图信息缓存
 */
@Component
public class MapInfoCache {
    /**
     * 地图信息
     */
    private ConcurrentHashMap<Integer, MapInfo> mapInfoMap;

    /**
     * 缓存地图信息
     * @return
     */
    public ConcurrentHashMap<Integer, MapInfo> getMapInfoMap() {
        return mapInfoMap;
    }

    /**
     * 在构造此类之后执行该方法，用easyexcel工具获取excel表中的地图数据
     */
    @PostConstruct
    public void init() {
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
