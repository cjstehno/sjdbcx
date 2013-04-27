# sjdbcx

> This is VERY raw alpha code

A light extension of the Spring JDBC support classes to add annotation support to ease DAO creation and management.

The idea is to provide the SQL and mapper information as annotations to a Repository interface and then let a proxy do
all Spring JDBC work behind the scenes.

An example (pulled from the unit tests) is shown below:

```java
public interface PersonRepository {

    static final String SQL = "insert into people (first_name,last_name,age) values (:firstName,:lastName,:age)";

    @Sql(value=SQL, type=Sql.Type.UPDATE)
    void create( Person person );

    @Sql("select id,first_name,last_name,age from people order by last_name,first_name,age")
    List<Person> list();

    @Sql("select id,first_name,last_name,age from people where id=:id")
    Person fetch( @Param("id") long personId );

    @Sql("select id,first_name,last_name,age from people where age >= :min and age <= :max order by last_name,first_name,age")
    List<Person> findByAgeRange( @Param("min") int ageMin, @Param("max") int ageMax );

    @Sql(value="update people set first_name=:firstName,last_name=:lastName,age=:age where id=:id", type=Sql.Type.UPDATE)
    void update( Person person );

    @Sql(value="delete from people where id=:id", type=Sql.Type.UPDATE)
    boolean delete( @Param("id") long personId );

    @Sql(value="sql.findByName", lookup=true)
    List<Person> findByName( @Param("name") final String name );

    @Sql("select count(*) from people") @RowMapper("singleColumnRowMapper")
    long countPeople();
}
```

I realize there are other JDBC mappers available; however, while Sjdbcx draws inspiration from them, it also attempts to
provide a more lightweight spring-centric approach.

This library should be viewed as an extension to Spring (unofficial), not a stand-alone mapping utility.

[![Build Status](https://drone.io/github.com/cjstehno/sjdbcx/status.png)](https://drone.io/github.com/cjstehno/sjdbcx/latest)
