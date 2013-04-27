package com.stehno.sjdbcx;

/**
 * Defines the resolver used to lookup helper beans such as RowMappers and ParamMappers.
 */
public interface ComponentResolver {

    <T> T resolve( String name, Class<? extends T> type );

    <T> T resolve( Class<? extends T> type );
}
