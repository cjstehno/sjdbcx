package com.stehno.sjdbcx.support;

import com.stehno.sjdbcx.ParamMapper;
import com.stehno.sjdbcx.support.operation.AbstractOperation;
import com.stehno.sjdbcx.support.operation.OperationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.lang.reflect.Method;

/**
 * Encapsulation of a single update operation method. This is used...
 */
public class UpdateOperation extends AbstractOperation {

    private static final Logger log = LoggerFactory.getLogger(UpdateOperation.class);
    private final ParamMapper paramMapper;
    private final Class returnType;

    public UpdateOperation( final Method method, final String sql, final OperationContext context ){
        super(method, sql, context);

        this.paramMapper = context.extractorFor( ParamMapper.class ).extract( method );
        this.returnType = method.getReturnType();
    }

    public Object execute( final AnnotatedArgument[] args ){
        final SqlParameterSource parameterSource = paramMapper.map( args );

        final String sql = getSql( args );

        if( log.isTraceEnabled() ){
            log.trace("Executing-Update:" );
            log.trace(" - SQL: {}", sql);
            log.trace(" - Params: {}", parameterSource);
        }

        final int result = getNamedParameterJdbcTemplate().update( sql, parameterSource );

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
