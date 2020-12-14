package com.zkw.springboot.bean;

import java.io.Serializable;

public class User implements Serializable {
    private String account;

    private String password;

    private String username;

    private Integer positionX;

    private Integer positionY;

    private String scenes;

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

    public String getScenes() {
        return scenes;
    }

    public void setScenes(String scenes) {
        this.scenes = scenes == null ? null : scenes.trim();
    }

    public String getArea(){
        return "当前角色位置为：("+this.getPositionX()+","+this.getPositionY()+")";
    }

    public void move(String direction){
        switch (direction){
            case "right":
                positionX++;
                break;
            case "left":
                positionX--;
                break;
            case "forward":
                positionY++;
                break;
            case "backward":
                positionY--;

        }
    }
}