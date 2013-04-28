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

package com.stehno.sjdbcx.support;

import com.stehno.sjdbcx.ComponentResolver;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;

/**
 *
 */
class RowMapperExtractor {

    private final ComponentResolver componentResolver;

    RowMapperExtractor( final ComponentResolver componentResolver ){
        this.componentResolver = componentResolver;
    }

    RowMapper extract( final Method method ){
        final com.stehno.sjdbcx.annotation.RowMapper mapper = AnnotationUtils.getAnnotation( method, com.stehno.sjdbcx.annotation.RowMapper.class );
        if( mapper == null ){
            Class mappedType = method.getReturnType();

            if( Collection.class.isAssignableFrom( mappedType ) ){
                mappedType = (Class)((ParameterizedType)method.getGenericReturnType()).getActualTypeArguments()[0];

            } else if( mappedType.isArray() ){
                throw new UnsupportedOperationException( "Auto-mapping for array return types is not yet supported" );

            } else if( mappedType.isPrimitive() ){
                if( mappedType == int.class || mappedType == long.class ){
                    return new SingleColumnRowMapper();
                }
            }

            return new BeanPropertyRowMapper(mappedType);

        } else {
            return componentResolver.resolve( mapper.value(), org.springframework.jdbc.core.RowMapper.class );
        }
    }
}
