package com.tlcsdm.core.annotation;

import java.lang.annotation.*;

/**
 * @author unknowIfGuestInDream
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface Order {
    int value() default 2147483647;
}
