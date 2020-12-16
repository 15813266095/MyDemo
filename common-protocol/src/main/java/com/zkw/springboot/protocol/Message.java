package com.zkw.springboot.protocol;

import com.zkw.springboot.bean.MapInfo;
import com.zkw.springboot.bean.User;
import lombok.Data;
import java.io.Serializable;
import java.util.Map;

/**
 * @author zhangkewei
 * @date 2020/12/16 15:39
 * @desc 客户端和服务端交互的消息协议
 */
@Data
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    private MessageType messageType;
    private User user;
    private Map<Integer,MapInfo> mapInfoMap;
    private String direction;
    private String description;
    private Integer oldMapId;
}
