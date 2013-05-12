package com.stehno.sjdbcx.support;

import com.stehno.sjdbcx.ComponentResolver;
import com.stehno.sjdbcx.SqlTransformer;
import com.stehno.sjdbcx.annotation.Sql;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 *
 */
public class SqlTransformerExtractor {

    private final ComponentResolver componentResolver;

    SqlTransformerExtractor( final ComponentResolver  componentResolver ){
        this.componentResolver = componentResolver;
    }

    SqlTransformer extract( final Method method ){
        final Sql sqlAnno = AnnotationUtils.getAnnotation( method, Sql.class );
        return StringUtils.hasText(sqlAnno.transformer()) ? componentResolver.resolve( sqlAnno.transformer(), SqlTransformer.class ) : null;
    }
}
