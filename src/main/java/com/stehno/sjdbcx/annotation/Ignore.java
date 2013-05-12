package com.stehno.sjdbcx.annotation;

import java.lang.annotation.*;

/**
 * Annotation used to flag a method parameter as one to be ignored by the ParamMapper.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Ignore { /* nothing special */ }
