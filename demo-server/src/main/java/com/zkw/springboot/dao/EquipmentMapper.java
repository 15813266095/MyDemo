package com.zkw.springboot.dao;

import com.zkw.springboot.bean.Equipment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EquipmentMapper {
    List<Equipment> findAll();

    Integer findEquipmentByUserId(String userId);
}
