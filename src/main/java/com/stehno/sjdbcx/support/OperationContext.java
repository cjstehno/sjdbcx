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
import com.stehno.sjdbcx.ParamMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.lang.reflect.Method;

/**
 *
 */
public class OperationContext {

    private final Method method;
    private final String sql;
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ComponentResolver componentResolver;
    private final ParamMapper paramMapper;

    public OperationContext( final Method method, final String sql, final ComponentResolver componentResolver, final NamedParameterJdbcTemplate jdbcTemplate, final ParamMapper paramMapper ){
        this.method = method;
        this.sql = sql;
        this.componentResolver = componentResolver;
        this.jdbcTemplate = jdbcTemplate;
        this.paramMapper = paramMapper;
    }

    public Method getMethod(){
        return method;
    }

    public ParamMapper getParamMapper(){
        return paramMapper;
    }

    public ComponentResolver getComponentResolver(){
        return componentResolver;
    }

    public NamedParameterJdbcTemplate getJdbcTemplate(){
        return jdbcTemplate;
    }

    public String getSql(){
        return sql;
    }
}
