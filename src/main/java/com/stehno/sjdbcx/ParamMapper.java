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

import com.stehno.sjdbcx.support.AnnotatedArgument;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

/**
 * Defines how the method input parameters are mapped to the SQL statement replacement variables.
 * Implementations should be stateless and thread-safe.
 *
 * Note that implementations must also process the Param annotations if they want them to be supported.
 */
public interface ParamMapper {

    /**
     * Converts the input arguments to a SqlParameterSource object for a SQL statement using named
     * parameters.
     *
     * @param annotatedArguments
     * @return
     */
    SqlParameterSource mapByName( AnnotatedArgument[] annotatedArguments );

    /**
     * Converts the input arguments to a SqlParameterSource object for a SQL statement using indexed
     * parameters.
     *
     * @param annotatedArguments
     * @return
     */
    public Object[] mapByIndex( final AnnotatedArgument[] annotatedArguments );
}
