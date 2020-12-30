package com.zkw.springboot.bean;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author zhangkewei
 * @date 2020/12/24 12:39
 * @desc 装备静态资源
 */
@Data
public class EquipmentExcel {
    /**
     * 装备ID
     */
    @ExcelProperty("装备ID")
    private int id;

    /**
     * 装备伤害
     */
    @ExcelProperty("伤害数值")
    private int damage;

    /**
     * 装备名称
     */
    @ExcelProperty("装备名称")
    private String name;
}
