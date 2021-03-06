package com.zkw.springboot.bean;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangkewei
 * @date 2020/12/16 15:31
 * @desc 地图类，包括地图id、出生地坐标XY、路径、地图里的用户信息
 */
@Data
public class MapInfo implements Serializable {
    /**
     * 地图id
     */
    private Integer id;

    /**
     * 地图出生点的X坐标
     */
    private Integer positionX;

    /**
     * 地图出生点的Y坐标
     */
    private Integer positionY;

    /**
     * 地图元素，1为障碍物
     */
    private int[][] paths;

    /**
     * 地图中的玩家
     */
    private Map<String,User> userMap=new ConcurrentHashMap<>();

    public MapInfo(){}

    public MapInfo(Integer id,Integer positionX,Integer positionY,String jsonPath){
        this.id = id;
        this.positionX = positionX;
        this.positionY = positionY;
        this.paths = JSON.parseObject(jsonPath, int[][].class);

    }

    /**
     * 玩家进入地图
     * @param user
     */
    public void enterUser(User user){
        userMap.put(user.getAccount(),user);
    }

    /**
     * 玩家退出地图
     * @param user
     * @return
     */
    public boolean exitUser(User user){
        return userMap.remove(user.getAccount())!=null;
    }

    /**
     * 玩家数
     * @return
     */
    public int getUserCount(){
        return userMap.size();
    }
}