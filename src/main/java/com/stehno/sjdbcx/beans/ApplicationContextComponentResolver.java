package com.stehno.sjdbcx.beans;

import com.stehno.sjdbcx.ComponentResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;

/**
 * ComponentResolver which resolves its components from the Spring application context by bean name or type.
 */
public class ApplicationContextComponentResolver implements ComponentResolver {

    private static final Logger log = LoggerFactory.getLogger( ApplicationContextComponentResolver.class );
    @Autowired private ApplicationContext applicationContext;

    @PostConstruct
    public void init(){
        log.debug("Initialized");
    }

    @Override
    public <T> T resolve( final String name, final Class<? extends T> type ){
        return applicationContext.getBean( name, type );
    }

    @Override
    public <T> T resolve( final Class<? extends T> type ){
        return applicationContext.getBean( type );
    }
}
