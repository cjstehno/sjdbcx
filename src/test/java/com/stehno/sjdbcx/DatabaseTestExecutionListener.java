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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;

public class DatabaseTestExecutionListener extends AbstractTestExecutionListener {

    private static final Logger log = LoggerFactory.getLogger( DatabaseTestExecutionListener.class );

    @Override
    public void beforeTestMethod(final TestContext testContext) throws Exception {
        JdbcTestUtils.deleteFromTables(getJdbcTemplate(testContext.getApplicationContext()), "pets", "people");

        log.info("Deleted data from tables: 'pets', 'people'...");
    }

    private JdbcTemplate getJdbcTemplate( final ApplicationContext applicationContext ){
        return new JdbcTemplate( applicationContext.getBean(DataSource.class) );
    }
}