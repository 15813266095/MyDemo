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

    /**
     * 将读取到的列对象保存成一个List
     * @param equipmentExcel
     * @param analysisContext
     */
    @Override
    public void invoke(EquipmentExcel equipmentExcel, AnalysisContext analysisContext) {
        list.add(equipmentExcel);
    }

    /**
     * 完成读取后的通知
     * @param analysisContext
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        LOGGER.info("装备数据解析完成！");
    }

    /**
     * 获取读取到的List
     * @return
     */
    public List<EquipmentExcel> getList(){
        return list;
    }
}
