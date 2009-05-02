/*
 * Copyright 2008, 2009 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package org.codehaus.cake.cache.test.tck.asmap.keyset;

import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.test.tck.asmap.AbstractAsMapTCKTest;
import org.junit.Test;

public class KeySetToString extends AbstractAsMapTCKTest {
    /**
     * toString works.
     */
    @Test
    public void toStringKeySet() {
        assertEquals("[]", newCache().asMap().keySet().toString());
        String str = newCache(5).asMap().keySet().toString();
        assertTrue(str.startsWith("["));
        assertTrue(str.endsWith("]"));
        HashSet<Integer> hs = new HashSet<Integer>();
        String[] ss = str.replace("[", "").replace("]", "").replace(" ", "").split(",");
        for (String s : ss) {
            hs.add(Integer.parseInt(s));
        }
        assertEquals(new HashSet(Arrays.asList(1, 2, 3, 4, 5)), hs);
    }

    /**
     * toString lazy starts the cache.
     */
    @Test
    public void toStringLazyStart() {
        c = newCache(0);
        assertFalse(c.isStarted());
        asMap().keySet().toString();
        checkLazystart();
    }

    /**
     * {@link Cache#isEmpty()} should not fail when cache is shutdown.
     * 
     * @throws InterruptedException
     *             was interrupted
     */
    @Test
    public void toStringShutdown() throws InterruptedException {
        c = newCache(5);
        assertTrue(c.isStarted());
        c.shutdown();

        // should not fail, but result is undefined until terminated
        asMap().keySet().toString();

        assertTrue(c.awaitTermination(1, TimeUnit.SECONDS));

        String s = asMap().keySet().toString();;
        assertEquals("[]", s);// cache should be empty
    }
}
