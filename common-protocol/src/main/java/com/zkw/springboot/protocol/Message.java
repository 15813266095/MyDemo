package com.zkw.springboot.protocol;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
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
     * 消息内容的描述
     */
    private String description;

    /**
     * 消息携带的数据
     */
    public Map<String,Object> map = new HashMap<>();
}
