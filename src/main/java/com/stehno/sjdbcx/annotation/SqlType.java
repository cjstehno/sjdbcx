package com.stehno.sjdbcx.annotation;

/**
 * Enumerates the allowed SQL statement types for use in the SQL annotation.
 */
public enum SqlType {

    /**
     * Produces an update call.
     */
    UPDATE,

    /**
     * Produces a query call.
     */
    QUERY,

    /**
     * Produces an execute call.
     */
    EXECUTE
}
