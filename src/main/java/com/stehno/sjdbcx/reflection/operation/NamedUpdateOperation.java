package com.stehno.sjdbcx.reflection.operation;

import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.lang.reflect.Method;

import static com.stehno.sjdbcx.reflection.operation.OperationUtils.convert;

/**
 *  Reflective implementation of an update method. This class is for internal use only.
 */
public class NamedUpdateOperation extends AbstractOperation implements NamedOperation {

    public NamedUpdateOperation( final Method method, final String sql, final OperationContext context ){
        super(method, sql, context);
    }

    @Override
    public Object execute( final String sql, final SqlParameterSource params ){
        final int result = getNamedParameterJdbcOperations().update( sql, params );

        logResult( result );

        return convert( result, getReturnType() );
    }
}
