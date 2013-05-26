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

package com.stehno.fixture;

import com.stehno.sjdbcx.ParamMapper;
import com.stehno.sjdbcx.support.AnnotatedArgument;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public class PetParamMapper implements ParamMapper {

    @Override
    public SqlParameterSource map( final AnnotatedArgument[] args ){
        final Pet pet = ( Pet )args[0].getValue();

        final MapSqlParameterSource parameterSource = new MapSqlParameterSource();

        parameterSource.addValue( "id", pet.getId() );
        parameterSource.addValue( "name", pet.getName() );
        parameterSource.addValue( "species", pet.getSpecies().name() );

        return parameterSource;
    }
}
