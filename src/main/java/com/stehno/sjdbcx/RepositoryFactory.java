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

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.lang.reflect.Proxy;

public class RepositoryFactory {
    // TODO: need to consider the performance aspects of this
    // TODO: could allow un-annotated methods to pass through
    // TODO: consider - metrics, jmx, logging, result caching
    // TODO: cache the various objects used here

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private SqlSourceResolver sqlSourceResolver;
    private RowMapperResolver rowMapperResolver;

    public void setRowMapperResolver( final RowMapperResolver rowMapperResolver ){
        this.rowMapperResolver = rowMapperResolver;
    }

    public void setSqlSourceResolver( final SqlSourceResolver sqlSourceResolver ){
        this.sqlSourceResolver = sqlSourceResolver;
    }

    public void setNamedParameterJdbcTemplate( final NamedParameterJdbcTemplate namedParameterJdbcTemplate ){
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @SuppressWarnings("unchecked")
    public <T> T create( Class<T> repoIface ){
        // TODO: this could probably be shared
        final RepositoryInvocationHandler handler = new RepositoryInvocationHandler();
        handler.setSqlSourceResolver( sqlSourceResolver );
        handler.setJdbcTemplate( namedParameterJdbcTemplate );
        handler.setRowMapperResolver( rowMapperResolver );

        return (T)Proxy.newProxyInstance( repoIface.getClassLoader(), new Class[]{ repoIface }, handler );
    }
}
