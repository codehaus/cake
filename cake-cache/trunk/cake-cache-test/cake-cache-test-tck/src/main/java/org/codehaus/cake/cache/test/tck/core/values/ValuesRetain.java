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
package org.codehaus.cake.cache.test.tck.core.values;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

/**
 * 
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public class ValuesRetain extends AbstractCacheTCKTest {

    /**
     * {@link Set#clear()} lazy starts the cache.
     */
    @Test
    public void retainAllLazyStart() {
        c = newCache(0);
        assertFalse(c.isStarted());
        c.keySet().retainAll(Collections.singleton(M1.getValue()));
        checkLazystart();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void retainAll() {
        c = newCache(1);
        c.values().retainAll(Collections.singleton(M1.getValue()));
        assertEquals(1, c.size());

        c.values().retainAll(Collections.singleton(M2.getValue()));
        assertEquals(0, c.size());
        c = newCache(5);
        c.values().retainAll(Arrays.asList(M1.getValue(), 1, M3.getValue(), 2, M5.getValue()));
        assertEquals(3, c.size());
        assertTrue(c.values().contains(M1.getValue()) && c.values().contains(M3.getValue())
                && c.values().contains(M5.getValue()));

    }

    @Test(expected = NullPointerException.class)
    public void retainAllNPE() {
        newCache(5).values().retainAll(null);
    }
}
