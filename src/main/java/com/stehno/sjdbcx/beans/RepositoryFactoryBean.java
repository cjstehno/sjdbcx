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

package com.stehno.sjdbcx.beans;

import com.stehno.sjdbcx.RepositoryFactory;
import org.springframework.beans.factory.config.AbstractFactoryBean;

/**
 *  Spring factory bean used to create configured proxied instances of the configured repository bean.
 */
public class RepositoryFactoryBean extends AbstractFactoryBean {

    private final Class type;
    private final RepositoryFactory factory;

    public RepositoryFactoryBean( final RepositoryFactory factory, final Class type ){
        this.factory = factory;
        this.type = type;
    }

    @Override
    public Class<?> getObjectType(){
        return type;
    }

    @Override
    protected Object createInstance() throws Exception {
        return factory.create( type );
    }
}
