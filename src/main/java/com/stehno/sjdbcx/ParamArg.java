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

import java.lang.annotation.Annotation;
import java.util.Arrays;

/**
 * A single annotated input parameter object.
 */
public class ParamArg {

    private final Object argument;
    private final Annotation[] annotations;

    public ParamArg( final Object argument, final Annotation[] annotations ){
        this.argument = argument;
        this.annotations = annotations;
    }

    public Object getArgument(){
        return argument;
    }

    public Iterable<Annotation> annotations(){
        return Arrays.asList(annotations);
    }

    public Annotation findAnnotation( final Class annot ){
        for( final Annotation anno: annotations ){
            if( annot.isInstance( anno ) ){
                return anno;
            }
        }
        return null;
    }
}
