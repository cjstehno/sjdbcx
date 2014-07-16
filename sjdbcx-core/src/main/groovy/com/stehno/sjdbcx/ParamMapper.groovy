package com.stehno.sjdbcx

import org.springframework.jdbc.core.namedparam.SqlParameterSource

/**
 * Created by cjstehno on 7/15/2014.
 */
interface ParamMapper {

    /**
     * Converts the input arguments to a SqlParameterSource object for a SQL statement using named
     * parameters.
     *
     * @param annotatedArguments
     * @return
     */
    SqlParameterSource mapByName( AnnotatedArgument[] annotatedArguments );

    /**
     * Converts the input arguments to a SqlParameterSource object for a SQL statement using indexed
     * parameters.
     *
     * @param annotatedArguments
     * @return
     */
    public Object[] mapByIndex( final AnnotatedArgument[] annotatedArguments );
}
