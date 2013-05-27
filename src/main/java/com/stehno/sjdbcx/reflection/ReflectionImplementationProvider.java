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

package com.stehno.sjdbcx.reflection;

import com.stehno.sjdbcx.ImplementationProvider;
import com.stehno.sjdbcx.annotation.Implemented;
import com.stehno.sjdbcx.annotation.ReplacementType;
import com.stehno.sjdbcx.annotation.Sql;
import com.stehno.sjdbcx.reflection.operation.*;
import com.stehno.sjdbcx.support.SqlResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * FIXME: document
 */
public class ReflectionImplementationProvider implements ImplementationProvider {

    private static final String INVALID_EXTEND_MSG = "Provided implementation must extend AbstractOperation.";
    private static final String MUST_IMPLEMENT_MSG = "Provided implementation must implement NamedOperation or IndexedOperation";
    private static final String ONLY_ONE_MSG = "Provided implementation must NOT implement NamedOperation AND IndexedOperation";
    private RepositoryInvocationHandler invocationHandler;
    private ApplicationContext applicationContext;
    private SqlResolver sqlResolver;
    private Class prototype;

    @Override
    public void setPrototype( final Class prototype ){
        this.prototype = prototype;
    }

    @Override
    public void init( final ApplicationContext applicationContext ){
        this.applicationContext = applicationContext;

        this.sqlResolver = applicationContext.getBean(SqlResolver.class);

        invocationHandler = new RepositoryInvocationHandler();
    }

    @Override
    public void implement( final Method method ) throws Exception {
        final OperationContext operationContext = applicationContext.getBean( OperationContext.class );
        final String sql = sqlResolver.resolve( prototype, method );

        final Operation operation;
        if( method.isAnnotationPresent(Implemented.class) ){
            operation = buildCustomOperation( method, sql, operationContext, method.getAnnotation(Implemented.class) );

        } else {
            operation = buildDefaultOperation( method, sql, operationContext, method.getAnnotation(Sql.class) );
        }

        invocationHandler.addOperation( method, operation );
    }

    private Operation buildDefaultOperation( final Method method, final String sql, final OperationContext operationContext, final Sql sqlAnno ){
        final Operation operation;
        final boolean indexed = sqlAnno.replacement() != ReplacementType.INDEXED;

        switch( sqlAnno.type() ){
            case UPDATE:
                if( indexed ){
                    operation = new NamedUpdateOperation(method, sql, operationContext);
                } else {
                    operation = new IndexedUpdateOperation(method, sql, operationContext);
                }
                break;

            case QUERY:
                if( indexed ){
                    operation = new NamedQueryOperation(method, sql, operationContext);
                } else {
                    operation = new IndexedQueryOperation(method, sql, operationContext);
                }
                break;

            case EXECUTE:
                // TODO: add more support for execute
                operation = new NamedExecuteOperation(method, sql, operationContext);
                break;

            default:
                throw new IllegalArgumentException("Invalid SQL statement type specified.");
        }

        return operation;
    }

    private Operation buildCustomOperation( final Method method, final String sql, final OperationContext operationContext, final Implemented implAnno ) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
        final Class<? extends Operation> operationClass;
        if( StringUtils.hasText( implAnno.value() ) ){
            operationClass = (Class<? extends Operation>)Class.forName( implAnno.value() );
        } else {
            operationClass = implAnno.type();
        }

        Assert.isAssignable( AbstractOperation.class, operationClass, INVALID_EXTEND_MSG );

        final boolean isNamed = NamedOperation.class.isAssignableFrom( operationClass );
        final boolean isIndexed = IndexedOperation.class.isAssignableFrom( operationClass );

        Assert.isTrue( isNamed || isIndexed, MUST_IMPLEMENT_MSG );
        Assert.isTrue( !( isNamed && isIndexed ), ONLY_ONE_MSG );

        return operationClass.getConstructor(Method.class,String.class,OperationContext.class).newInstance( method, sql, operationContext );
    }

    @Override
    public Object instantiate(){
        // FIXME: needs to handle abstract prototype classes too
        return Proxy.newProxyInstance( prototype.getClassLoader(), new Class[]{ prototype }, invocationHandler );
    }
}
