package com.stehno.sjdbcx.reflection.operation;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.lang.reflect.Method;

import static com.stehno.sjdbcx.reflection.operation.OperationUtils.convert;

/**
 *  Reflective implementation of a query method with named parameters. This class is for internal use only.
 */
public class NamedQueryOperation extends AbstractOperation implements NamedOperation {

    private RowMapper rowMapper;
    private ResultSetExtractor resultSetExtractor;

    public NamedQueryOperation( final Method method, final String sql, final OperationContext context ){
        super(method, sql, context);

        this.resultSetExtractor = context.extract( ResultSetExtractor.class, method );

        if( this.resultSetExtractor == null ){
            this.rowMapper = context.extract( RowMapper.class, method );
        }
    }

    @Override
    public Object execute( final String sql, final SqlParameterSource params ){
        if( resultSetExtractor != null ){
            return getNamedParameterJdbcOperations().query( sql, params, resultSetExtractor );

        } else {
            return convert( getNamedParameterJdbcOperations().query( sql, params, rowMapper ), getReturnType() );
        }
    }
}
