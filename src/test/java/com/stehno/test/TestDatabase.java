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

package com.stehno.test;

import org.junit.rules.ExternalResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.jdbc.JdbcTestUtils;

public class TestDatabase extends ExternalResource {

    private NamedParameterJdbcTemplate namedJdbcTemplate;
    private JdbcTemplate jdbcTemplate;

    public JdbcTemplate getJdbcTemplate(){
        return jdbcTemplate;
    }

    public NamedParameterJdbcTemplate getNamedJdbcTemplate(){
        return namedJdbcTemplate;
    }

    @Override
    protected void before() throws Throwable {
        // org.h2.Driver
        jdbcTemplate = new JdbcTemplate( new DriverManagerDataSource(
            "jdbc:h2:~/test", "sa", ""
        ));

        namedJdbcTemplate = new NamedParameterJdbcTemplate( jdbcTemplate );

        jdbcTemplate.execute(
            "create table people (id int AUTO_INCREMENT primary key, first_name varchar(40), last_name varchar(40), age int)"
        );
    }

    @Override
    protected void after(){
        JdbcTestUtils.dropTables( jdbcTemplate, "people" );
    }
}
