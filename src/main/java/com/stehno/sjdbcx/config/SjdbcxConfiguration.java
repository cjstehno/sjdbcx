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

import com.stehno.sjdbcx.ParamMapperResolver;
import com.stehno.sjdbcx.RepositoryFactory;
import com.stehno.sjdbcx.RowMapperResolver;
import com.stehno.sjdbcx.beans.BeanNameParamMapperResolver;
import com.stehno.sjdbcx.beans.BeanNameRowMapperResolver;
import com.stehno.sjdbcx.beans.PropertiesSqlSourceResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * This configuration bean provides the default beans for the SJDBCX beans and resolvers.
 */
@Configuration
public class SjdbcxConfiguration {

    @Autowired private DataSource dataSource;

    @Bean
    public PropertiesSqlSourceResolver sqlSourceResolver(){
        return new PropertiesSqlSourceResolver();
    }

    @Bean
    public RepositoryFactory repositoryFactory(){
        final RepositoryFactory factory = new RepositoryFactory();
        factory.setNamedParameterJdbcTemplate( new NamedParameterJdbcTemplate( dataSource ));
        factory.setSqlSourceResolver( sqlSourceResolver() );
        factory.setRowMapperResolver( rowMapperResolver() );
        factory.setParamMapperResolver( paramMapperResolver() );
        return factory;
    }

    @Bean
    public RowMapperResolver rowMapperResolver(){
        return new BeanNameRowMapperResolver();
    }

    @Bean
    public ParamMapperResolver paramMapperResolver(){
        return new BeanNameParamMapperResolver();
    }
}
