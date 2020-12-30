package com.zkw.springboot.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 资源注解
 */
@Retention(RUNTIME)
@Target({METHOD})
public @interface ResourceAnno {
    Class<?> bean();
    Class<?> listener();
}
