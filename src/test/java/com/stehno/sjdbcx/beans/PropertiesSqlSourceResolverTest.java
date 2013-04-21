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
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Properties;

import static junit.framework.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class PropertiesSqlSourceResolverTest {

    private static final String KEY = "some.prop";
    private static final String VALUE = "select something";

    private PropertiesSqlSourceResolver resolver;
    private Properties properties;

    @Before
    public void before(){
        properties = new Properties();
        properties.setProperty( KEY, VALUE );

        resolver = new PropertiesSqlSourceResolver();
        resolver.setProperties( properties );
    }

    @Test
    public void resolve(){
        assertEquals( VALUE, resolver.resolve( KEY ) );
    }

    @Test(expected = IllegalArgumentException.class)
    public void resolveNonExisting(){
        resolver.resolve( "blah" );
    }
}
