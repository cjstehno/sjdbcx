package com.stehno.sjdbcx.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  Used to provide a customized mapping of the method input parameters to the SQL replacement variables.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ParamMapper {

    /**
     *  Bean name of the ParamMapper instance to be used.
     *
     * @return
     */
    String value();
}
