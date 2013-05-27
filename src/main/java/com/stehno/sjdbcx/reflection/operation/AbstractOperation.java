package com.stehno.sjdbcx.reflection.operation;

import com.stehno.sjdbcx.ParamMapper;
import com.stehno.sjdbcx.SqlTransformer;
import com.stehno.sjdbcx.support.AnnotatedArgument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.lang.reflect.Method;

/**
 * An abstract base Operation containing shared functionality.
 */
public abstract class AbstractOperation implements Operation {

    private static final Logger log = LoggerFactory.getLogger( AbstractOperation.class );
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SqlTransformer sqlTransformer;
    private final ParamMapper paramMapper;
    private final Class returnType;
    private final String sql;

    protected AbstractOperation( final Method method, final String sql, final OperationContext context ){
        this.returnType = method.getReturnType();
        this.paramMapper = context.extract( ParamMapper.class, method );
        this.namedParameterJdbcTemplate = context.getNamedParameterJdbcTemplate();
        this.sqlTransformer = context.extract( SqlTransformer.class, method );
        this.sql = sql;
    }

    @Override
    public final Object execute( final AnnotatedArgument[] args ){
        final String mergedSql = getSql( args );

        if( this instanceof IndexedOperation ){
            final Object[] params = paramMapper.mapByIndex( args );

            logExecution( getClass().getSimpleName(), sql, params );

            return ((IndexedOperation)this).execute( mergedSql, params );

        } else if( this instanceof NamedOperation ){
            final SqlParameterSource params = paramMapper.mapByName( args );

            logExecution( getClass().getSimpleName(), sql, params );

            return ((NamedOperation)this).execute( mergedSql, params );

        } else {
            throw new UnsupportedOperationException("Only IndexedOperation and NamedOperation are supported.");
        }
    }

    protected ParamMapper getParamMapper(){
        return paramMapper;
    }

    protected Class getReturnType(){
        return returnType;
    }

    protected NamedParameterJdbcOperations getNamedParameterJdbcOperations(){
        return namedParameterJdbcTemplate;
    }

    protected JdbcOperations getJdbcOperations(){
        return namedParameterJdbcTemplate.getJdbcOperations();
    }

    protected String getSql( final AnnotatedArgument[] annotatedArguments ){
        if( sqlTransformer != null ){
            return sqlTransformer.apply( sql, annotatedArguments );
        } else {
            return sql;
        }
    }

    protected void logExecution( final String opname, final String sql, final Object params ){
        log.debug("Executing-{}:", opname);

        if( log.isTraceEnabled() ){
            log.trace(" - SQL:    {}", sql);
            log.trace(" - Params: {}", params);
        }
    }

    protected void logResult( final Object result ){
        if( log.isTraceEnabled() ){
            log.trace(" - Result: {}", result);
        }
    }
}
