package com.stehno.sjdbcx.support;

import com.stehno.sjdbcx.ComponentResolver;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.lang.reflect.Method;
import java.util.List;

/**
 *
 */
public class ResultSetExtractorExtractor {

    private final ComponentResolver componentResolver;

    ResultSetExtractorExtractor( final ComponentResolver componentResolver ){
        this.componentResolver = componentResolver;
    }

    @SuppressWarnings("unchecked")
    ResultSetExtractor<? extends List> extract( final Method method ){
        final com.stehno.sjdbcx.annotation.ResultSetExtractor extractor = AnnotationUtils.getAnnotation( method, com.stehno.sjdbcx.annotation.ResultSetExtractor.class );
        return extractor != null ? (ResultSetExtractor<? extends List>)componentResolver.resolve( extractor.value(), ResultSetExtractor.class ) : null;
    }
}
