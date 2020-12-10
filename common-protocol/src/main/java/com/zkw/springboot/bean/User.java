package com.zkw.springboot.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {
    private String account;
    private String password;
    private String username;
    private int position_X;
    private int position_Y;
    private String Scenes;

    public String getArea(){
        return "当前角色位置为：("+this.getPosition_X()+","+this.getPosition_Y()+")";
    }

    public void move(String direction){
        switch (direction){
            case "right":
                position_X++;
                break;
            case "left":
                position_X--;
                break;
            case "forward":
                position_Y++;
                break;
            case "backward":
                position_Y--;

        }
    }
}
