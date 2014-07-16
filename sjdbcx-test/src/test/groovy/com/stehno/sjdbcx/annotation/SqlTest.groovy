package com.stehno.sjdbcx.annotation


class SqlTest {
}

abstract class SqlAnnotator {

    @Sql('select count(*) from sometable')
    abstract int count()
}