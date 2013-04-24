package com.stehno.sjdbcx;

import com.stehno.sjdbcx.annotation.Param;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.lang.annotation.Annotation;

/**
 * The default param mapper which is used if no other param mapper is specified.
 * It may be sub-classed in order to augment its functionality.
 *
 * The Param annotations are processed by this implementation.
 */
public class DefaultParamMapper implements ParamMapper {

    @Override
    public SqlParameterSource map( final ParamArg[] paramArgs ){
        final MapSqlParameterSource parameterSource = new MapSqlParameterSource();

        for( final ParamArg arg: paramArgs ){
            final Annotation annot = arg.findAnnotation( Param.class );
            if( annot == null ){
                addBean(parameterSource, arg.getArgument());

            } else {
                parameterSource.addValue( ((Param)annot).value(), arg.getArgument() );
            }
        }

        return parameterSource;
    }

    private void addBean( final MapSqlParameterSource source, final Object beanObj ){
        final BeanPropertySqlParameterSource beanSource = new BeanPropertySqlParameterSource(beanObj);

        for( final String name: beanSource.getReadablePropertyNames() ){
            source.addValue( name, beanSource.getValue( name ) );
        }
    }
}