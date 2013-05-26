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

package com.stehno.sjdbcx.support;

import com.stehno.sjdbcx.annotation.JdbcRepository;
import com.stehno.sjdbcx.annotation.ResolveMethod;
import com.stehno.sjdbcx.annotation.Sql;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Properties;

/**
 * Used to resolve the SQL string from the annotation content or from the specified (or default) properties file.
 */
public class SqlResolver {

    private static final Logger log = LoggerFactory.getLogger( SqlResolver.class );
    private static final String PROP_FILE = "%s.sql.properties";

    /**
     * Uses the Sql and JdbcRepository annotations to resolve the SQL string to be used.
     *
     * @param prototype the repository prototype class
     * @param method the method to resolve SQL for
     * @return the String of SQL
     */
    public String resolve( final Class prototype, final Method method ){
        final JdbcRepository jdbcAnno = AnnotationUtils.findAnnotation( prototype, JdbcRepository.class );
        final Sql sqlAnno = AnnotationUtils.getAnnotation( method, Sql.class );

        if( shouldLookupSql( jdbcAnno, sqlAnno ) ){
            final String sqlKey = StringUtils.hasLength( sqlAnno.value() ) ? sqlAnno.value() : method.getName().toLowerCase();
            final Resource resource = determineResolverResource( prototype, jdbcAnno );

            final String sql = resolve( resource ).getProperty( sqlKey );

            log.debug("Resolved SQL ({}) for repository ({}) from resource ({}) property ({})", sql, prototype, resource, sqlKey );

            return sql;

        } else {
            return sqlAnno.value();
        }
    }

    private Resource determineResolverResource( final Class clazz, final JdbcRepository jdbcAnno ){
        final String resourceStr = jdbcAnno != null ? jdbcAnno.value() : null;
        final String fileName = clazz.getSimpleName().toLowerCase();
        final String resourcePath = StringUtils.isEmpty(resourceStr) ? String.format( PROP_FILE, fileName ) : resourceStr;
        final ClassPathResource resource = new ClassPathResource("/" + resourcePath);

        log.debug("Resolving SQL for ({}) from resource ({})", clazz, resource );

        return resource;
    }

    private boolean shouldLookupSql( final JdbcRepository jdbcAnno, final Sql sqlAnno ){
        ResolveMethod resolveMethod = sqlAnno.resolve();
        if( resolveMethod == ResolveMethod.DEFAULT ){
            if( jdbcAnno != null ){
                resolveMethod = jdbcAnno.defaultResolve() == ResolveMethod.DEFAULT ? ResolveMethod.LOOKUP : jdbcAnno.defaultResolve();
            } else {
                resolveMethod = ResolveMethod.LOOKUP;
            }
        }

        return resolveMethod == ResolveMethod.LOOKUP;
    }

    private Properties resolve( final Resource resource ){
        // FIXME: properties files need to be cached once loaded.

        final Properties properties = new Properties();

        try (final InputStream input = resource.getInputStream() ){
            properties.load( input );
        } catch( IOException e ){
            log.error("Unable to load property resource ({}): {}", resource, e.getMessage(), e);
        }

        log.trace("Loaded properties file ({}): {}", resource, properties);

        return properties;
    }
}
