package com.stehno.sjdbcx.annotation;

import java.lang.annotation.*;

/**
 *  ... only supports ResultSetExtractor<? extends List>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResultSetExtractor {

    String value();
}
