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

import com.stehno.sjdbcx.ParamMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

/**
 * FIXME: document
 *
 * must have a PreparedStatementCallback defined...
 */
class ExecuteOperation extends AbstractOperation {

    private static final Logger log = LoggerFactory.getLogger( ExecuteOperation.class );
    private final ParamMapper paramMapper;
    private final PreparedStatementCallback callback;

    ExecuteOperation( final OperationContext context ){
        super(context);
        this.paramMapper = new ParamMapperExtractor( context.getComponentResolver() ).extract( context.getMethod() );
        this.callback = new PreparedStatementCallbackExtractor( context.getComponentResolver() ).extract( context.getMethod() );
    }

    @Override
    public Object execute( final ParamArg[] args ){
        final SqlParameterSource parameterSource = paramMapper.map( args );

        if( log.isTraceEnabled() ){
            log.trace("Executing-Execute:" );
            log.trace(" - SQL: {}", getSql());
            log.trace(" - Params: {}", parameterSource);
        }

        // TODO: add in a SqlParameterSourceAware similar to ArgumentAware

        final Object result = getJdbcTemplate().execute( getSql(), parameterSource, callback );

        if( log.isTraceEnabled() ){
            log.trace(" - Result: {}", result);
        }

        return result;
    }
}
