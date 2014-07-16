package com.stehno.sjdbcx.transformer

import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation

/**
 * Created by cjstehno on 7/15/2014.
 */
@GroovyASTTransformation(phase = CompilePhase.CANONICALIZATION)
class SqlTransformer implements ASTTransformation {

    @Override
    void visit(final ASTNode[] nodes, final SourceUnit source){

    }
}
