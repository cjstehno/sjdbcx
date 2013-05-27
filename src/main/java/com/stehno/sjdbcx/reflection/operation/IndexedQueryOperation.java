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

package com.stehno.sjdbcx.reflection.operation;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import java.lang.reflect.Method;

import static com.stehno.sjdbcx.reflection.operation.OperationUtils.convert;

/**
 *  Reflective implementation of a query method with indexed parameters. This class is for internal use only.
 */
public class IndexedQueryOperation extends AbstractOperation implements IndexedOperation {

    private RowMapper rowMapper;
    private ResultSetExtractor resultSetExtractor;

    public IndexedQueryOperation( final Method method, final String sql, final OperationContext context ){
        super( method, sql, context );

        this.resultSetExtractor = context.extract( ResultSetExtractor.class, method );

        if( this.resultSetExtractor == null ){
            this.rowMapper = context.extract( RowMapper.class, method );
        }
    }

    @Override
    public Object execute( final String sql, final Object[] params ){
        if( resultSetExtractor != null ){
            return getJdbcOperations().query( sql, params, resultSetExtractor );

        } else {
            return convert( getJdbcOperations().query( sql, params, rowMapper ), getReturnType() );
        }
    }
}
