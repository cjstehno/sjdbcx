package com.stehno.sjdbcx.support;

import com.stehno.sjdbcx.ParamMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.Collection;
import java.util.List;

/**
 *
 */
class QueryOperation implements Operation {

    private static final Logger log = LoggerFactory.getLogger( QueryOperation.class );
    private NamedParameterJdbcTemplate jdbcTemplate;
    private ParamMapper paramMapper;
    private RowMapper rowMapper;
    private Class returnType;
    private String sql;

    QueryOperation( final OperationContext context ){
        this.jdbcTemplate = context.getJdbcTemplate();
        this.paramMapper = context.getParamMapper();
        this.rowMapper = new RowMapperExtractor( context.getComponentResolver() ).extract( context.getMethod() );
        this.returnType = context.getMethod().getReturnType();
        this.sql = context.getSql();
    }

    public Object execute( final ParamArg[] args ){
        final SqlParameterSource parameterSource = paramMapper.map( args );

        if( log.isTraceEnabled() ){
            log.trace("Executing-Query:" );
            log.trace(" - SQL: {}", sql);
            log.trace(" - Params: {}", parameterSource);
        }

        final List results = jdbcTemplate.query( sql, parameterSource, rowMapper );

        if( log.isTraceEnabled() ){
            log.trace(" - Result-count: {}", results.size());
        }

        return mapResultsToReturn( results );
    }

    private Object mapResultsToReturn( final List results ){
        // supports: collection, list, array, single mapped object
        final Object returnValue;
        if( List.class.isAssignableFrom( returnType ) ){
            returnValue = results;

        } else if( Collection.class.equals( returnType ) ){
            returnValue = results;

        } else if( returnType.isArray() ){
            returnValue = results.toArray();

        } else {
            // FIXME: would be better to use row mapper type to determine single-mapped object then fail on "else" fall-through
            // single object

            returnValue = DataAccessUtils.requiredSingleResult( results );
        }
        return returnValue;
    }
}
