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

package com.stehno.sjdbcx.reflection.operation;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OperationUtilsTest {

    private static final List<String> STRING_LIST = Arrays.asList("one", "two", "three");

    @Test
    public void convertIntToVoid(){
        // just ensure no error
        OperationUtils.convert( 21, Void.class );
    }

    @Test(expected = IllegalArgumentException.class)
    public void convertIntToInvalid(){
        // just ensure no error
        OperationUtils.convert( 21, String.class );
    }

    @Test
    public void convertIntToInt(){
        Integer val = OperationUtils.convert( 21, Integer.class );
        assertEquals( Integer.valueOf(21), val );

        int num = OperationUtils.convert( 21, int.class );
        assertEquals( 21, num );
    }

    @Test
    public void convertIntToLong(){
        Long val = OperationUtils.convert( 21, Long.class );
        assertEquals( Long.valueOf(21), val );

        long num = OperationUtils.convert( 21, long.class );
        assertEquals( 21, num );
    }

    @Test
    public void convertIntToBoolean(){
        Boolean val = OperationUtils.convert( 21, Boolean.class );
        assertEquals( Boolean.TRUE, val );

        boolean num = OperationUtils.convert( 21, boolean.class );
        assertEquals( true, num );

        val = OperationUtils.convert( 0, Boolean.class );
        assertEquals( Boolean.FALSE, val );

        num = OperationUtils.convert( 0, boolean.class );
        assertEquals( false, num );
    }

    @Test
    public void convertListToList(){
        List<String> res = OperationUtils.convert(STRING_LIST, List.class );
        assertTrue( res.containsAll( STRING_LIST ) );
    }

    @Test
    public void convertListToCollection(){
        Collection<String> res = OperationUtils.convert(STRING_LIST, Collection.class );
        assertTrue( res.containsAll( STRING_LIST ) );
    }

    @Test
    public void convertListToSet(){
        Set<String> res = OperationUtils.convert(STRING_LIST, Set.class );
        assertTrue( res.containsAll( STRING_LIST ) );
    }

    @Test
    public void convertListToSingle(){
        Object res = OperationUtils.convert(STRING_LIST, Object.class );
        assertEquals( STRING_LIST.get(0), res );
    }

    @Test
    public void convertListToArray(){
        String[] res = OperationUtils.convert(STRING_LIST, String[].class );
        assertEquals( 3, res.length );
        assertEquals( STRING_LIST.get(0), res[0] );
        assertEquals( STRING_LIST.get(1), res[1] );
        assertEquals( STRING_LIST.get(2), res[2] );
    }
}
