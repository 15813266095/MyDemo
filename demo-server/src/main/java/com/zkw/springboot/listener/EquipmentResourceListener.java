package com.zkw.springboot.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.zkw.springboot.bean.EquipmentExcel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangkewei
 * @date 2020/12/24 14:49
 * @desc 装备信息的读取监听器
 */
public class EquipmentResourceListener extends AnalysisEventListener<EquipmentExcel> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EquipmentResourceListener.class);

    private List<EquipmentExcel> list = new ArrayList();

    @Override
    public void invoke(EquipmentExcel equipmentExcel, AnalysisContext analysisContext) {
        list.add(equipmentExcel);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        LOGGER.info("装备数据解析完成！");
    }

    public List<EquipmentExcel> getList(){
        return list;
    }
}
