package com.stehno.sjdbcx.annotation

import java.lang.annotation.Documented
import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

/**
 *  Used to provide a customized mapping of the method input parameters to the SQL replacement variables.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ParamMapper {

    /**
     * Used to specify the bean name for the shared ParamMapper instance. The bean
     * resolved, must be or inherit from the class specified by the type() value.
     *
     * If left blank or omitted, a bean will be resolved by type.
     */
    String value() default ''

    Class<? extends com.stehno.sjdbcx.ParamMapper> type()
}