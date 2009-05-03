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
package org.codehaus.cake.cache.test.tck.asmap;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import org.codehaus.cake.cache.Cache;
import org.junit.Test;

/**
 * Tests {@link Cache#containsValue}.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: ContainsValue.java 327 2009-04-08 09:34:27Z kasper $
 */
public class ContainsValue extends AbstractAsMapTCKTest {

    /** {@link ConcurrentMap#containsValue} returns <code>true</code> for contained values. */
    @Test
    public void containsValue() {
        init(5);
        assertContainsValue(M1);
        assertNotContainsValue(M6);
    }

    /** <code>null</code> parameter to {@link ConcurrentMap#containsValue} throws {@link NullPointerException}. */
    @Test(expected = NullPointerException.class)
    public void containsValueNPE() {
        init(5);
        containsValue(null);
    }

    /** {@link ConcurrentMap#containsValue} lazy starts the cache. */
    @Test
    public void containsValueLazyStart() {
        init();
        assertNotStarted();
        assertNotContainsValue(M1);
        assertStarted();
    }

    /** {@link ConcurrentMap#containsValue()} should not fail when cache is shutdown. */
    @Test
    public void containsValueShutdown() throws InterruptedException {
        init(5);
        assertContainsValue(M1);
        shutdown();

        containsValue(M1);// should not fail, but result is undefined until terminated

        awaitTermination();
        assertFalse(containsKey(M1));
    }
}
