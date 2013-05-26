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

import com.stehno.sjdbcx.reflection.operation.Operation;
import com.stehno.sjdbcx.support.AnnotatedArgument;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * FIXME: doc
 */
public class RepositoryInvocationHandler implements InvocationHandler {

    private final Map<Method,Operation> operations = new HashMap<>();

    void addOperation( final Method method, final Operation operation ){
        operations.put( method, operation );
    }

    @Override
    public Object invoke( final Object self, final Method method, final Object[] args ) throws Throwable{
        return operations.get( method ).execute( parseArguments( method, args ) );
    }

    private AnnotatedArgument[] parseArguments( final Method method, final Object[] args ){
        if( args != null ){
            final AnnotatedArgument[] annotatedArguments = new AnnotatedArgument[args.length];
            final Annotation[][] paramAnnos = method.getParameterAnnotations();

            for( int a=0; a<args.length; a++ ){
                annotatedArguments[a] = new AnnotatedArgument( args[a], paramAnnos[a] );
            }

            return annotatedArguments;

        } else {
            return null;
        }
    }
}
