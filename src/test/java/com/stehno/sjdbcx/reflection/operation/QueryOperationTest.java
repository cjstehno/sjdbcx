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
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class QueryOperationTest {

    private static final String SQL = "select something from somewhere";
    private Method method;

    @Mock private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Mock private OperationContext operationContext;

    @Before
    public void before() throws Exception {
        when( operationContext.extract( eq(SqlTransformer.class), any(Method.class) ) ).thenReturn( null );
        when( operationContext.extract( eq(ParamMapper.class), any(Method.class) ) ).thenReturn( new DefaultParamMapper() );

        when( operationContext.getNamedParameterJdbcTemplate() ).thenReturn( namedParameterJdbcTemplate );

        method = QueryRepository.class.getMethod("tester");
    }

    @Test
    public void executeRowMapper() throws Exception {
        final RowMapper rowMapper = mock(RowMapper.class);
        when( operationContext.extract( eq( RowMapper.class), any(Method.class) ) ).thenReturn( rowMapper );

        final Object returned = new Object();
        when( namedParameterJdbcTemplate.query( eq( SQL ), any( SqlParameterSource.class ), eq( rowMapper ) ) ).thenReturn( Arrays.asList(returned) );

        assertOperation( returned );
    }

    @Test
    public void executeResultSetExtractor() throws Exception {
        final ResultSetExtractor extractor = mock(ResultSetExtractor.class);
        when( operationContext.extract( eq(ResultSetExtractor.class), any(Method.class) ) ).thenReturn( extractor );

        final Object returned = new Object();
        when( namedParameterJdbcTemplate.query( eq( SQL ), any( SqlParameterSource.class ), eq( extractor ) ) ).thenReturn( returned );

        assertOperation( returned );
    }

    private void assertOperation( final Object returned ){
        final QueryOperation operation = new QueryOperation( method, SQL, operationContext );
        assertEquals( returned, operation.execute( new AnnotatedArgument[]{} ) );
    }

    @JdbcRepository
    static interface QueryRepository {

        @Sql Object tester();
    }
}
