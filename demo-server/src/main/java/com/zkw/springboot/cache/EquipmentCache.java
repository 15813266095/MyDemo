package com.zkw.springboot.cache;

import com.zkw.springboot.bean.Equipment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangkewei
 * @date 2020/12/24 14:59
 * @desc
 */
@Component
public class EquipmentCache {

    @Value("${demoserver.resources.equipment}")
    private String fileName;

    private ConcurrentHashMap<Integer, Equipment> equipmentMap;

    public ConcurrentHashMap<Integer, Equipment> getEquipmentMap() {
        return equipmentMap;
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
