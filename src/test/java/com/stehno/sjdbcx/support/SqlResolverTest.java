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

import com.stehno.fixture.Pet;
import com.stehno.fixture.interfaces.PetRepository;
import com.stehno.sjdbcx.support.SqlResolver;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;

import static junit.framework.Assert.assertEquals;

public class SqlResolverTest {

    private final Class repoClass = PetRepository.class;
    private Properties sqlProperties;
    private SqlResolver resolver;

    @Before
    public void before() throws IOException {
        sqlProperties = new Properties();
        sqlProperties.load(getClass().getResourceAsStream("/petrepository.sql.properties"));

        resolver = new SqlResolver();
    }

    @Test
    public void resolves() throws Exception {
        assertProperty( "count.all", resolver.resolve( repoClass, method("count") ) );
        assertProperty( "count.species", resolver.resolve( repoClass, method("count", Pet.Species.class) ) );

        assertProperty( "list.all", resolver.resolve( repoClass, method("list") ) );
        assertProperty( "list.species", resolver.resolve( repoClass, method( "list", Pet.Species.class ) ) );

        assertProperty( "create", resolver.resolve( repoClass, method("create", Pet.class) ) );

        assertProperty( "update", resolver.resolve( repoClass, method("update", Pet.class) ) );

        assertProperty( "delete", resolver.resolve( repoClass, method("delete", long.class) ) );

        assertEquals( "select id,name,species from pets where id=:id", resolver.resolve( repoClass, method( "fetch", long.class ) ) );
    }

    private void assertProperty( final String key, final String sql ){
        assertEquals(sqlProperties.getProperty(key), sql);
    }

    private Method method( final String name, final Class... args ) throws NoSuchMethodException {
        return repoClass.getMethod( name, args );
    }
}

