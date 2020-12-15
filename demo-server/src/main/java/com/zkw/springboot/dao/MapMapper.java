package com.zkw.springboot.dao;

import com.zkw.springboot.bean.Map;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MapMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Map record);

    int insertSelective(Map record);

    Map selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Map record);

    int updateByPrimaryKey(Map record);

    @Select("SELECT * FROM t_map")
    public List<Map> findAll();
}