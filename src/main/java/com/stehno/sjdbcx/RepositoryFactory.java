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

import com.stehno.sjdbcx.annotation.JdbcRepository;
import com.stehno.sjdbcx.annotation.Sql;
import com.stehno.sjdbcx.reflection.ReflectionImplementationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Factory used to create instances of the Repository.
 *
 * Defaults to the ReflectionImplementationProvider.
 */
public class RepositoryFactory<T> extends AbstractFactoryBean<T> implements ApplicationContextAware {

    private static final String MISSING_ANNOTATION_MSG = "The provided class (%s) is not annotated with the JdbcRepository annotation.";
    private static final String INVALID_CLASS_MSG = "The provided class must be abstract or an interface.";
    private static final String MISSING_TYPE_MSG = "An objectType must be specified.";
    private static final Logger log = LoggerFactory.getLogger( RepositoryFactory.class );
    private ApplicationContext applicationContext;
    private Class<? extends ImplementationProvider> implementationProviderClass;
    private Class<T> objectType;

    public void setImplementationProviderClass( final Class<? extends ImplementationProvider> implementationProviderClass ){
        this.implementationProviderClass = implementationProviderClass;
    }

    public void setObjectType( final Class<T> objectType ){
        this.objectType = objectType;
    }

    @Override
    public Class<?> getObjectType(){
        return objectType;
    }

    @Override
    public void setApplicationContext( final ApplicationContext applicationContext ) throws BeansException{
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull( objectType, MISSING_TYPE_MSG );
        Assert.isTrue( objectType.isAnnotationPresent( JdbcRepository.class ), String.format( MISSING_ANNOTATION_MSG, objectType ) );
        Assert.isTrue( objectType.isInterface() || Modifier.isAbstract( objectType.getModifiers() ), INVALID_CLASS_MSG );

        if( implementationProviderClass == null ){
            implementationProviderClass = ReflectionImplementationProvider.class;
        }

        super.afterPropertiesSet();
    }

    @Override
    protected T createInstance() throws Exception {
        final ImplementationProvider implProvider = implementationProviderClass.newInstance();
        implProvider.setPrototype( objectType );
        implProvider.init(applicationContext);

        for( final Method method: objectType.getMethods() ){
            if( method.isAnnotationPresent( Sql.class ) ){
                log.trace( "Processing method: {}", method );

                implProvider.implement(method);

            } else {
                if( objectType.isInterface() ){
                    log.warn("Found interface method ({}) without Sql annotation - ignoring.", method);
                } else {
                    log.trace("Found non-annotated method ({}) - ignoring", method);
                }
            }
        }

        return (T)implProvider.instantiate();
    }
}
