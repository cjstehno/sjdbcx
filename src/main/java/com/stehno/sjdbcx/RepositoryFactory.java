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

import com.stehno.sjdbcx.support.RepositoryInvocationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.lang.reflect.Proxy;

public class RepositoryFactory {

    private static final String BEAN_NAME = "repositoryInvocationHandler";
    private static final Logger log = LoggerFactory.getLogger(RepositoryFactory.class);

    @Autowired private ApplicationContext applicationContext;

    @PostConstruct
    public void init(){
        log.debug("Initialized");
    }

    @SuppressWarnings("unchecked")
    public <T> T create( Class<T> repoInterface ){
        Assert.isTrue( repoInterface.isInterface(), "An interface must be specified." );

        log.trace( "Creating proxy for {}", repoInterface );

        return (T)Proxy.newProxyInstance(
            repoInterface.getClassLoader(),
            new Class[]{ repoInterface },
            applicationContext.getBean( BEAN_NAME, RepositoryInvocationHandler.class )
        );
    }
}
