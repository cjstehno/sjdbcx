package com.stehno.sjdbcx.reflection.operation;

import com.stehno.sjdbcx.support.ArgumentAware;
import org.springframework.jdbc.core.PreparedStatementSetter;

import java.lang.reflect.Method;

import static com.stehno.sjdbcx.reflection.operation.OperationUtils.convert;

/**
 * ... a update operation based on indexed param replacement
 */
public class IndexedUpdateOperation extends AbstractOperation implements IndexedOperation {

    private final PreparedStatementSetter statementSetter;

    public IndexedUpdateOperation( final Method method, final String sql, final OperationContext context ){
        super( method, sql, context );

        this.statementSetter = context.extract( PreparedStatementSetter.class, method );
    }

    @Override
    public Object execute( final String sql, final Object[] params ){
        final int result;
        if( statementSetter != null ){
            if( statementSetter instanceof ArgumentAware ){
                ((ArgumentAware)statementSetter).setArguments( params );
            }

            result = getJdbcOperations().update( sql, statementSetter );

        } else {
            result = getJdbcOperations().update( sql, params );
        }

        logResult( result );

        return convert( result, getReturnType() );
    }
}
