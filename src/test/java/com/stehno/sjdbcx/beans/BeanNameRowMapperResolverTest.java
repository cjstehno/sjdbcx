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

package com.stehno.sjdbcx.beans;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.util.ReflectionTestUtils;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BeanNameRowMapperResolverTest {

    private BeanNameRowMapperResolver resolver;

    @Mock private ApplicationContext context;
    @Mock private RowMapper mapper;

    @Before
    public void before(){
        resolver = new BeanNameRowMapperResolver();

        ReflectionTestUtils.setField( resolver, "applicationContext", context );
    }

    @Test
    public void resolve(){
        when(context.getBean( eq("foo"), eq(RowMapper.class) )).thenReturn( mapper );

        assertEquals( mapper, resolver.resolve( "foo" ) );
    }
}
