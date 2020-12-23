package com.zkw.springboot.annotation;

import com.zkw.springboot.protocol.MessageType;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 处理器注解，标注在方法上，可以利用反射识别
 */
@Retention(RUNTIME)
@Target({METHOD})
public @interface HandlerAnno {
    MessageType messageType();
}
