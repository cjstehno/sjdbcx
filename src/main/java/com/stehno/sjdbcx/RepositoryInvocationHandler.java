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
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;

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
            final int result = jdbcTemplate.update( sql, parameterSource );

            // supports (return): int, boolean
            Object returnValue = null;
            if( method.getReturnType().equals( boolean.class ) ){
                returnValue = result > 0;
            } else if( method.getReturnType().equals( int.class ) ){
                returnValue = result;
            }

            return returnValue;

        } else {
            final List results = jdbcTemplate.query( sql, parameterSource, configureRowMapper(method) );

            // supports: collection, list, array, single mapped object
            final Object returnValue;
            if( List.class.isAssignableFrom( method.getReturnType() ) ){
                returnValue = results;

            } else if( Collection.class.equals( method.getReturnType() ) ){
                returnValue = results;

            } else if( method.getReturnType().isArray() ){
                returnValue = results.toArray();

            } else {
                // FIXME: would be better to use row mapper type to determine single-mapped object then fail on "else" fall-through
                // single object
                return results.get(0);
            }

            return returnValue;
        }
    }

    private String extractSql( final String value, final boolean lookup ){
        return lookup ? sqlSourceResolver.resolve(value) : value;
    }

    private RowMapper configureRowMapper( final Method method ) throws IllegalAccessException, InstantiationException {
        final com.stehno.sjdbcx.annotation.RowMapper mapper = AnnotationUtils.getAnnotation( method, com.stehno.sjdbcx.annotation.RowMapper.class );
        if( mapper == null ){
            Class mappedType = method.getReturnType();
            if( Collection.class.isAssignableFrom( mappedType ) ){
                mappedType = (Class)((ParameterizedType)method.getGenericReturnType()).getActualTypeArguments()[0];
            }
            // TODO: do for array and map too

            return new BeanPropertyRowMapper(mappedType);

        } else {
            return rowMapperResolver.resolveRowMapper(mapper.value());
        }
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
