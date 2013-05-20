package com.stehno.sjdbcx;

import com.stehno.sjdbcx.support.AnnotatedArgument;

/**
 * SqlTransformers are used to apply a transformation to the SQL string based on the input parameters.
 */
public interface SqlTransformer {

    /**
     *
     * @param sql the unprocessed SQL string
     * @param annotatedArguments the runtime arguments
     * @return the transformed SQL string
     */
    String apply( String sql, AnnotatedArgument[] annotatedArguments );
}
