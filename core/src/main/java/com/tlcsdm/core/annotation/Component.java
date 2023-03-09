package com.tlcsdm.core.annotation;

import java.lang.annotation.*;

/**
 * @author unknowIfGuestInDream
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Component {
    String value() default "";
}
