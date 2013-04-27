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
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.lang.annotation.Annotation;

/**
 * The default param mapper which is used if no other param mapper is specified.
 * It may be sub-classed in order to augment its functionality.
 *
 * The Param annotations are processed by this implementation.
 */
public class DefaultParamMapper implements ParamMapper {

    @Override
    public SqlParameterSource map( final ParamArg[] paramArgs ){
        final MapSqlParameterSource parameterSource = new MapSqlParameterSource();

        for( final ParamArg arg: paramArgs ){
            final Annotation annot = arg.findAnnotation( Param.class );
            if( annot == null ){
                addBean(parameterSource, arg.getArgument());

            } else {
                parameterSource.addValue( ((Param)annot).value(), arg.getArgument() );
            }
        }

        return parameterSource;
    }

    private void addBean( final MapSqlParameterSource source, final Object beanObj ){
        final BeanPropertySqlParameterSource beanSource = new BeanPropertySqlParameterSource(beanObj);

        for( final String name: beanSource.getReadablePropertyNames() ){
            source.addValue( name, beanSource.getValue( name ) );
        }
    }
}