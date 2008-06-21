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
package org.codehaus.cake.cache.test.tck.service.memorystore;

import org.codehaus.cake.cache.policy.paging.LRUReplacementPolicy;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("unchecked")
public class MemoryStoreReplacementPolicyLRU extends AbstractCacheTCKTest {

    @Before
    public void setup() {
        conf.withMemoryStore().setPolicy(new LRUReplacementPolicy<Integer, String>());
        conf.withMemoryStore().setMaximumSize(5);
        init();
    }

    @Test
    public void testPut() {
        put(5);
        assertSize(5);
        assertPeek(M1);
        put(6, 6);
        assertSize(5);
        assertPeek(entry(M1, null));
        put(7, 9);
        assertPeek(entry(M2, null));
        assertPeek(entry(M3, null));
        assertPeek(entry(M4, null));
        assertPeek(M5);
        assertPeek(M6);
        assertPeek(M7);
        assertPeek(M8);
        assertPeek(M9);
    }

    @Test
    public void testTouch() {
        put(5);
        assertSize(5);
        assertPeek(M1);
        assertPeek(M2);
        get(M1);
        put(6, 6);
        assertSize(5);
        assertPeek(M1);
        assertPeek(entry(M2, null));
        get(M3, M4, M3, M1, M3);

        assertPeek(M5);
        put(17, 17);
        assertSize(5);
        assertPeek(entry(M5, null));

        assertPeek(M6);

        put(18, 18);
        assertSize(5);
        assertPeek(entry(M6, null));

        assertPeek(M4);
        put(18, 18);
        assertSize(5);
        assertPeek(M4);

        assertPeek(M4);
        put(19, 19);
        assertSize(5);
        assertPeek(entry(M4, null));

        assertPeek(M1);
        put(20, 20);
        assertPeek(entry(M1, null));

        assertPeek(M3);
        put(21, 21);
        assertPeek(entry(M3, null));
    }

    @Test
    public void testClear() {
        testPut();
        clear();
        testTouch();
        clear();
        testTouch();
        clear();
        testPut();
    }

}
