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

package com.stehno.sjdbcx.reflection;

import com.stehno.sjdbcx.ParamMapper;
import com.stehno.sjdbcx.annotation.Ignore;
import com.stehno.sjdbcx.annotation.Param;
import com.stehno.sjdbcx.support.AnnotatedArgument;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.lang.annotation.Annotation;

/**
 * The default param mapper which is used if no other param mapper is specified.
 * It may be sub-classed in order to augment its functionality.
 *
 * enums are converted to strings as .name()
 *
 * The Param annotations are processed by this implementation.
 * Any parameter annotated with Ignore will not be processed.
 */
public class DefaultParamMapper implements ParamMapper {

    @Override
    public SqlParameterSource mapByName( final AnnotatedArgument[] annotatedArguments ){
        final MapSqlParameterSource parameterSource = new MapSqlParameterSource();

        if( annotatedArguments != null ){
            for( final AnnotatedArgument arg: annotatedArguments ){
                if( arg.findAnnotation(Ignore.class) == null ){
                    final Annotation paramAnno = arg.findAnnotation( Param.class );
                    if( paramAnno == null ){
                        addBean(parameterSource, arg.getValue());

                    } else {
                        Object value = arg.getValue();
                        if( value != null && value.getClass().isEnum() ){
                            value = (( Enum )value).name();
                        }

                        parameterSource.addValue( ((Param)paramAnno).value(), value );
                    }
                }
            }
        }

        return parameterSource;
    }

    @Override
    public Object[] mapByIndex( final AnnotatedArgument[] annotatedArguments ){
        final Object[] args = new Object[annotatedArguments.length];
        for( int i=0; i< annotatedArguments.length; i++ ){
            args[i] = annotatedArguments[i].getValue();
        }
        return args;
    }

    private void addBean( final MapSqlParameterSource source, final Object beanObj ){
        final BeanPropertySqlParameterSource beanSource = new BeanPropertySqlParameterSource(beanObj);

        for( final String name: beanSource.getReadablePropertyNames() ){
            source.addValue( name, beanSource.getValue( name ) );
        }
    }
}