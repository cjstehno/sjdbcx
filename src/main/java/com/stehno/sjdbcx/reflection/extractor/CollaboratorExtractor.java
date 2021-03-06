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

package com.stehno.sjdbcx.reflection.extractor;

import java.lang.reflect.Method;

/**
 * Provides a means of extracting a JDBC collaborator based on the method and its meta data.
 */
public interface CollaboratorExtractor<C> {

    /**
     * Extracts a collaborator of the specified type from the given method.
     *
     * @param method the method
     * @return the extracted and configured collaborator
     */
    C extract( final Method method );
}
