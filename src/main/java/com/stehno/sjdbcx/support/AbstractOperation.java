package com.stehno.sjdbcx.support;

import com.stehno.sjdbcx.SqlTransformer;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * An abstract base Operation containing shared functionality.
 */
abstract class AbstractOperation implements Operation {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SqlTransformer sqlTransformer;
    private final String sql;

    protected AbstractOperation( final OperationContext context ){
        this.jdbcTemplate = context.getJdbcTemplate();
        this.sqlTransformer = new SqlTransformerExtractor( context.getComponentResolver() ).extract( context.getMethod() );
        this.sql = context.getSql();
    }

    protected NamedParameterJdbcTemplate getJdbcTemplate(){
        return jdbcTemplate;
    }

    protected String getSql( final ParamArg[] paramArgs ){
        if( sqlTransformer != null ){
            return sqlTransformer.apply( sql, paramArgs );
        } else {
            return sql;
        }
    }
}
