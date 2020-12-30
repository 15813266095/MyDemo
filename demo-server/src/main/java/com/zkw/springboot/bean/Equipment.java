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

    /**
     * 装备ID
     */
    private int equipmentId;

    /**
     * 装备伤害
     */
    private int damage;

    /**
     * 装备名称
     */
    private String name;

    /**
     * 所属用户的用户id
     */
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
