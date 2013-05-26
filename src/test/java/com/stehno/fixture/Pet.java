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

public class Pet {

    public static enum Species { CAT, DOG, BIRD };

    private Long id;
    private String name;
    private Species species;

    public Pet(){}

    public Pet(final Long id, final String name, final Species species){
        this.id = id;
        this.name = name;
        this.species = species;
    }

    public Long getId(){
        return id;
    }

    public void setId( final Long id ){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName( final String name ){
        this.name = name;
    }

    public Species getSpecies(){
        return species;
    }

    public void setSpecies( final Species species ){
        this.species = species;
    }

    @Override
    public boolean equals( final Object o ){
        if( this == o ) return true;
        if( o == null || getClass() != o.getClass() ) return false;

        final Pet pet = (Pet)o;

        if( !id.equals( pet.id ) ) return false;
        if( !name.equals( pet.name ) ) return false;
        if( species != pet.species ) return false;

        return true;
    }

    @Override
    public int hashCode(){
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + species.hashCode();
        return result;
    }
}
