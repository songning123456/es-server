package com.es.elasticsearch.annotation;

import java.lang.annotation.*;

/**
 * @author sonning
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface ControllerAspectAnnotation {
    String description() default "";
}
