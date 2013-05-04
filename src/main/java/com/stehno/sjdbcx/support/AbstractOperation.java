package com.stehno.sjdbcx.support;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * Created with IntelliJ IDEA.
 * User: cjstehno
 * Date: 4/29/13
 * Time: 7:41 PM
 * To change this template use File | Settings | File Templates.
 */
abstract class AbstractOperation implements Operation {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final String sql;

    protected AbstractOperation( final OperationContext context ){
        this.jdbcTemplate = context.getJdbcTemplate();
        this.sql = context.getSql();
    }

    protected NamedParameterJdbcTemplate getJdbcTemplate(){
        return jdbcTemplate;
    }

    protected String getSql(){
        return sql;
    }
}
