package com.stehno.sjdbcx.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface JdbcDao {

    /**
     * The resource used to lookup the Sql properties file. If no value is specified the annotated class name will be
     * used based on the root classpath.
     *
     * @return
     */
    String value() default "";

    /**
     * The default setting to use for the SQL annotation "resolve" property. The SQL annotation may still explicitly
     * override the value specified here.
     *
     * The DEFAULT option is somewhat redundant here; however, it will be treated as LOOKUP, which is the overall default.
     *
     * @return
     */
    ResolveMethod defaultResolve() default ResolveMethod.LOOKUP;
}
