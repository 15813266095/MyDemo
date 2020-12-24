package com.zkw.springboot.bean;

import lombok.Data;

/**
 * @author zhangkewei
 * @date 2020/12/24 12:37
 * @desc
 */
@Data
public class Equipment {
    private int id;
    private int damage;
    private String name;
    private int userId;

    public Equipment(int id, int damage, String name, int userId) {
        this.id = id;
        this.damage = damage;
        this.name = name;
        this.userId = userId;
    }
}
