package com.zkw.springboot.resource;

import com.github.benmanes.caffeine.cache.Cache;
import com.zkw.springboot.annotation.ResourceAnno;
import com.zkw.springboot.bean.Equipment;
import com.zkw.springboot.bean.EquipmentExcel;
import com.zkw.springboot.dao.EquipmentMapper;
import com.zkw.springboot.listener.EquipmentResourceListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zhangkewei
 * @date 2020/12/24 14:59
 * @desc 在配置中读取的装备信息缓存
 */
@Component
public class EquipmentResource {

    /**
     * 地图配置的路径
     */
    @Value("${demoServer.resources.equipment}")
    private String fileName;

    /**
     * 装备信息
     */
    private List<EquipmentExcel> equipmentExcels;

    @Autowired
    private EquipmentMapper equipmentMapper;

    @Autowired
    private Cache<Integer, Equipment> equipmentCache;

    /**
     * 查找数据库中装备对应的角色，并构建equipmentMap
     * @param o
     */
    @ResourceAnno(bean = EquipmentExcel.class, listener = EquipmentResourceListener.class)
    private void setMap(Object o){
        this.equipmentExcels = (List<EquipmentExcel>)o;
        List<Equipment> equipments = equipmentMapper.findAll();
        for (int i = 0; i < equipmentExcels.size(); i++) {
            EquipmentExcel equipmentExcel = equipmentExcels.get(i);
            Equipment equipment = equipments.get(i);
            equipment.setName(equipmentExcel.getName());
            equipment.setDamage(equipmentExcel.getDamage());
            equipmentCache.asMap().put(equipmentExcel.getId(),equipment);
        }
    }

}
