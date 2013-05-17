package com.stehno.sjdbcx.support;

import com.stehno.sjdbcx.ParamMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.Collection;
import java.util.List;

/**
 *
 */
class QueryOperation extends AbstractOperation {

    private static final Logger log = LoggerFactory.getLogger( QueryOperation.class );
    private ParamMapper paramMapper;
    private RowMapper rowMapper;
    private ResultSetExtractor<? extends List> resultSetExtractor;
    private Class returnType;

    QueryOperation( final OperationContext context ){
        super(context);
        this.paramMapper = new ParamMapperExtractor( context.getComponentResolver() ).extract( context.getMethod() );

        this.resultSetExtractor = new ResultSetExtractorExtractor( context.getComponentResolver() ).extract( context.getMethod() );
        if( this.resultSetExtractor == null ){
            this.rowMapper = new RowMapperExtractor( context.getComponentResolver() ).extract( context.getMethod() );
        }

        this.returnType = context.getMethod().getReturnType();
    }

    public Object execute( final ParamArg[] args ){
        final SqlParameterSource parameterSource = paramMapper.map( args );

        final String sql = getSql( args );

        if( log.isTraceEnabled() ){
            log.trace("Executing-Query:" );
            log.trace(" - SQL: {}", sql);
            log.trace(" - Params: {}", parameterSource);
        }

        final List results;
        if( resultSetExtractor != null ){
            results = getJdbcTemplate().query( sql, parameterSource, resultSetExtractor );
        } else {
            results = getJdbcTemplate().query( sql, parameterSource, rowMapper );
        }

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
