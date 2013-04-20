/*
 * Copyright (c) 2013 Christopher J. Stehno
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.stehno.sjdbcx;

import com.stehno.sjdbcx.annotation.Param;
import com.stehno.sjdbcx.annotation.Sql;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Invocation handler used to "implement" the repository interfaces.
 */
class RepositoryInvocationHandler implements InvocationHandler {

    private NamedParameterJdbcTemplate jdbcTemplate;
    private SqlSourceResolver sqlSourceResolver;
    private RowMapperResolver rowMapperResolver;

    void setJdbcTemplate( final NamedParameterJdbcTemplate jdbcTemplate ){
        this.jdbcTemplate = jdbcTemplate;
    }

    void setSqlSourceResolver( final SqlSourceResolver sqlSourceResolver ){
        this.sqlSourceResolver = sqlSourceResolver;
    }

    void setRowMapperResolver( final RowMapperResolver rowMapperResolver ){
        this.rowMapperResolver = rowMapperResolver;
    }

    @Override
    public Object invoke( final Object proxy, final Method method, final Object[] args ) throws Throwable {
        final Sql sqlAnno = AnnotationUtils.getAnnotation( method, Sql.class );
        Assert.notNull( sqlAnno, "No SQL annotation specified." );

        final String sql = extractSql( sqlAnno.value(), sqlAnno.lookup() );
        final SqlParameterSource parameterSource = parseArguments( method, args );

        if( sqlAnno.type() == Sql.Type.UPDATE ){
            return jdbcTemplate.update( sql, parameterSource );

        } else {
            // TODO: may need to differentiate between single results vs collection or array
            return jdbcTemplate.query( sql, parameterSource, configureRowMapper(method) );
        }
    }

    private String extractSql( final String value, final boolean lookup ){
        return lookup ? sqlSourceResolver.resolve(value) : value;
    }

    private RowMapper configureRowMapper( final Method method ) throws IllegalAccessException, InstantiationException {
        final com.stehno.sjdbcx.annotation.RowMapper mapper = AnnotationUtils.getAnnotation( method, com.stehno.sjdbcx.annotation.RowMapper.class );
        return mapper == null ? new BeanPropertyRowMapper(method.getReturnType()) : rowMapperResolver.resolveRowMapper(mapper.value());
    }

    private SqlParameterSource parseArguments( final Method method, final Object[] args) {
        final CompositeSqlParameterSource parameterSource = new CompositeSqlParameterSource();

        if( args != null ){
            final Annotation[][] paramAnnos = method.getParameterAnnotations();

            outer: for( int a=0; a<args.length; a++ ){
                for( final Annotation ann: paramAnnos[a] ){
                    if( ann instanceof Param ){
                        parameterSource.addValue( ((Param)ann).value(), args[a] );
                        continue outer;
                    }
                }

                parameterSource.addBean(args[a]);
            }
        }

        return parameterSource;
    }
}
