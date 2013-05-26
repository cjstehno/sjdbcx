package com.stehno.sjdbcx.reflection.operation;

import com.stehno.sjdbcx.IndexedParamMapper;
import com.stehno.sjdbcx.support.AnnotatedArgument;
import com.stehno.sjdbcx.support.ArgumentAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementSetter;

import java.lang.reflect.Method;

/**
 * ... a update operation based on indexed param replacement
 */
public class IndexedUpdateOperation extends AbstractOperation {
    // FIXME: remove duplication with named version

    private static final Logger log = LoggerFactory.getLogger( IndexedUpdateOperation.class );
    private final Class returnType;
    private final PreparedStatementSetter statementSetter;
    private final IndexedParamMapper paramMapper;

    public IndexedUpdateOperation( final Method method, final String sql, final OperationContext context ){
        super( method, sql, context );

        this.statementSetter = context.extractorFor( PreparedStatementSetter.class ).extract( method );
        this.paramMapper = context.extractorFor( IndexedParamMapper.class ).extract( method );
        this.returnType = method.getReturnType();
    }

    @Override
    public Object execute( final AnnotatedArgument[] args ){
        final Object[] params = paramMapper.map( args );

        final String sql = getSql( args );

        if( log.isTraceEnabled() ){
            log.trace("Executing-Update:" );
            log.trace(" - SQL: {}", sql);
            log.trace(" - Params: {}", params);
        }

        final JdbcOperations jdbcOperations = getNamedParameterJdbcTemplate().getJdbcOperations();

        final int result;
        if( statementSetter != null ){
            if( statementSetter instanceof ArgumentAware ){
                ((ArgumentAware)statementSetter).setArguments( params );
            }

            result = jdbcOperations.update( sql, statementSetter );

        } else {
            result = jdbcOperations.update( sql, params );
        }

        if( log.isTraceEnabled() ){
            log.trace(" - Result:  {}", result);
        }

        return mapResultsToReturn( result );
    }

    private Object mapResultsToReturn( final int result ){
        Object returnValue = null;
        if( returnType.equals( boolean.class ) ){
            returnValue = result > 0;
        } else if( returnType.equals( int.class ) ){
            returnValue = result;
        }
        return returnValue;
    }
}
