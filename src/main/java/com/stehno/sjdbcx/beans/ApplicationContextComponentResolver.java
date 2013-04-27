package com.stehno.sjdbcx.beans;

import com.stehno.sjdbcx.ComponentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * ComponentResolver which resolves its components from the Spring application context by bean name or type.
 */
public class ApplicationContextComponentResolver implements ComponentResolver {

    @Autowired private ApplicationContext applicationContext;

    @Override
    public <T> T resolve( final String name, final Class<? extends T> type ){
        return applicationContext.getBean( name, type );
    }

    @Override
    public <T> T resolve( final Class<? extends T> type ){
        return applicationContext.getBean( type );
    }
}
