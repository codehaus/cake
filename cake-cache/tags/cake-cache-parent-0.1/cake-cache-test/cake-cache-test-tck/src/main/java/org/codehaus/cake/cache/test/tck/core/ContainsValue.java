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

import java.util.concurrent.TimeUnit;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

/**
 * Tests {@link Cache#containsValue}.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: ContainsValue.java 415 2007-11-09 08:25:23Z kasper $
 */
public class ContainsValue extends AbstractCacheTCKTest {

    /**
     * {@link Cache#containsValue} returns <code>true</code> for contained values.
     */
    @Test
    public void containsValue() {
        c = newCache(5);
        assertTrue(c.containsValue("A"));
        assertFalse(c.containsValue("Z"));
    }

    /**
     * <code>null</code> parameter to {@link Cache#containsValue} throws {@link NullPointerException}.
     */
    @Test(expected = NullPointerException.class)
    public void containsValueNPE() {
        c = newCache(5);
        c.containsValue(null);
    }

    /**
     * {@link Cache#containsValue} lazy starts the cache.
     */
    @Test
    public void containsValueLazyStart() {
        c = newCache(0);
        assertFalse(c.isStarted());
        c.containsValue("A");
        checkLazystart();
    }

    /**
     * {@link Cache#containsValue()} should not fail when cache is shutdown.
     * 
     * @throws InterruptedException
     *             was interrupted
     */
    @Test
    public void containsValueShutdown() throws InterruptedException {
        c = newCache(5);
        assertTrue(c.isStarted());
        c.shutdown();

        // should not fail, but result is undefined until terminated
        c.containsValue("A");

        assertTrue(c.awaitTermination(1, TimeUnit.SECONDS));

        boolean containsValue = c.containsValue("A");
        assertFalse(containsValue);// cache should be empty
    }
}
