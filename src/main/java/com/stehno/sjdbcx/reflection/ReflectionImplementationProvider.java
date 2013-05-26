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
import com.stehno.sjdbcx.support.SqlResolver;
import com.stehno.sjdbcx.annotation.ReplacementType;
import com.stehno.sjdbcx.annotation.Sql;
import com.stehno.sjdbcx.reflection.operation.ExecuteOperation;
import com.stehno.sjdbcx.reflection.operation.IndexedUpdateOperation;
import com.stehno.sjdbcx.reflection.operation.OperationContext;
import com.stehno.sjdbcx.reflection.operation.QueryOperation;
import com.stehno.sjdbcx.reflection.operation.UpdateOperation;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * FIXME: document
 */
public class ReflectionImplementationProvider implements ImplementationProvider {

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
        final Sql sqlAnno = AnnotationUtils.getAnnotation( method, Sql.class );
        final String sql = sqlResolver.resolve( prototype, method );

        final OperationContext operationContext = applicationContext.getBean( OperationContext.class );

        switch( sqlAnno.type() ){
            case UPDATE:
                if( sqlAnno.replacement() != ReplacementType.INDEXED ){
                    invocationHandler.addOperation( method, new UpdateOperation(method, sql, operationContext) );
                } else {
                    invocationHandler.addOperation( method, new IndexedUpdateOperation(method, sql, operationContext) );
                }
                break;

            case QUERY:
                invocationHandler.addOperation( method, new QueryOperation(method, sql, operationContext) );
                break;

            case EXECUTE:
                invocationHandler.addOperation( method, new ExecuteOperation(method, sql, operationContext) );
                break;

            default:
                throw new IllegalArgumentException("Invalid SQL statement type specified.");
        }
    }

    @Override
    public Object instantiate(){
        // FIXME: needs to handle abstract prototype classes too
        return Proxy.newProxyInstance( prototype.getClassLoader(), new Class[]{ prototype }, invocationHandler );
    }
}
