package com.stehno.sjdbcx.support;

import com.stehno.sjdbcx.ParamMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

/**
 * Encapsulation of a single update operation method. This is used...
 */
class UpdateOperation extends AbstractOperation {

    private static final Logger log = LoggerFactory.getLogger(UpdateOperation.class);
    private final ParamMapper paramMapper;
    private final Class returnType;

    UpdateOperation( final OperationContext context ){
        super(context);
        this.paramMapper = new ParamMapperExtractor( context.getComponentResolver() ).extract( context.getMethod() );
        this.returnType = context.getMethod().getReturnType();
    }

    public Object execute( final ParamArg[] args ){
        final SqlParameterSource parameterSource = paramMapper.map( args );

        final String sql = getSql( args );

        if( log.isTraceEnabled() ){
            log.trace("Executing-Update:" );
            log.trace(" - SQL: {}", sql);
            log.trace(" - Params: {}", parameterSource);
        }

        final int result = getJdbcTemplate().update( sql, parameterSource );

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
