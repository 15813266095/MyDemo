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
    /**
     * 消息类型
     */
    private MessageType messageType;

    /**
     * 消息携带的用户
     */
    private User user;

    /**
     * 消息携带的地图信息
     */
    private Map<Integer,MapInfo> mapInfoMap;

    /**
     * 角色移动时，移动消息携带的移动方向
     */
    private String direction;

    /**
     * 消息内容的描述
     */
    private String description;

    /**
     * 切换地图时，消息携带的旧地图id
     */
    private Integer oldMapId;
}
