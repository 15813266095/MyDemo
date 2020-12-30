package com.zkw.springboot.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.zkw.springboot.bean.MapInfoExcel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangkewei
 * @date 2020/12/18 16:30
 * @desc 地图信息的读取监听器
 */
public class MapResourceListener extends AnalysisEventListener<MapInfoExcel> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapResourceListener.class);

    private List<MapInfoExcel> list = new ArrayList<MapInfoExcel>();

    /**
     * 将读取到的列对象保存成一个List
     * @param mapInfoExcel
     * @param analysisContext
     */
    @Override
    public void invoke(MapInfoExcel mapInfoExcel, AnalysisContext analysisContext) {
        //LOGGER.info("解析到一条数据:{}", JSON.toJSONString(mapInfo));
        list.add(mapInfoExcel);
    }

    /**
     * 完成读取后的通知
     * @param analysisContext
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        LOGGER.info("地图数据解析完成！");
    }

    /**
     * 获取读取到的List
     * @return
     */
    public List<MapInfoExcel> getList(){
        return list;
    }
}
