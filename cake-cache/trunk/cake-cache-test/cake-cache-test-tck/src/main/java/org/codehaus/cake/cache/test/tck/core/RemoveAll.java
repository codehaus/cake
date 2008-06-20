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
package org.codehaus.cake.cache.test.tck.core;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.codehaus.cake.test.util.CollectionTestUtil;
import org.junit.Test;

public class RemoveAll extends AbstractCacheTCKTest {
    // TODO: remove, removeAll

    @Test(expected = NullPointerException.class)
    public void removeAllNPE1() {
        init();
        c.removeAll(null);
    }

    @Test(expected = NullPointerException.class)
    public void removeAllNPE() {
        init();
        c.removeAll(CollectionTestUtil.keysWithNull);

    }

    @Test
    public void removeAll() {
        init();
        c.removeAll(CollectionTestUtil.asList(2, 3));

        c = newCache(5);
        c.removeAll(CollectionTestUtil.asList(2, 3));
        assertSize(3);

        c = newCache(5);
        c.removeAll(CollectionTestUtil.asList(5, 6));
        assertSize(4);
    }

    /**
     * {@link Cache#put(Object, Object)} lazy starts the cache.
     */
    @Test
    public void removeAllLazyStart() {
        init();
        assertFalse(c.isStarted());
        c.removeAll(CollectionTestUtil.asList(2, 3));
        checkLazystart();
    }

    /**
     * {@link Cache#containsKey()} should not fail when cache is shutdown.
     */
    @Test
    public void removeAllShutdownISE() {
        c = newCache(5);
        assertTrue(c.isStarted());
        c.shutdown();

        // should fail
        c.removeAll(CollectionTestUtil.asList(2, 3));
    }
}
