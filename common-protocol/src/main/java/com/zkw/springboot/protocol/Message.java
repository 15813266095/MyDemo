package com.zkw.springboot.protocol;

import com.zkw.springboot.bean.User;
import lombok.Data;

import java.io.Serializable;

@Data
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    MessageType messageType;
    User user;
    private String direction;
    private String description;


}
