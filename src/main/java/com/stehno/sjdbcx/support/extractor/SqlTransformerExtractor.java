package com.stehno.sjdbcx.support.extractor;

import com.stehno.sjdbcx.SqlTransformer;
import com.stehno.sjdbcx.annotation.Sql;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 *
 */
public class SqlTransformerExtractor extends AbstractCollaboratorExtractor<SqlTransformer> {

    public SqlTransformer extract( final Method method ){
        final Sql sqlAnno = AnnotationUtils.getAnnotation( method, Sql.class );
        return StringUtils.hasText(sqlAnno.transformer()) ? resolve( sqlAnno.transformer() ) : null;
    }
}
