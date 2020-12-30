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
    private Cache<Integer, MapInfo> mapInfoCache;

    /**
     * 反射调用，传入读取出来的资源
     * @param o
     */
    @ResourceAnno(bean = MapInfoExcel.class, listener = MapResourceListener.class)
    private void setMap(Object o){
        this.mapInfoExcels = (List<MapInfoExcel>)o;
        for (MapInfoExcel mapInfoExcel : mapInfoExcels) {
            mapInfoCache.asMap().put(mapInfoExcel.getId(), new MapInfo(mapInfoExcel.getId(), mapInfoExcel.getPositionX(), mapInfoExcel.getPositionY(), mapInfoExcel.getJsonPath()));
        }
    }
}
