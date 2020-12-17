package com.zkw.springboot.bean;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangkewei
 * @date 2020/12/16 15:31
 * @desc 地图类，包括地图id、出生地坐标XY、路径、地图里的用户信息
 */
@Slf4j
@ToString
public class MapInfo implements Serializable {
    private Integer id;

    private Integer positionX;

    private Integer positionY;

    private int[][] path = new int[20][20];

    private Map<String,User> users=new HashMap<>();

    public Map<String, User> getUsers() {
        return users;
    }

    public void addUser(User user){
        users.put(user.getAccount(),user);
    }

    public boolean removeUser(User user){
        return users.remove(user.getAccount())!=null;
    }

    public int getUserCount(){
        return users.size();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPositionX() {
        return positionX;
    }

    public void setPositionX(Integer positionX) {
        this.positionX = positionX;
    }

    public Integer getPositionY() {
        return positionY;
    }

    public void setPositionY(Integer positionY) {
        this.positionY = positionY;
    }

    public int[][] getPathNums(){
        return path;
    }

    public String getPath() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < path.length; i++) {
            for (int j = 0; j < path[0].length; j++) {
                s.append(path[i][j]+",");
            }
        }
        return s.toString();
    }

    public void setPath(String s1) {
        String[] s = s1.split(",");
        int index=0;
        for (int i = 0; i < this.path.length; i++) {
            for (int j = 0; j < this.path[0].length; j++) {
                path[i][j]=Integer.valueOf(s[index++]);
            }
        }
    }
}