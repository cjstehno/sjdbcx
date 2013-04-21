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

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.Map;

/**
 * FIXME: document
 */
public class CompositeSqlParameterSource implements SqlParameterSource {

    private final MapSqlParameterSource defaultSource = new MapSqlParameterSource();

    public CompositeSqlParameterSource addValue( final String name, final Object value ){
        defaultSource.addValue(name, value);
        return this;
    }

    public CompositeSqlParameterSource addBean( final Object beanObj ){
        final BeanPropertySqlParameterSource beanSource = new BeanPropertySqlParameterSource(beanObj);

        for( final String name: beanSource.getReadablePropertyNames() ){
            defaultSource.addValue(name, beanSource.getValue(name) );
        }

        return this;
    }

    Map<String,Object> getValues(){
        return defaultSource.getValues();
    }

    @Override
    public boolean hasValue(String name) {
        return defaultSource.hasValue(name);
    }

    @Override
    public Object getValue(String name) throws IllegalArgumentException {
        return defaultSource.getValue(name);
    }

    @Override
    public int getSqlType(String name) {
        return defaultSource.getSqlType(name);
    }

    @Override
    public String getTypeName(String s) {
        return defaultSource.getTypeName(s);
    }

    @Override
    public String toString(){
        return defaultSource.getValues().toString();
    }
}
