package com.zkw.springboot.bean;

import java.io.Serializable;

public class Map implements Serializable {
    private Integer id;

    private Integer positionX;

    private Integer positionY;

    private int[][] path = new int[20][20];

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