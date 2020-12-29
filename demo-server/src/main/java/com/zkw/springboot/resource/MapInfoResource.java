package com.zkw.springboot.resource;

import com.github.benmanes.caffeine.cache.Cache;
import com.zkw.springboot.annotation.ResourceAnno;
import com.zkw.springboot.bean.MapInfo;
import com.zkw.springboot.bean.MapInfoExcel;
import com.zkw.springboot.listener.MapResourceListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zhangkewei
 * @date 2020/12/23 12:50
 * @desc 在配置中读取的地图信息缓存
 */
@Component
public class MapInfoResource {

    /**
     * 地图配置的路径
     */
    @Value("${demoServer.resources.map}")
    private String fileName;

    /**
     * 地图信息
     */
    private List<MapInfoExcel> mapInfoExcels;

    @Autowired
    private Cache<Integer, MapInfo> caffeineCache;


    @ResourceAnno(bean = MapInfoExcel.class, listener = MapResourceListener.class)
    private void setMap(Object o){
        mapInfoExcels = (List<MapInfoExcel>)o;
        for (MapInfoExcel mapInfoExcel : mapInfoExcels) {
            caffeineCache.asMap().put(mapInfoExcel.getId(), new MapInfo(mapInfoExcel.getId(), mapInfoExcel.getPositionX(), mapInfoExcel.getPositionY(), mapInfoExcel.getJsonPath()));
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
