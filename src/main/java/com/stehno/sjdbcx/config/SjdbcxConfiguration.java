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

package com.stehno.sjdbcx.config;

import com.stehno.sjdbcx.RepositoryFactory;
import com.stehno.sjdbcx.beans.ApplicationContextComponentResolver;
import com.stehno.sjdbcx.beans.PropertiesSqlSourceResolver;
import com.stehno.sjdbcx.support.RepositoryInvocationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

/**
 * This configuration bean provides the default beans for the SJDBCX beans and resolvers.
 */
@Configuration
public class SjdbcxConfiguration {

    @Autowired private DataSource dataSource;

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(){
        return new NamedParameterJdbcTemplate( dataSource );
    }

    @Bean
    public PropertiesSqlSourceResolver sqlSourceResolver(){
        return new PropertiesSqlSourceResolver();
    }

    @Bean @Scope("prototype")
    public RepositoryInvocationHandler repositoryInvocationHandler(){
        return new RepositoryInvocationHandler();
    }

    @Bean
    public RepositoryFactory repositoryFactory(){
        return new RepositoryFactory();
    }

    @Bean
    public ApplicationContextComponentResolver componentResolver(){
        return new ApplicationContextComponentResolver();
    }
}
