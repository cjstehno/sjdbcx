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

package com.stehno.sjdbcx.reflection.operation;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unchecked")
public class OperationUtils {

    static <R> R convert( final List<?> list, final Class<R> desiredType ){
        // TODO: map support?
        if( desiredType == List.class ){
            return (R)list;

        } else if( desiredType == Set.class ){
            return (R)new LinkedHashSet<R>( (Collection)list );

        } else if( desiredType == Collection.class ){
            return (R)list;

        } else if( desiredType.isArray() ){
            return (R)list.toArray();

        } else {
            return (R)( list.isEmpty() ? null : list.get(0) );
        }
    }

    static <R> R convert( final int number, final Class<R> desiredType ){
        // TODO: support Object, String?
        if( desiredType == boolean.class || desiredType == Boolean.class ){
            return (R)Boolean.valueOf( number > 0 );

        } else if( desiredType == int.class || desiredType == Integer.class ){
            return (R)Integer.valueOf(number);

        } else if( desiredType == long.class || desiredType == Long.class ){
            return (R)Long.valueOf(number);

        } else if( desiredType == void.class | desiredType == Void.class ){
            return null;

        } else {
            throw new IllegalArgumentException( String.format("Return type of (int) cannot be converted to %s", desiredType) );
        }
    }
}
