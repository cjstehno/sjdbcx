package com.stehno.sjdbcx;

import java.lang.annotation.Annotation;
import java.util.Arrays;

/**
 * A single annotated input parameter object.
 */
public class ParamArg {

    private final Object argument;
    private final Annotation[] annotations;

    public ParamArg( final Object argument, final Annotation[] annotations ){
        this.argument = argument;
        this.annotations = annotations;
    }

    public Object getArgument(){
        return argument;
    }

    public Iterable<Annotation> annotations(){
        return Arrays.asList(annotations);
    }

    public Annotation findAnnotation( final Class annot ){
        for( final Annotation anno: annotations ){
            if( annot.isInstance( anno ) ){
                return anno;
            }
        }
        return null;
    }
}
