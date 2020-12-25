package com.zkw.springboot.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.zkw.springboot.resource.EquipmentResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangkewei
 * @date 2020/12/24 14:49
 * @desc
 */
public class EquipmentResourceListener extends AnalysisEventListener<EquipmentResource> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EquipmentResourceListener.class);

    private List<EquipmentResource> list = new ArrayList();

    @Override
    public void invoke(EquipmentResource equipmentResource, AnalysisContext analysisContext) {
        list.add(equipmentResource);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        LOGGER.info("装备数据解析完成！");
    }

    public List<EquipmentResource> getList(){
        return list;
    }
}
