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

import com.stehno.sjdbcx.SqlSourceResolver;
import org.springframework.util.Assert;

import java.util.Properties;

/**
 * A SqlSourceResolver which resolves SQL strings from a properties file.
 *
 * @see org.springframework.beans.factory.config.PropertiesFactoryBean
 */
public class PropertiesSqlSourceResolver implements SqlSourceResolver {

    private Properties properties;

    public void setProperties( final Properties properties ){
        this.properties = properties;
    }

    /**
     * Resolves SQL strings from the configured Properties instance. The keys will be mapped directly to property keys.
     *
     * If no value is found for the key, an exception will be thrown.
     *
     * @param key the property key for the SQL string
     * @return the SQL string
     * @throws IllegalArgumentException if there is no mapping for the specified key
     */
    @Override
    public String resolve( final String key ){
        final String sql = properties.getProperty(key);

        Assert.notNull( sql, String.format( "No SQL specified for mapping (%s).", key ) );

        return sql;
    }
}
