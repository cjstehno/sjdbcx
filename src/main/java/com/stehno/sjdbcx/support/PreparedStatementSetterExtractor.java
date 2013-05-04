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
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.jdbc.core.PreparedStatementSetter;

import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * User: cjstehno
 * Date: 5/4/13
 * Time: 6:55 AM
 * To change this template use File | Settings | File Templates.
 */
public class PreparedStatementSetterExtractor {

    private final ComponentResolver componentResolver;

    PreparedStatementSetterExtractor( final ComponentResolver  componentResolver ){
        this.componentResolver = componentResolver;
    }

    PreparedStatementSetter extract( final Method method ){
        final com.stehno.sjdbcx.annotation.PreparedStatementSetter setter = AnnotationUtils.getAnnotation( method, com.stehno.sjdbcx.annotation.PreparedStatementSetter.class );
        return componentResolver.resolve( setter.value(), PreparedStatementSetter.class );
    }
}
