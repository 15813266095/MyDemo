package com.zkw.springboot.bean;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

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
    private Map<String,User> usermap=new HashMap<>();

    /**
     * 地图元素的JSON格式
     */
    private String jsonPath;

    public Map<String, User> getUsers() {
        return usermap;
    }

    public void enterUser(User user){
        usermap.put(user.getAccount(),user);
    }

    public boolean containUser(User user){
        return usermap.containsKey(user.getAccount());
    }

    public boolean exitUser(User user){
        return usermap.remove(user.getAccount())!=null;
    }

    public int getUserCount(){
        return usermap.size();
    }

    public int[][] getPath() {
        return JSON.parseObject(jsonPath,int[][].class);
    }
}