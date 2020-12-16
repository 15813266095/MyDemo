package com.zkw.springboot;

import com.zkw.springboot.bean.MapInfo;
import com.zkw.springboot.dao.MapMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class DemoServerApplicationTests {

    @Autowired
    MapMapper mapMapper;

    @Test
    void test() {
        MapInfo mapInfo = new MapInfo();
        mapInfo.setPositionX(0);
        mapInfo.setPositionY(0);
        mapMapper.insert(mapInfo);
    }

}
