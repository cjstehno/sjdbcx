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

public class Person {

    private Long id;
    private String firstName;
    private String lastName;
    private int age;

    public Person(){
        super();
    }

    public Person( final Long id, final String firstName, final String lastName, final int age){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    /**
     * ... does not include ID.
     * @param o
     * @return
     */
    @Override
    public boolean equals( final Object o ){
        if( this == o ) return true;
        if( o == null || getClass() != o.getClass() ) return false;

        final Person person = (Person)o;

        if( age != person.age ) return false;
        if( !firstName.equals( person.firstName ) ) return false;
        if( !lastName.equals( person.lastName ) ) return false;

        return true;
    }

    @Override
    public int hashCode(){
        int result = firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + age;
        return result;
    }
}
