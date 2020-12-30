package com.zkw.springboot.annotation;

import com.zkw.springboot.protocol.ExecutorType;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 标注线程池的注解
 */
@Retention(RUNTIME)
public @interface ThreadPoolAnno {
    ExecutorType executorType();
    String type() default "";
}
