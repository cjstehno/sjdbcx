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

import com.stehno.sjdbcx.IndexedParamMapper;

/**
 *  Default implementation of the IndexedParamMapper. It simply maps the original argument values to the
 *  parameter array.
 */
public class DefaultIndexedParamMapper implements IndexedParamMapper {

    @Override
    public Object[] map( final ParamArg[] paramArgs ){
        final Object[] args = new Object[paramArgs.length];
        for( int i=0; i<paramArgs.length; i++ ){
            args[i] = paramArgs[i].getArgument();
        }
        return args;
    }
}
