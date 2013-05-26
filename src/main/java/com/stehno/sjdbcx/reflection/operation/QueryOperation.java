package com.stehno.sjdbcx.reflection.operation;

import com.stehno.sjdbcx.ParamMapper;
import com.stehno.sjdbcx.support.AnnotatedArgument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

/**
 *
 */
public class QueryOperation extends AbstractOperation {

    private static final Logger log = LoggerFactory.getLogger( QueryOperation.class );
    private ParamMapper paramMapper;
    private RowMapper rowMapper;
    private ResultSetExtractor resultSetExtractor;
    private Class returnType;

    public QueryOperation( final Method method, final String sql, final OperationContext context ){
        super(method, sql, context);

        this.paramMapper = context.extractorFor( ParamMapper.class ).extract( method );

        this.resultSetExtractor = context.extractorFor( ResultSetExtractor.class ).extract( method );
        if( this.resultSetExtractor == null ){
            this.rowMapper = context.extractorFor( RowMapper.class ).extract( method );
        }

        this.returnType = method.getReturnType();
    }

    public Object execute( final AnnotatedArgument[] args ){
        final SqlParameterSource parameterSource = paramMapper.map( args );

        final String sql = getSql( args );

        if( log.isTraceEnabled() ){
            log.trace("Executing-Query:" );
            log.trace(" - SQL: {}", sql);
            log.trace(" - Params: {}", parameterSource);
        }

        if( resultSetExtractor != null ){
            return getNamedParameterJdbcTemplate().query( sql, parameterSource, resultSetExtractor );

        } else {
            return mapResultsToReturn( getNamedParameterJdbcTemplate().query( sql, parameterSource, rowMapper ) );
        }
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
