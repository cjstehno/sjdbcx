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

package com.stehno.sjdbcx.annotation;

import java.lang.annotation.*;

/**
 *  Used to annotate a method with a SQL statement.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Sql {

    /**
     * The string of SQL with replacement parameters (named, not indexed). If not specified the method name will be used
     * in the resolver. If the resolve method it ResolveMethod.SQL this is not a valid combination.
     * @return
     */
    String value() default "";

    /**
     * Value used to retrieve the actual SQL string from a configured SqlSourceResolver.
     *
     * @return
     */
    ResolveMethod resolve() default ResolveMethod.DEFAULT;

    /**
     * The type of SQL operation, defaults to QUERY.
     *
     * @return
     */
    SqlType type() default SqlType.QUERY;

    /**
     * The type of variable replacement to be used in the SQL.
     *
     * @return
     */
    ReplacementType replacement() default ReplacementType.NAMED;

    /**
     * Specify a SqlTransformer to be used (if any).
     *
     * @return
     */
    String transformer() default "";
}
