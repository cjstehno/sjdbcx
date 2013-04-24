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

import com.stehno.sjdbcx.annotation.Sql;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Invocation handler used to "implement" the repository interfaces.
 */
class RepositoryInvocationHandler implements InvocationHandler {

    private static final Logger log = LoggerFactory.getLogger(RepositoryInvocationHandler.class);
    private NamedParameterJdbcTemplate jdbcTemplate;
    private SqlSourceResolver sqlSourceResolver;
    private RowMapperResolver rowMapperResolver;
    private ParamMapperResolver paramMapperResolver;

    void setParamMapperResolver( final ParamMapperResolver paramMapperResolver ){
        this.paramMapperResolver = paramMapperResolver;
    }

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
        if( log.isTraceEnabled() ){
            log.trace( "Invoking {}({}) on a {}", method.getName(), Arrays.toString( args ), proxy.getClass() );
        }

        final Sql sqlAnno = AnnotationUtils.getAnnotation( method, Sql.class );
        Assert.notNull( sqlAnno, "No SQL annotation specified." );

        final String sql = extractSql( sqlAnno.value(), sqlAnno.lookup() );
        final SqlParameterSource parameterSource = mapArguments( method, parseArguments( method, args ) );

        if(log.isTraceEnabled()){
            log.trace(" - Type:   {}", sqlAnno.type().name());
            log.trace(" - SQL:    {}", sql);
            log.trace(" - Params: {}", parameterSource);
        }

        if( sqlAnno.type() == Sql.Type.UPDATE ){
            return handleUpdate( method, sql, parameterSource );

        } else {
            return handleQuery( method, sql, parameterSource );
        }
    }

    private Object handleQuery( final Method method, final String sql, final SqlParameterSource parameterSource ) throws IllegalAccessException, InstantiationException{
        final List results = jdbcTemplate.query( sql, parameterSource, configureRowMapper( method ) );

        if( log.isTraceEnabled() ){
            log.trace(" - Result-count: {}", results.size());
        }

        // supports: collection, list, array, single mapped object
        final Class returnType = method.getReturnType();
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
            returnValue = results.get(0);
        }

        return returnValue;
    }

    private Object handleUpdate( final Method method, final String sql, final SqlParameterSource parameterSource ){
        final int result = jdbcTemplate.update( sql, parameterSource );

        if( log.isTraceEnabled() ){
            log.trace(" - Result:  {}", result);
        }

        // supports (return): int, boolean
        Object returnValue = null;
        if( method.getReturnType().equals( boolean.class ) ){
            returnValue = result > 0;
        } else if( method.getReturnType().equals( int.class ) ){
            returnValue = result;
        }

        return returnValue;
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

            } else if( mappedType.isArray() ){
                throw new UnsupportedOperationException( "Auto-mapping for array return types is not yet supported" );
            }

            return new BeanPropertyRowMapper(mappedType);

        } else {
            return rowMapperResolver.resolve( mapper.value() );
        }
    }

    private ParamArg[] parseArguments( final Method method, final Object[] args){
        if( args != null ){
            final ParamArg[] paramArgs = new ParamArg[args.length];
            final Annotation[][] paramAnnos = method.getParameterAnnotations();

            for( int a=0; a<args.length; a++ ){
                paramArgs[a] = new ParamArg( args[a], paramAnnos[a] );
            }

            return paramArgs;
        }

        return null;
    }

    // TODO: move this up
    private final ParamMapper defaultParamMapper = new DefaultParamMapper();

    private SqlParameterSource mapArguments( final Method method, final ParamArg[] paramArgs ){
        if( paramArgs != null ){
            final com.stehno.sjdbcx.annotation.ParamMapper mapper = AnnotationUtils.getAnnotation( method, com.stehno.sjdbcx.annotation.ParamMapper.class );
            if( mapper == null ){
                // FIXME: make this shared single instance
                return defaultParamMapper.map( paramArgs );
            } else {
                // FIXME: error handling
                return paramMapperResolver.resolve( mapper.value() ).map( paramArgs );
            }
        } else {
            return null;
        }
    }
}
