package com.stehno.sjdbcx.reflection.operation;

import com.stehno.sjdbcx.ParamMapper;
import com.stehno.sjdbcx.support.AnnotatedArgument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.lang.reflect.Method;

import static com.stehno.sjdbcx.reflection.operation.OperationUtils.convert;

/**
 *  Reflective implementation of a query method. This class is for internal use only.
 */
public class QueryOperation extends AbstractOperation {

    private static final Logger log = LoggerFactory.getLogger( QueryOperation.class );
    private ParamMapper paramMapper;
    private RowMapper rowMapper;
    private ResultSetExtractor resultSetExtractor;
    private Class returnType;

    public QueryOperation( final Method method, final String sql, final OperationContext context ){
        super(method, sql, context);

        this.paramMapper = context.extract( ParamMapper.class, method );

        this.resultSetExtractor = context.extract( ResultSetExtractor.class, method );
        if( this.resultSetExtractor == null ){
            this.rowMapper = context.extract( RowMapper.class, method );
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

        final NamedParameterJdbcTemplate jdbcTemplate = getNamedParameterJdbcTemplate();

        if( resultSetExtractor != null ){
            return jdbcTemplate.query( sql, parameterSource, resultSetExtractor );

        } else {
            return convert( jdbcTemplate.query( sql, parameterSource, rowMapper ), returnType );
        }
    }
}
