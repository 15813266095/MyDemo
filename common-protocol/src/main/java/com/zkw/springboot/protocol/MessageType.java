package com.zkw.springboot.protocol;

import java.io.Serializable;

public enum MessageType implements Serializable {
    REGISTER,LOGIN,GET,MOVE,CHANGE_SCENES,SUCCESS,ERROR;
}
