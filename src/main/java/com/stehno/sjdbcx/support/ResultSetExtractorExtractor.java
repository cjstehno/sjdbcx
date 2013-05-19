package com.stehno.sjdbcx.support;

import com.stehno.sjdbcx.ComponentResolver;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.lang.reflect.Method;

/**
 *
 */
public class ResultSetExtractorExtractor {

    private final ComponentResolver componentResolver;

    ResultSetExtractorExtractor( final ComponentResolver componentResolver ){
        this.componentResolver = componentResolver;
    }

    ResultSetExtractor extract( final Method method ){
        final com.stehno.sjdbcx.annotation.ResultSetExtractor extractor = AnnotationUtils.getAnnotation( method, com.stehno.sjdbcx.annotation.ResultSetExtractor.class );
        return extractor != null ? componentResolver.resolve( extractor.value(), ResultSetExtractor.class ) : null;
    }
}
