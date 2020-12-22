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
    private Integer id;
    private Integer positionX;
    private Integer positionY;
    private int[][] path;
    private Map<String,User> users=new HashMap<>();
    private String jsonPath;

    public Map<String, User> getUsers() {
        return users;
    }

    public void addUser(User user){
        users.put(user.getAccount(),user);
    }

    public boolean containUser(User user){
        return users.containsKey(user.getAccount());
    }

    public boolean removeUser(User user){
        return users.remove(user.getAccount())!=null;
    }

    public int getUserCount(){
        return users.size();
    }

    public int[][] getPath() {
        return JSON.parseObject(jsonPath,int[][].class);
    }
}