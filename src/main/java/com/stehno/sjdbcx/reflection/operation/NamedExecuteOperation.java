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

import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.lang.reflect.Method;

/**
 * FIXME: document
 *
 * must have a PreparedStatementCallback defined...
 */
public class NamedExecuteOperation extends AbstractOperation implements NamedOperation {

    private final PreparedStatementCallback callback;

    public NamedExecuteOperation( final Method method, final String sql, final OperationContext context ){
        super(method, sql, context);

        this.callback = context.extract( PreparedStatementCallback.class, method );
    }

    @Override
    public Object execute( final String sql, final SqlParameterSource params ){
        // TODO: add in a SqlParameterSourceAware similar to ArgumentAware

        final Object result = getNamedParameterJdbcOperations().execute( sql, params, callback );

        logResult( result );

        return result;
    }
}
