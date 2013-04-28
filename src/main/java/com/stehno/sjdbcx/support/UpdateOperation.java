package com.stehno.sjdbcx.support;

import com.stehno.sjdbcx.ParamMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

/**
 * Encapsulation of a single update operation method. This is used...
 */
class UpdateOperation implements Operation {

    private static final Logger log = LoggerFactory.getLogger(UpdateOperation.class);
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ParamMapper paramMapper;
    private final Class returnType;
    private final String sql;

    UpdateOperation( final OperationContext context ){
        this.jdbcTemplate = context.getJdbcTemplate();
        this.paramMapper = context.getParamMapper();
        this.returnType = context.getMethod().getReturnType();
        this.sql = context.getSql();
    }

    public Object execute( final ParamArg[] args ){
        final SqlParameterSource parameterSource = paramMapper.map( args );

        if( log.isTraceEnabled() ){
            log.trace("Executing-Update:" );
            log.trace(" - SQL: {}", sql);
            log.trace(" - Params: {}", parameterSource);
        }

        final int result = jdbcTemplate.update( sql, parameterSource );

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
