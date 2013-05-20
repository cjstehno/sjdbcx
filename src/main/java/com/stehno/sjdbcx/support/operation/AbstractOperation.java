package com.stehno.sjdbcx.support.operation;

import com.stehno.sjdbcx.SqlTransformer;
import com.stehno.sjdbcx.support.AnnotatedArgument;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.lang.reflect.Method;

/**
 * An abstract base Operation containing shared functionality.
 */
public abstract class AbstractOperation implements Operation {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SqlTransformer sqlTransformer;
    private final String sql;

    protected AbstractOperation( final Method method, final String sql, final OperationContext context ){
        this.namedParameterJdbcTemplate = context.getNamedParameterJdbcTemplate();
        this.sqlTransformer = context.extractorFor( SqlTransformer.class ).extract( method );
        this.sql = sql;
    }

    protected NamedParameterJdbcTemplate getNamedParameterJdbcTemplate(){
        return namedParameterJdbcTemplate;
    }

    protected String getSql( final AnnotatedArgument[] annotatedArguments ){
        if( sqlTransformer != null ){
            return sqlTransformer.apply( sql, annotatedArguments );
        } else {
            return sql;
        }
    }
}
