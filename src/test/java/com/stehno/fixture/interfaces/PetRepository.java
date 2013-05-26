/*
 * Copyright (c) 2013 Christopher J. Stehno
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.stehno.fixture.interfaces;

import com.stehno.fixture.Pet;
import com.stehno.fixture.PetParamMapper;
import com.stehno.sjdbcx.annotation.*;

import java.util.List;

@JdbcRepository
public interface PetRepository {

    @Sql("count.all")
    long count();

    @Sql("count.species")
    long count( @Param("species") Pet.Species species );

    @Sql(value="select id,name,species from pets where id=:id", resolve=ResolveMethod.SQL)
    Pet fetch( @Param("id") long id );

    @Sql("list.all")
    List<Pet> list();

    @Sql("list.species")
    List<Pet> list( @Param("species") Pet.Species species );

    @Sql(type=SqlType.UPDATE) @ParamMapper(type=PetParamMapper.class)
    void create( Pet pet );

    @Sql(type=SqlType.UPDATE) @ParamMapper(type=PetParamMapper.class)
    boolean update( Pet pet );

    @Sql(type=SqlType.UPDATE)
    boolean delete( @Param("id") long id );
}
