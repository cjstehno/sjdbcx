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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RepositoryFactoryTest {

    private RepositoryFactory factory;
    private PersonRepository repository;
    private Person person;

    @Mock private NamedParameterJdbcTemplate jdbcTemplate;
    @Mock private SqlSourceResolver sqlSourceResolver;
    @Mock private RowMapperResolver rowMapperResolver;
    @Captor private ArgumentCaptor<SqlParameterSource> paramCaptor;

    @Before
    public void before(){
        factory = new RepositoryFactory();
        factory.setNamedParameterJdbcTemplate( jdbcTemplate );
        factory.setSqlSourceResolver( sqlSourceResolver );
        factory.setRowMapperResolver( rowMapperResolver );

        repository = factory.create( PersonRepository.class );
        assertNotNull( repository );

        person = new Person( null, "John", "Doe", 42 );
    }

    @Test
    public void create(){
        repository.create( person );

        verify(jdbcTemplate).update(
            eq("insert into people (first_name,last_name,age) values (:firstName,:lastName,:age)"),
            paramCaptor.capture()
        );

        final CompositeSqlParameterSource paramSource = (CompositeSqlParameterSource)paramCaptor.getValue();

        assertEquals( 5, paramSource.getValues().size() );
        assertEquals( "John", paramSource.getValue( "firstName" ) );
        assertEquals( "Doe", paramSource.getValue( "lastName" ) );
        assertEquals( 42, paramSource.getValue( "age" ) );
        assertEquals( null, paramSource.getValue( "id" ) );
    }

    @Test
    public void list(){
        when( jdbcTemplate.query(
            eq( "select id,first_name,last_name,age from people order by last_name,first_name,age" ),
            paramCaptor.capture(),
            any(BeanPropertyRowMapper.class)
        )).thenReturn(
            Arrays.asList( person )
        );

        final List<Person> people = repository.list();

        assertNotNull( people );
        assertEquals( 1, people.size() );

        final CompositeSqlParameterSource paramSource = (CompositeSqlParameterSource)paramCaptor.getValue();
        assertEquals( 0, paramSource.getValues().size() );
    }

    @Test
    public void fetch(){
        when(jdbcTemplate.query(
            eq("select id,first_name,last_name,age from people where id=:id"),
            paramCaptor.capture(),
            any(BeanPropertyRowMapper.class)
        )).thenReturn(
            Arrays.asList( person )
        );

        final Person pers = repository.fetch( 100 );
        assertNotNull( pers );

        final CompositeSqlParameterSource paramSource = (CompositeSqlParameterSource)paramCaptor.getValue();

        assertEquals( 1, paramSource.getValues().size() );
        assertEquals( 100L, paramSource.getValue( "id" ) );
    }

    @Test
    public void findByAgeRange(){
        when(jdbcTemplate.query(
            eq("select id,first_name,last_name,age from people where age >= :min and age <= :max order by last_name,first_name,age"),
            paramCaptor.capture(),
            any(BeanPropertyRowMapper.class)
        )).thenReturn(
            Arrays.asList( person )
        );

        final List<Person> results = repository.findByAgeRange( 18, 40 );
        assertNotNull( results );
        assertEquals( 1, results.size() );
        assertEquals( person, results.get(0) );

        final CompositeSqlParameterSource paramSource = (CompositeSqlParameterSource)paramCaptor.getValue();

        assertEquals( 2, paramSource.getValues().size() );
        assertEquals( 18, paramSource.getValue( "min" ) );
        assertEquals( 40, paramSource.getValue( "max" ) );
    }

    @Test
    public void update(){
        person.setId( 123L );

        repository.update( person );

        verify(jdbcTemplate).update(
            eq("update people set first_name=:firstName,last_name=:lastName,age=:age where id=:id"),
            paramCaptor.capture()
        );

        final CompositeSqlParameterSource paramSource = (CompositeSqlParameterSource)paramCaptor.getValue();

        assertEquals( 5, paramSource.getValues().size() );
        assertEquals( "John", paramSource.getValue( "firstName" ) );
        assertEquals( "Doe", paramSource.getValue( "lastName" ) );
        assertEquals( 42, paramSource.getValue( "age" ) );
        assertEquals( 123L, paramSource.getValue( "id" ) );
    }

    @Test
    public void delete(){
        when(jdbcTemplate.update(
            eq("delete from people where id=:id"),
            paramCaptor.capture()
        )).thenReturn( 1 );

        final boolean deleted = repository.delete( 246L );
        assertTrue( deleted );

        final CompositeSqlParameterSource paramSource = (CompositeSqlParameterSource)paramCaptor.getValue();

        assertEquals( 1, paramSource.getValues().size() );
        assertEquals( 246L, paramSource.getValue( "id" ) );
    }

    @Test
    public void findByName(){
        when( sqlSourceResolver.resolve("sql.findByName") ).thenReturn( "some sql" );

        when( jdbcTemplate.query( eq( "some sql" ), paramCaptor.capture(), any( BeanPropertyRowMapper.class ) ) ).thenReturn(
            Arrays.asList( person )
        );

        final List<Person> people = repository.findByName("Bob");
        assertEquals( 1, people.size() );

        final CompositeSqlParameterSource paramSource = (CompositeSqlParameterSource)paramCaptor.getValue();

        assertEquals( 1, paramSource.getValues().size() );
        assertEquals( "Bob", paramSource.getValue( "name" ) );
    }

    @Test
    public void countPeople(){
        when( rowMapperResolver.resolve( "singleColumnRowMapper" ) ).thenReturn(
            new SingleColumnRowMapper( Long.class )
        );

        when( jdbcTemplate.query(
            eq( "select count(*) from people" ),
            paramCaptor.capture(),
            any( SingleColumnRowMapper.class )
        ) ).thenReturn(
            Arrays.asList( 1L )
        );

        final long count = repository.countPeople();
        assertEquals( 1L, count );

        final CompositeSqlParameterSource paramSource = (CompositeSqlParameterSource)paramCaptor.getValue();
        assertEquals( 0, paramSource.getValues().size() );
    }
}
