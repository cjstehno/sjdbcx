package com.stehno.sjdbcx;

/**
 * Param mappers may be resolved from any supporting source.
 */
public interface ParamMapperResolver {

    ParamMapper resolve( final String name );
}
