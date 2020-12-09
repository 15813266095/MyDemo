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
}
