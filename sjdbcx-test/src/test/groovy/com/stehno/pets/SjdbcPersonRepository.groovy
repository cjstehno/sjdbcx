package com.stehno.pets
import com.stehno.sjdbcx.annotation.ParamMapper
import com.stehno.sjdbcx.annotation.Sql
/**
 * Created by cjstehno on 7/15/2014.
 */
abstract class SjdbcPersonRepository implements PersonRepository {

    @Override
    @Sql('insert into people (first_name,last_name) values (?,?)')
    @ParamMapper(type=PersonParamMapper)
    abstract void create( final Person person )
}

class PersonParamMapper implements com.stehno.sjdbcx.ParamMapper {

}