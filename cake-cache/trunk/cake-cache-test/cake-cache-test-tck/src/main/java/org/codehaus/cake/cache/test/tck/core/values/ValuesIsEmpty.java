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

import java.util.concurrent.TimeUnit;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

/**
 * 
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: ValuesIsEmpty.java 415 2007-11-09 08:25:23Z kasper $
 */
public class ValuesIsEmpty extends AbstractCacheTCKTest {

    /**
     * isEmpty is true of empty map and false for non-empty.
     */
    @Test
    public void isEmptyValues() {
        assertTrue(newCache(0).values().isEmpty());
        assertFalse(newCache(5).values().isEmpty());
    }

    /**
     * {@link Cache#isEmpty()} lazy starts the cache.
     */
    @Test
    public void isEmptyLazyStart() {
        c = newCache(0);
        assertFalse(c.isStarted());
        c.values().isEmpty();
        checkLazystart();
    }

    /**
     * {@link Cache#isEmpty()} should not fail when cache is shutdown.
     * 
     * @throws InterruptedException
     *             was interrupted
     */
    @Test
    public void isEmptyShutdown() throws InterruptedException {
        c = newCache(5);
        assertTrue(c.isStarted());
        c.shutdown();

        // should not fail, but result is undefined until terminated
        c.values().isEmpty();

        assertTrue(c.awaitTermination(1, TimeUnit.SECONDS));

        boolean isEmpty = c.values().isEmpty();
        assertTrue(isEmpty);// cache should be empty
    }

}
