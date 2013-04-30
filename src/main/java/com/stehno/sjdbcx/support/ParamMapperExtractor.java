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

import com.stehno.sjdbcx.ComponentResolver;
import com.stehno.sjdbcx.ParamMapper;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;

/**
 * FIXME: document
 */
class ParamMapperExtractor {

    private final ParamMapper defaultParamMapper = new DefaultParamMapper();
    private final ComponentResolver componentResolver;

    ParamMapperExtractor( final ComponentResolver  componentResolver ){
        this.componentResolver = componentResolver;
    }

    ParamMapper extract( final Method method ){
        final com.stehno.sjdbcx.annotation.ParamMapper mapper = AnnotationUtils.getAnnotation( method, com.stehno.sjdbcx.annotation.ParamMapper.class );
        return mapper == null ? defaultParamMapper : componentResolver.resolve( mapper.value(), ParamMapper.class );
    }
}
