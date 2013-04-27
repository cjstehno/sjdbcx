package com.stehno.sjdbcx.annotation;

/**
 * Enumerates the methods of SQL resolving for use in the annotations.
 */
public enum ResolveMethod {

    /**
     * use whatever the default is configured as (used if not specified)
     */
    DEFAULT,

    /**
     * Use the configured string as SQL.
     */
    SQL,

    /**
     * Resolve the SQL from the configured source
     */
    LOOKUP
}
