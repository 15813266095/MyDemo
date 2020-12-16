package com.zkw.springboot.dao;


import com.zkw.springboot.bean.MapInfo;

import java.util.List;

public interface MapMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MapInfo record);

    int insertSelective(MapInfo record);

    MapInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MapInfo record);

    int updateByPrimaryKey(MapInfo record);

    List<MapInfo> findAll();
}