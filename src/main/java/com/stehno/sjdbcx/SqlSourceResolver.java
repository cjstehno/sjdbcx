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

/**
 * Allows SQL strings to be generated/resolved separate from the code and annotations themselves.
 *
 * @see com.stehno.sjdbcx.annotation.Sql
 */
public interface SqlSourceResolver {

    /**
     * Resolves the SQL string for the given key value.
     *
     * @param key the key used to find the SQL string
     * @return the SQL string
     */
    String resolve( final String key );
}
