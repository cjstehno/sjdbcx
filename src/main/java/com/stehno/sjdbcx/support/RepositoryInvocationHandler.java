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

import com.stehno.sjdbcx.ComponentResolver;
import com.stehno.sjdbcx.SqlSourceResolver;
import com.stehno.sjdbcx.annotation.JdbcDao;
import com.stehno.sjdbcx.annotation.ResolveMethod;
import com.stehno.sjdbcx.annotation.Sql;
import com.stehno.sjdbcx.annotation.SqlType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Invocation handler used to "implement" the repository interfaces.
 *
 * Each proxied instance will create its own instance of this class, they cannot be shared across multiple DAO instances
 * as it may be stateful.
 */
public class RepositoryInvocationHandler implements InvocationHandler {

    private static final Logger log = LoggerFactory.getLogger(RepositoryInvocationHandler.class);

    @Autowired private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired(required=false) private ComponentResolver componentResolver;
    @Autowired(required=false) private SqlSourceResolver sqlSourceResolver;

    // a locking strategy would probably be better than CHM, but let's start with this
    private final Map<Method,Operation> operationCache = new ConcurrentHashMap<>();

    @Override
    public Object invoke( final Object proxy, final Method method, final Object[] args ) throws Throwable {
        if( log.isTraceEnabled() ){
            log.trace( "Invoking {}({}) on a {}", method.getName(), Arrays.toString( args ), proxy.getClass() );
        }

        return findOperation( proxy, method ).execute( parseArguments( method, args ) );
    }

    private Operation findOperation( final Object proxy, final Method method ){
        Operation operation = operationCache.get( method );

        if( operation == null ){
            final Sql sqlAnno = AnnotationUtils.getAnnotation( method, Sql.class );
            Assert.notNull( sqlAnno, "No SQL annotation specified." );

            final OperationContext context = new OperationContext(
                method,
                extractSql( proxy.getClass(), method, sqlAnno ),
                componentResolver,
                namedParameterJdbcTemplate,
                new ParamMapperExtractor( componentResolver ).extract( method )
            );

            if( sqlAnno.type() == SqlType.UPDATE ){
                operation = new UpdateOperation( context );
            } else {
                operation = new QueryOperation( context );
            }

            operationCache.put( method, operation );
        }

        return operation;
    }

    private ParamArg[] parseArguments( final Method method, final Object[] args ){
        if( args != null ){
            final ParamArg[] paramArgs = new ParamArg[args.length];
            final Annotation[][] paramAnnos = method.getParameterAnnotations();

            for( int a=0; a<args.length; a++ ){
                paramArgs[a] = new ParamArg( args[a], paramAnnos[a] );
            }

            return paramArgs;

        } else {
            return null;
        }
    }

    private String extractSql( final Class clazz, final Method method, final Sql sqlAnno ){
        final JdbcDao jdbcAnno = AnnotationUtils.findAnnotation( clazz, JdbcDao.class );

        if( shouldLookupSql( jdbcAnno, sqlAnno ) )
            return sqlSourceResolver.resolve( determineResolverResource( clazz, jdbcAnno ) ).getSql(
                StringUtils.hasLength( sqlAnno.value() ) ? sqlAnno.value() : method.getName().toLowerCase()
            );
        else {
            return sqlAnno.value();
        }
    }

    private Resource determineResolverResource( final Class clazz, final JdbcDao jdbcAnno ){
        final String resourceStr = jdbcAnno != null ? jdbcAnno.value() : null;
        String resourcePath = StringUtils.isEmpty(resourceStr) ? clazz.getInterfaces()[0].getSimpleName().toLowerCase() + ".sql.properties" : resourceStr;

        return new ClassPathResource("/" + resourcePath );
    }

    private boolean shouldLookupSql( final JdbcDao jdbcAnno, final Sql sqlAnno ){
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
}
