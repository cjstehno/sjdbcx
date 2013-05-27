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

package com.stehno.fixture.interfaces;

import com.stehno.fixture.Pet;
import com.stehno.sjdbcx.reflection.operation.AbstractOperation;
import com.stehno.sjdbcx.reflection.operation.IndexedOperation;
import com.stehno.sjdbcx.reflection.operation.OperationContext;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.lang.reflect.Method;

public class FindWhereOperation extends AbstractOperation implements IndexedOperation {

    public FindWhereOperation( final Method method, final String sql, final OperationContext context ){
        super( method, sql, context );
    }

    @Override
    public Object execute( final String sql, final Object[] params ){
        return getJdbcOperations().query(
            sql,
            new BeanPropertyRowMapper<>(Pet.class),
            String.format("%%%s%%", params[0]),
            ((Pet.Species)params[1]).name()
        );
    }
}
