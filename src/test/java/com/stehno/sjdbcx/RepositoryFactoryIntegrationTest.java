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
import com.stehno.sjdbcx.beans.ApplicationContextComponentResolver;
import com.stehno.sjdbcx.support.RepositoryInvocationHandler;
import com.stehno.test.TestDatabase;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RepositoryFactoryIntegrationTest {

    private static final String TABLE_NAME = "people";
    private PersonRepository repository;
    private Person person;

    @Rule public TestDatabase database = new TestDatabase();

    @Before
    public void before(){
        final ApplicationContext applicationContext = mock(ApplicationContext.class);

        final ApplicationContextComponentResolver componentResolver = new ApplicationContextComponentResolver();
        ReflectionTestUtils.setField( componentResolver, "applicationContext", applicationContext );

        final SqlSource sqlSource = mock(SqlSource.class);
        when(sqlSource.getSql("findbyname")).thenReturn("select id,first_name,last_name,age from people where first_name=:name");

        final SqlSourceResolver sqlSourceResolver = mock(SqlSourceResolver.class);
        when( sqlSourceResolver.resolve( new ClassPathResource( "/personrepository.sql.properties" ) ) ).thenReturn(sqlSource);

        final RepositoryInvocationHandler handler = new RepositoryInvocationHandler();
        ReflectionTestUtils.setField( handler, "namedParameterJdbcTemplate", database.getNamedJdbcTemplate() );
        ReflectionTestUtils.setField( handler, "componentResolver", componentResolver );
        ReflectionTestUtils.setField( handler, "sqlSourceResolver", sqlSourceResolver );

        when(applicationContext.getBean("repositoryInvocationHandler", RepositoryInvocationHandler.class)).thenReturn(handler);

        final RepositoryFactory factory = new RepositoryFactory();
        ReflectionTestUtils.setField( factory, "applicationContext", applicationContext );


//        when(componentResolver.resolve( "singleColumnRowMapper", RowMapper.class )).thenReturn(
//            new SingleColumnRowMapper( Long.class )
//        );

        repository = factory.create( PersonRepository.class );
        assertNotNull( repository );

        person = new Person( null, "John", "Doe", 42 );
    }

    @Test
    public void exercise(){
        repository.create( person );

        assertEquals( 1, JdbcTestUtils.countRowsInTable( database.getJdbcTemplate(), TABLE_NAME ) );

        final List<Person> people = repository.list();
        assertEquals( 1, people.size() );
        assertEquals( person, people.get( 0 ) );

        assertEquals( person, repository.fetch( 1 ) );

        assertEquals( 0, repository.findByAgeRange( 18, 20 ).size() );
        assertEquals( person, repository.findByAgeRange( 40, 50 ).get( 0 ) );

        assertEquals( 1L, repository.countPeople() );

        assertEquals( person, repository.findByName( "John" ).get( 0 ) );

        final Person toUpdate = people.get(0);
        toUpdate.setFirstName( "Jane" );

        repository.update( toUpdate );

        assertEquals( toUpdate, repository.fetch( 1 ) );

        repository.delete( 1L );

        assertEquals( 0, JdbcTestUtils.countRowsInTable( database.getJdbcTemplate(), TABLE_NAME ) );
    }
}
