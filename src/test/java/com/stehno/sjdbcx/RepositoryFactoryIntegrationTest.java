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

package com.stehno.sjdbcx;

import com.stehno.fixture.Person;
import com.stehno.fixture.PersonRepository;
import com.stehno.test.TestDatabase;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class RepositoryFactoryIntegrationTest {

    private static final String TABLE_NAME = "people";
    private RepositoryFactory factory;
    private PersonRepository repository;
    private Person person;

    @Rule public TestDatabase database = new TestDatabase();
    @Mock private SqlSourceResolver sqlSourceResolver;
    @Mock private RowMapperResolver rowMapperResolver;

    @Before
    public void before(){
        factory = new RepositoryFactory();
        factory.setNamedParameterJdbcTemplate( database.getNamedJdbcTemplate() );
        factory.setSqlSourceResolver( sqlSourceResolver );
        factory.setRowMapperResolver( rowMapperResolver );

        repository = factory.create( PersonRepository.class );
        assertNotNull( repository );

        person = new Person( null, "John", "Doe", 42 );
    }

    @Test
    public void create(){
        repository.create( person );

        assertEquals( 1, JdbcTestUtils.countRowsInTable( database.getJdbcTemplate(), TABLE_NAME ) );

        final List<Person> people = repository.list();
        assertEquals( 1, people.size() );
    }
}
