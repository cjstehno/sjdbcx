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

package com.stehno.sjdbcx.support.extractor;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Extracts a collaborator of the specified type from the component resolver based on the "value" of the annotation, if it
 * exists. If no "value" is defined (empty string), the "type" property will be used to resolve the collaborator.
 *
 * If the extractor is executed against a method that does not contain the specified annotation, the default value configured
 * in this extractor will be returned (may be null) - note that this is NOT necessarily the same as the default value of the
 * annotation.
 *
 * If the component resolver cannot resolve the specified component, an exception may be thrown, depending on the component
 * resolver implementation.
 *
 * Annotations supported by this extractor must have a "String value() default """ and "Class<?> type()" attribute for
 * configuring the extractor key value.
 *
 * Instances of this class are thread-safe (stateless) and may be created once and reused.
 */
public class AnnotatedCollaborationExtractor<C> extends AbstractCollaboratorExtractor<C> {

    private static final String TYPE_ATTRIBUTE = "type";
    private final Class<? extends Annotation> annoType;
    private final C defaultExtractor;

    /**
     * Creates a CollaboratorExtractor based on the provide ComponentResolver.
     *
     * @param annoType the method annotation used to determine the component resolver key
     */
    public AnnotatedCollaborationExtractor( final Class<? extends Annotation> annoType ){
        this(annoType, null);
    }

    public AnnotatedCollaborationExtractor( final Class<? extends Annotation> annoType, final C defaultExtractor ){
        this.annoType = annoType;
        this.defaultExtractor = defaultExtractor;
    }

    @Override @SuppressWarnings("unchecked")
    public C extract( final Method method ){
        final Annotation annotation = AnnotationUtils.getAnnotation( method, annoType );
        if( annotation != null ){
            final String extractionKey = (String)AnnotationUtils.getValue( annotation );
            if( StringUtils.isEmpty( extractionKey ) ){
                return resolve( extractionKey );
            } else {
                return resolve( (Class<C>)AnnotationUtils.getValue( annotation, TYPE_ATTRIBUTE ) );
            }

        } else {
            return defaultExtractor;
        }
    }
}
