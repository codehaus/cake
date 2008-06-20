/*
 * Copyright 2008 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://cake.codehaus.org/LICENSE
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.codehaus.cake.cache.test.keys;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: KeyValues.java 415 2007-11-09 08:25:23Z kasper $
 */
public class KeyValues {

    private static final long randomSeed = 2342723;

    private static final int MAXIMUM_ELEMENTS = 100000;

    private static final String[] keys = new String[100000];

    private static final Integer[] values;

    static {
        Random rnd = new Random(randomSeed);
        ArrayList<Integer> list = new ArrayList<Integer>(MAXIMUM_ELEMENTS);

        for (int i = 0; i < MAXIMUM_ELEMENTS; i++) {
            list.add(i);
        }
        Collections.shuffle(list, rnd);

        values = list.toArray(new Integer[MAXIMUM_ELEMENTS]);
        for (int i = 0; i < values.length; i++) {
            keys[i] = Integer.toHexString(values[i]);
        }

    }

    public static Integer getInt(int i) {
        return values[i];
    }

    public static Integer getIntMod(long i) {
        return values[(int) (Math.abs(i % MAXIMUM_ELEMENTS))] ;
    }

    public static String getString(int i) {
        return keys[i];
    }
    
    public static String getStringMod(int i) {
        return keys[(Math.abs(i % MAXIMUM_ELEMENTS))] ;
    }
}
