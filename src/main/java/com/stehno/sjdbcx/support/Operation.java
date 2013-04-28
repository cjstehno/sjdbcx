package com.stehno.sjdbcx.support;

/**
 * Created with IntelliJ IDEA.
 * User: cjstehno
 * Date: 4/27/13
 * Time: 11:04 AM
 * To change this template use File | Settings | File Templates.
 */
public interface Operation {

    Object execute( final ParamArg[] args );
}
