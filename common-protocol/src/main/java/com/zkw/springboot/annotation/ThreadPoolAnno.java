package com.zkw.springboot.annotation;

import com.zkw.springboot.protocol.ExecutorType;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
public @interface ThreadPoolAnno {
    ExecutorType pooltype();
}
