package com.stehno.sjdbcx.reflection.operation;

import com.stehno.sjdbcx.ParamMapper;
import com.stehno.sjdbcx.support.AnnotatedArgument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.lang.reflect.Method;

import static com.stehno.sjdbcx.reflection.operation.OperationUtils.convert;

/**
 *  Reflective implementation of an update method. This class is for internal use only.
 */
public class UpdateOperation extends AbstractOperation {

    private static final Logger log = LoggerFactory.getLogger(UpdateOperation.class);
    private final ParamMapper paramMapper;
    private final Class returnType;

    public UpdateOperation( final Method method, final String sql, final OperationContext context ){
        super(method, sql, context);

        this.paramMapper = context.extract( ParamMapper.class, method );
        this.returnType = method.getReturnType();
    }

    public Object execute( final AnnotatedArgument[] args ){
        final SqlParameterSource parameterSource = paramMapper.map( args );

        final String sql = getSql( args );

        if( log.isTraceEnabled() ){
            log.trace("Executing-Update:" );
            log.trace(" - SQL: {}", sql);
            log.trace(" - Params: {}", parameterSource);
        }

        final int result = getNamedParameterJdbcTemplate().update( sql, parameterSource );

        if( log.isTraceEnabled() ){
            log.trace(" - Result:  {}", result);
        }

        return convert( result, returnType );
    }
}
