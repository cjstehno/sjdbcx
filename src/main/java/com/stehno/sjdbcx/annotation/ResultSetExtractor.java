package com.stehno.sjdbcx.annotation;

import java.lang.annotation.*;

/**
 *  ... type of the returned object should match that of the method return
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResultSetExtractor {

    String value() default "";
    Class<? extends org.springframework.jdbc.core.ResultSetExtractor> type();
}
