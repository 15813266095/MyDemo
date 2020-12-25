package com.zkw.springboot.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhangkewei
 * @date 2020/12/24 12:37
 * @desc 装备类
 */
@Data
public class Equipment implements Serializable {
    private int equipmentId;
    private int damage;
    private String name;
    private String userId;

    public Equipment(int equipmentId, int damage, String name, String userId) {
        this.equipmentId = equipmentId;
        this.damage = damage;
        this.name = name;
        this.userId = userId;
    }

    public Equipment(){

    }
}
