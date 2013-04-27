package com.stehno.sjdbcx.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PreparedStatementCallback {

    /**
     *  Bean name of the instance to be used.
     *
     * @return
     */
    String value();
}
