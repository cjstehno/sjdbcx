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

import com.stehno.sjdbcx.SqlSource;
import com.stehno.sjdbcx.SqlSourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * A SqlSourceResolver which resolves SQL strings from a properties file.
 *
 * @see org.springframework.beans.factory.config.PropertiesFactoryBean
 */
public class PropertiesSqlSourceResolver implements SqlSourceResolver {

    private static final Logger log = LoggerFactory.getLogger(PropertiesSqlSourceResolver.class);

    /**
     */
    @Override
    public SqlSource resolve( final Resource resource ){
        final Properties properties = new Properties();

        try (final InputStream input = resource.getInputStream() ){
            properties.load( input );
        } catch( IOException e ){
            log.error("Unable to load property resource ({}): {}", resource, e.getMessage(), e);
        }

        return new SqlSource() {
            @Override
            public String getSql( final String key ){
                return properties.getProperty( key );
            }
        };
    }
}
