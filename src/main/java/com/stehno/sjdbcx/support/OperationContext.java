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
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.lang.reflect.Method;

/**
 *
 */
class OperationContext {

    private NamedParameterJdbcTemplate jdbcTemplate;
    private ComponentResolver componentResolver;
    private Method method;
    private String sql;

    void setJdbcTemplate( final NamedParameterJdbcTemplate jdbcTemplate ){
        this.jdbcTemplate = jdbcTemplate;
    }

    void setComponentResolver( final ComponentResolver componentResolver ){
        this.componentResolver = componentResolver;
    }

    void setMethod( final Method method ){
        this.method = method;
    }

    void setSql( final String sql ){
        this.sql = sql;
    }

    Method getMethod(){
        return method;
    }

    ComponentResolver getComponentResolver(){
        return componentResolver;
    }

    NamedParameterJdbcTemplate getJdbcTemplate(){
        return jdbcTemplate;
    }

    String getSql(){
        return sql;
    }
}
