package com.stehno.sjdbcx.reflection.operation;

import com.stehno.sjdbcx.support.AnnotatedArgument;

/**
 * The operative injected implementation of a JDBC repository method.
 */
public interface Operation {
    // TODO: it would be nice to parametrise this to a hard return type but I am not sure that jvm will play nice.

    /**
     * Executes the Operation.
     *
     * @param args the method arguments and any annotations
     * @return the return value (if any for the operation)
     */
    Object execute( final AnnotatedArgument[] args );
}
