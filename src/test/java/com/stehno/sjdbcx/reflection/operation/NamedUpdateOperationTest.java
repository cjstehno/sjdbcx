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
import com.stehno.sjdbcx.SqlTransformer;
import com.stehno.sjdbcx.annotation.JdbcRepository;
import com.stehno.sjdbcx.annotation.Sql;
import com.stehno.sjdbcx.reflection.DefaultParamMapper;
import com.stehno.sjdbcx.support.AnnotatedArgument;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NamedUpdateOperationTest {

    private static final String SQL = "select something from somewhere";

    @Mock private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Mock private OperationContext operationContext;

    @Before
    public void before() throws Exception {
        when( operationContext.extract( eq(SqlTransformer.class), any(Method.class) ) ).thenReturn( null );
        when( operationContext.extract( eq(ParamMapper.class), any(Method.class) ) ).thenReturn( new DefaultParamMapper() );

        when( operationContext.getNamedParameterJdbcTemplate() ).thenReturn( namedParameterJdbcTemplate );
    }

    @Test
    public void executeForInt() throws Exception {
        when( namedParameterJdbcTemplate.update( eq( SQL ), any( SqlParameterSource.class ) ) ).thenReturn(42);

        final Method method = UpdateRepository.class.getMethod("intTest");

        final NamedUpdateOperation operation = new NamedUpdateOperation( method, SQL, operationContext );
        assertEquals( 42, operation.execute( new AnnotatedArgument[]{ } ) );
    }

    @Test
    public void executeForTrueBool() throws Exception {
        when( namedParameterJdbcTemplate.update( eq(SQL), any(SqlParameterSource.class) ) ).thenReturn(42);

        final Method method = UpdateRepository.class.getMethod("boolTest");

        final NamedUpdateOperation operation = new NamedUpdateOperation( method, SQL, operationContext );
        assertEquals( true, operation.execute( new AnnotatedArgument[]{ } ) );
    }

    @Test
    public void executeForFalseBool() throws Exception {
        when( namedParameterJdbcTemplate.update( eq(SQL), any(SqlParameterSource.class) ) ).thenReturn(0);

        final Method method = UpdateRepository.class.getMethod("boolTest");

        final NamedUpdateOperation operation = new NamedUpdateOperation( method, SQL, operationContext );
        assertEquals( false, operation.execute( new AnnotatedArgument[]{ } ) );
    }

    @JdbcRepository
    static interface UpdateRepository {

        @Sql int intTest();

        @Sql boolean boolTest();
    }
}
