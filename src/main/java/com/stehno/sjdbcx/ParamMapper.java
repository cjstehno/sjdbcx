package com.stehno.sjdbcx;

import org.springframework.jdbc.core.namedparam.SqlParameterSource;

/**
 * Defines how the method input parameters are mapped to the SQL statement replacement variables.
 * Implementations should be stateless and thread-safe.
 *
 * Note that implementations must also process the Param annotations if they want them to be supported.
 */
public interface ParamMapper {

    /**
     * Converts the input arguments to a SqlParameterSource object.
     *
     * @param paramArgs
     * @return
     */
    SqlParameterSource map( ParamArg[] paramArgs );
}
