package com.zkw.springboot.protocal;

import com.zkw.springboot.bean.User;
import lombok.Data;
import java.io.Serializable;

@Data
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean isActive;
    MessageType messageType;
    User user;

    public Message(){ }

    public Message(Boolean isActive){
        this.isActive=isActive;
    }
}
