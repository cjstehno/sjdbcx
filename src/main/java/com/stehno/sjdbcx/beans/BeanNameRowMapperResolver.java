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

import com.stehno.sjdbcx.RowMapperResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.RowMapper;

/**
 * RowMapper resolver that uses the provided name to look up mappers configured as beans in the application context.
 */
public class BeanNameRowMapperResolver implements RowMapperResolver {

    @Autowired private ApplicationContext applicationContext;

    @Override @SuppressWarnings("unchecked")
    public <T> RowMapper<T> resolveRowMapper( final String name ){
        return applicationContext.getBean( name, RowMapper.class );
    }
}
