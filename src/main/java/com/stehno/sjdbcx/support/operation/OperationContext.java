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

package com.stehno.sjdbcx.support.operation;

import com.stehno.sjdbcx.support.extractor.CollaboratorExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 *
 */
public class OperationContext {

    @Autowired private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final Map<Class,CollaboratorExtractor> extractors = new IdentityHashMap<>();

    public void registerExtractor( Class forType, CollaboratorExtractor extractor ){
        extractors.put( forType, extractor );
    }

    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate(){
        return namedParameterJdbcTemplate;
    }

    @SuppressWarnings("unchecked")
    public <E> CollaboratorExtractor<E> extractorFor( Class<E> type ){
        return extractors.get( type );
    }
}
