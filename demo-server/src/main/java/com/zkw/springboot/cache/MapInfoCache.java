package com.zkw.springboot.cache;

import com.zkw.springboot.annotation.ResourceAnno;
import com.zkw.springboot.bean.MapInfo;
import com.zkw.springboot.listener.MapResourceListener;
import com.zkw.springboot.resource.MapResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangkewei
 * @date 2020/12/23 12:50
 * @desc 在配置中读取的地图信息缓存
 */
@Component
public class MapInfoCache {

    /**
     * 地图配置的路径
     */
    @Value("${demoserver.resources.map}")
    private String fileName;

    /**
     * 地图信息
     */
    private ConcurrentHashMap<Integer, MapInfo> mapInfoMap;

    /**
     * 获取地图信息
     * @return
     */
    public ConcurrentHashMap<Integer, MapInfo> getMapInfoMap() {
        return mapInfoMap;
    }


    @ResourceAnno(bean = MapResource.class, listener = MapResourceListener.class)
    private void setMap(Object o){
        mapInfoMap = new ConcurrentHashMap<>();
        List<MapResource> mapResources=(List<MapResource>)o;
        for (MapResource mapResource : mapResources) {
            mapInfoMap.put(mapResource.getId(), new MapInfo(mapResource.getId(),mapResource.getPositionX(),mapResource.getPositionY(),mapResource.getJsonPath()));
        }
    }

//    /**
//     * 在构造此类之后执行该方法，用easyexcel工具获取excel表中的地图数据
//     */
//    @PostConstruct
//    public void init() {
//        mapInfoMap = new ConcurrentHashMap<>();
//        MapResourceListener mapResourceListener = new MapResourceListener();
//        EasyExcel.read(fileName, MapResource.class, mapResourceListener).sheet().doRead();
//        List<MapResource> mapResources = mapResourceListener.getList();
//        for (MapResource mapResource : mapResources) {
//            mapInfoMap.put(mapResource.getId(), new MapInfo(mapResource.getId(),mapResource.getPositionX(),mapResource.getPositionY(),mapResource.getJsonPath()));
//        }
//    }
}
