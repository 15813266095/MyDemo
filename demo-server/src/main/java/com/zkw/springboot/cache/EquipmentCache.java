package com.zkw.springboot.cache;

import com.zkw.springboot.annotation.ResourceAnno;
import com.zkw.springboot.bean.Equipment;
import com.zkw.springboot.dao.EquipmentMapper;
import com.zkw.springboot.listener.EquipmentResourceListener;
import com.zkw.springboot.resource.EquipmentResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangkewei
 * @date 2020/12/24 14:59
 * @desc 在配置中读取的装备信息缓存
 */
@Component
public class EquipmentCache {

    /**
     * 地图配置的路径
     */
    @Value("${demoserver.resources.equipment}")
    private String fileName;

    @Autowired
    private EquipmentMapper equipmentMapper;

    private ConcurrentHashMap<Integer, Equipment> equipmentMap;

    public ConcurrentHashMap<Integer, Equipment> getEquipmentMap() {
        return equipmentMap;
    }

    /**
     * 查找数据库中装备对应的角色，并构建equipmentMap
     * @param o
     */
    @ResourceAnno(bean = EquipmentResource.class, listener = EquipmentResourceListener.class)
    private void setMap(Object o){
        equipmentMap = new ConcurrentHashMap<>();
        List<EquipmentResource> equipmentResources = (List<EquipmentResource>)o;
        List<Equipment> equipments = equipmentMapper.findAll();
        for (int i = 0; i < equipmentResources.size(); i++) {
            EquipmentResource equipmentResource = equipmentResources.get(i);
            Equipment equipment = equipments.get(i);
            equipment.setName(equipmentResource.getName());
            equipment.setDamage(equipmentResource.getDamage());
            equipmentMap.put(equipmentResource.getId(),equipment);
        }
    }

//    @PostConstruct
//    public void init(){
//        equipmentMap = new ConcurrentHashMap<>();
//        EquipmentResourceListener equipmentResourceListener = new EquipmentResourceListener();
//        EasyExcel.read(fileName, EquipmentResource.class, equipmentResourceListener).sheet().doRead();
//        List<EquipmentResource> list = equipmentResourceListener.getList();
//        for (EquipmentResource equipmentResource : list) {
//            equipmentMap.put(equipmentResource.getId(),new Equipment(equipmentResource.getId(), equipmentResource.getDamage(), equipmentResource.getName(),123));
//        }
//    }
}
