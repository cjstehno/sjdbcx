
create table pets (
  id int primary key,
  name varchar(20) not null,
  species varchar(10) not null
);

create table people (
  id int primary key,
  first_name varchar(20) not null,
  last_name varchar(20) not null,
  age int
);