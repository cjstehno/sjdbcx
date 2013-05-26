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

import com.stehno.sjdbcx.ParamMapper;
import com.stehno.sjdbcx.support.AnnotatedArgument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.lang.reflect.Method;

/**
 * FIXME: document
 *
 * must have a PreparedStatementCallback defined...
 */
public class ExecuteOperation extends AbstractOperation {

    private static final Logger log = LoggerFactory.getLogger( ExecuteOperation.class );
    private final ParamMapper paramMapper;
    private final PreparedStatementCallback callback;

    public ExecuteOperation( final Method method, final String sql, final OperationContext context ){
        super(method, sql, context);

        this.paramMapper = context.extractorFor( ParamMapper.class ).extract( method );
        this.callback = context.extractorFor( PreparedStatementCallback.class ).extract( method );
    }

    @Override
    public Object execute( final AnnotatedArgument[] args ){
        final SqlParameterSource parameterSource = paramMapper.map( args );

        final String sql = getSql( args );

        if( log.isTraceEnabled() ){
            log.trace("Executing-Execute:" );
            log.trace(" - SQL: {}", sql);
            log.trace(" - Params: {}", parameterSource);
        }

        // TODO: add in a SqlParameterSourceAware similar to ArgumentAware

        final Object result = getNamedParameterJdbcTemplate().execute( sql, parameterSource, callback );

        if( log.isTraceEnabled() ){
            log.trace(" - Result: {}", result);
        }

        return result;
    }
}
