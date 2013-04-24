package com.stehno.sjdbcx.beans;

import com.stehno.sjdbcx.ParamMapper;
import com.stehno.sjdbcx.ParamMapperResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * Resolves ParamMappers from application context by bean name.
 */
public class BeanNameParamMapperResolver implements ParamMapperResolver {

    @Autowired private ApplicationContext applicationContext;

    @Override
    public ParamMapper resolve( final String name ){
        return applicationContext.getBean( name, ParamMapper.class );
    }
}
