package com.zkw.springboot.bean;

import java.io.Serializable;

/**
 * @author zhangkewei
 * @date 2020/12/16 15:39
 * @desc 用户类，包括账号、密码、用户名、坐标XY、所在地图id
 */
public class User implements Serializable {
    private String account;

    private String password;

    private String username;

    private Integer positionX;

    private Integer positionY;

    private Integer mapId;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
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

    public Integer getMapId() {
        return mapId;
    }

    public void setMapId(Integer mapId) {
        this.mapId = mapId;
    }

    public String getArea(){
        return "当前角色坐标为("+positionX+","+positionY+")";
    }

    public boolean move(String direction, MapInfo mapInfo){
        int[][] path = mapInfo.getPath();
        switch (direction){
            case "forward":
                if(positionY+1>=path.length||path[positionY+1][positionX]==1){
                    return false;
                }
                positionY++;
                break;
            case "backward":
                if(positionY-1<0||path[positionY-1][positionX]==1){
                    return false;
                }
                positionY--;
                break;
            case "right":
                if(positionX+1>=path[0].length||path[positionY][positionX+1]==1){
                    return false;
                }
                positionX++;
                break;
            case "left":
                if(positionX-1<0||path[positionY][positionX-1]==1){
                    return false;
                }
                positionX--;
        }
        return true;
    }
}