package com.stehno.sjdbcx.annotation;

import java.lang.annotation.*;

/**
 * Annotation used to denote a repository interface or abstract class as one to be processed by the SJDBCX
 * system.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JdbcRepository {

    /**
     * The resource used to lookup the Sql properties file. If no value is specified the annotated class name will be
     * used based on the root classpath.
     *
     * @return the resource path (Spring Resource) for the SQL properties file.
     */
    String value() default "";

    /**
     * The default setting to use for the SQL annotation "resolve" property. The SQL annotation may still explicitly
     * override the value specified here.
     *
     * The DEFAULT option is somewhat redundant here; however, it will be treated as LOOKUP, which is the overall default.
     *
     * @return the default value to be used as the "resolve" property of Sql annotations
     */
    ResolveMethod defaultResolve() default ResolveMethod.LOOKUP;
}
