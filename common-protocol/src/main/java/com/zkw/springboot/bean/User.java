package com.zkw.springboot.bean;

import java.io.Serializable;

/**
 * @author zhangkewei
 * @date 2020/12/16 15:39
 * @desc 用户类，包括账号、密码、用户名、坐标XY、所在地图id
 */
public class User implements Serializable {
    /**
     * 用户账号
     */
    private String account;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户所在X坐标
     */
    private Integer positionX;

    /**
     * 用户所在Y坐标
     */
    private Integer positionY;

    /**
     * 用户所在地图ID
     */
    private Integer mapId;

    /**
     * 角色的装备
     */
    private Equipment equipment;

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

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public String getArea(){
        return "当前角色坐标为("+positionX+","+positionY+")";
    }

}