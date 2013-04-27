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

import com.stehno.fixture.PersonRepository;
import com.stehno.sjdbcx.ComponentResolver;
import com.stehno.sjdbcx.RepositoryFactory;
import com.stehno.sjdbcx.SqlSourceResolver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class RepositoryFactoryBeanTest {

    private RepositoryFactoryBean factoryBean;
    private RepositoryFactory repositoryFactory;

    @Mock private SqlSourceResolver sqlSourceResolver;
    @Mock private ComponentResolver componentResolver;
    @Mock private NamedParameterJdbcTemplate jdbcTemplate;

    @Before
    public void before() throws Exception {
        repositoryFactory = new RepositoryFactory();
        repositoryFactory.setComponentResolver( componentResolver );
        repositoryFactory.setSqlSourceResolver( sqlSourceResolver );
        repositoryFactory.setNamedParameterJdbcTemplate( jdbcTemplate );

        factoryBean = new RepositoryFactoryBean( repositoryFactory, PersonRepository.class );
        factoryBean.afterPropertiesSet();
    }

    @Test
    public void getObjectType(){
        assertEquals( PersonRepository.class, factoryBean.getObjectType() );
    }

    @Test
    public void createInstance() throws Exception {
        final PersonRepository repository = (PersonRepository)factoryBean.createInstance();

        assertNotNull( repository );
    }
}
