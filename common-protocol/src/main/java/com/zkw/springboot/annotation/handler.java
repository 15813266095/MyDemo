package com.zkw.springboot.annotation;

import com.zkw.springboot.protocol.MessageType;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({METHOD})
public @interface handler {
    MessageType messageType();
}
