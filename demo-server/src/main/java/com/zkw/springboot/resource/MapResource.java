package com.zkw.springboot.resource;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author zhangkewei
 * @date 2020/12/24 10:30
 * @desc 地图静态资源
 */
@Data
public class MapResource {
    /**
     * 地图id
     */
    @ExcelProperty("地图ID")
    private Integer id;

    /**
     * 地图出生点的X坐标
     */
    @ExcelProperty("出生点坐标X")
    private Integer positionX;

    /**
     * 地图出生点的Y坐标
     */
    @ExcelProperty("出生点坐标Y")
    private Integer positionY;

    /**
     * 地图元素的JSON格式
     */
    @ExcelProperty("地图元素")
    private String jsonPath;
}
