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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * Abstract base class for CollaboratorExtractors using the ApplicationContext to find collaborators.
 */
public abstract class AbstractCollaboratorExtractor<C> implements CollaboratorExtractor<C> {

    @Autowired private ApplicationContext applicationContext;

    /**
     * Resolves a collaborator bean by type.
     *
     * @param type the bean type
     * @return the bean
     */
    protected final C resolve( final Class<C> type ){
        return applicationContext.getBean( type );
    }

    /**
     * Resolves a collaborator bean by name.
     *
     * @param beanName the bean name
     * @return the bean
     */
    @SuppressWarnings("unchecked")
    protected final C resolve( final String beanName ){
        return (C)applicationContext.getBean( beanName );
    }
}
