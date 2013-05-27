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

package com.stehno.sjdbcx.reflection;

import com.stehno.fixture.Pet;
import com.stehno.fixture.interfaces.PetRepository;
import com.stehno.sjdbcx.DatabaseTestExecutionListener;
import com.stehno.sjdbcx.TestConfig;
import com.stehno.sjdbcx.config.SjdbcxConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.util.List;

import static junit.framework.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={TestConfig.class, SjdbcxConfiguration.class, ReflectionConfig.class})
@TestExecutionListeners({
    DependencyInjectionTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    DatabaseTestExecutionListener.class
})
public class ReflectionPetRepositoryTest {

    private static final Pet SPOT = new Pet( 100L, "Spot", Pet.Species.DOG );
    private static final Pet ARIEL = new Pet( 200L, "Ariel", Pet.Species.BIRD );
    private static final Pet SHADOW = new Pet( 300L, "Shadow", Pet.Species.CAT );
    private static final Pet TOM = new Pet( 400L, "Tom", Pet.Species.CAT );

    @Autowired PetRepository petRepository;

    @Before
    public void before(){
        addSomePets();
    }

    @Test
    public void counting(){
        assertEquals( 1, petRepository.count(Pet.Species.BIRD) );
        assertEquals( 2, petRepository.count(Pet.Species.CAT) );
        assertEquals( 1, petRepository.count(Pet.Species.DOG) );
    }

    @Test
    public void deleting(){
        petRepository.delete( SHADOW.getId() );
        petRepository.delete( SPOT.getId() );

        assertEquals( 1, petRepository.count( Pet.Species.BIRD ) );
        assertEquals( 1, petRepository.count(Pet.Species.CAT) );
        assertEquals( 0, petRepository.count(Pet.Species.DOG) );
    }

    @Test
    public void listAll(){
        final List<Pet> pets = petRepository.list();

        assertEquals( 4, pets.size() );

        assertTrue( pets.contains( SPOT ) );
        assertTrue( pets.contains( ARIEL ) );
        assertTrue( pets.contains( SHADOW ) );
        assertTrue( pets.contains( TOM ) );
    }

    @Test
    public void listBySpecies(){
        List<Pet> pets = petRepository.list( Pet.Species.CAT );
        assertEquals( 2, pets.size() );
        assertTrue( pets.contains( SHADOW ) );
        assertTrue( pets.contains( TOM ) );

        pets = petRepository.list( Pet.Species.DOG );
        assertEquals( 1, pets.size() );
        assertTrue( pets.contains( SPOT ) );

        pets = petRepository.list( Pet.Species.BIRD );
        assertEquals( 1, pets.size() );
        assertTrue( pets.contains( ARIEL) );
    }

    @Test
    public void fetch(){
        assertEquals( SPOT, petRepository.fetch(SPOT.getId()) );
        assertEquals( ARIEL, petRepository.fetch(ARIEL.getId()) );
        assertEquals( SHADOW, petRepository.fetch(SHADOW.getId()) );
        assertEquals( TOM, petRepository.fetch(TOM.getId()) );
    }

    @Test
    public void update(){
        final String newName = "Squeek";

        assertTrue( petRepository.update( new Pet(SHADOW.getId(), newName, SHADOW.getSpecies()) ) );

        final Pet pet = petRepository.fetch( SHADOW.getId() );
        assertFalse( SHADOW.equals(pet) );
        assertEquals( newName, pet.getName() );
    }

    @Test
    public void findWhereOne(){
        final List<Pet> list = petRepository.findWhere("S", Pet.Species.CAT);

        assertEquals( 1, list.size() );
        assertTrue( list.contains(SHADOW) );
    }

    @Test
    public void findWhereTwo(){
        final List<Pet> list = petRepository.findWhere("o", Pet.Species.CAT);

        assertEquals( 2, list.size() );
        assertTrue( list.contains(SHADOW) );
        assertTrue( list.contains(TOM) );
    }

    private void addSomePets(){
        petRepository.create( SPOT );
        petRepository.create( ARIEL );
        petRepository.create( SHADOW );
        petRepository.create( TOM );

        assertEquals( 4, petRepository.count() );
    }
}
