package com.stehno.sjdbcx.support;

import com.stehno.sjdbcx.IndexedParamMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementSetter;

/**
 * ... a update operation based on indexed param replacement
 */
class IndexedUpdateOperation extends AbstractOperation {
    // FIXME: remove duplication with named version

    private static final Logger log = LoggerFactory.getLogger( IndexedUpdateOperation.class );
    private final Class returnType;
    private final PreparedStatementSetter statementSetter;
    private final IndexedParamMapper paramMapper;

    IndexedUpdateOperation( final OperationContext context ){
        super( context );
        this.statementSetter = new PreparedStatementSetterExtractor( context.getComponentResolver() ).extract( context.getMethod() );
        this.paramMapper = new IndexedParamMapperExtractor( context.getComponentResolver() ).extract( context.getMethod() );
        this.returnType = context.getMethod().getReturnType();
    }

    @Override
    public Object execute( final ParamArg[] args ){
        final Object[] params = paramMapper.map( args );

        if( log.isTraceEnabled() ){
            log.trace("Executing-Update:" );
            log.trace(" - SQL: {}", getSql());
            log.trace(" - Params: {}", params);
        }

        final int result;
        if( statementSetter != null ){
            if( statementSetter instanceof ArgumentAware ){
                ((ArgumentAware)statementSetter).setArguments( params );
            }

            result = getJdbcTemplate().getJdbcOperations().update( getSql(), statementSetter );

        } else {
            result = getJdbcTemplate().getJdbcOperations().update( getSql(), params );
        }

        if( log.isTraceEnabled() ){
            log.trace(" - Result:  {}", result);
        }

        return mapResultsToReturn( result );
    }

    private Object mapResultsToReturn( final int result ){
        Object returnValue = null;
        if( returnType.equals( boolean.class ) ){
            returnValue = result > 0;
        } else if( returnType.equals( int.class ) ){
            returnValue = result;
        }
        return returnValue;
    }
}
