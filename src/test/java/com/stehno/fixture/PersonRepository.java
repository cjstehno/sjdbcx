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

package com.stehno.fixture;

import com.stehno.sjdbcx.annotation.Param;
import com.stehno.sjdbcx.annotation.RowMapper;
import com.stehno.sjdbcx.annotation.Sql;

import java.util.List;

/*

 */
@SuppressWarnings("ALL")
public interface PersonRepository {

    static final String SQL = "insert into people (first_name,last_name,age) values (:firstName,:lastName,:age)";

    @Sql(value=SQL, type=Sql.Type.UPDATE)
    void create( Person person );

    @Sql("select id,first_name,last_name,age from people order by last_name,first_name,age")
    List<Person> list();

    @Sql("select id,first_name,last_name,age from people where id=:id")
    Person fetch( @Param("id") long personId );

    @Sql("select id,first_name,last_name,age from people where age >= :min and age <= :max order by last_name,first_name,age")
    List<Person> findByAgeRange( @Param("min") int ageMin, @Param("max") int ageMax );

    @Sql(value="update people set first_name=:firstName,last_name=:lastName,age=:age where id=:id", type=Sql.Type.UPDATE)
    void update( Person person );

    @Sql(value="delete from people where id=:id", type=Sql.Type.UPDATE)
    boolean delete( @Param("id") long personId );

    @Sql(value="sql.findByName", lookup=true)
    List<Person> findByName( @Param("name") final String name );

    @Sql("select count(*) from people") @RowMapper("singleColumnRowMapper")
    long countPeople();
}