package com.zkw.springboot.dao;

import com.zkw.springboot.bean.Equipment;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;


@Mapper
public interface EquipmentMapper {
    /**
     * 从数据库查找到所有装备信息
     * @return
     */
    List<Equipment> findAll();

    /**
     * 根据用户id查找装备信息
     * @param userId
     * @return
     */
    Integer findEquipmentByUserId(String userId);
}
